package github.tornaco.thanos.module.component.manager.redesign

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.common.UiState
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.pm.ComponentInfo
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.thanos.module.component.manager.ComponentRule
import github.tornaco.thanos.module.component.manager.fallbackRule
import github.tornaco.thanos.module.component.manager.getActivityRule
import github.tornaco.thanos.module.component.manager.model.ComponentModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import util.PinyinComparatorUtils
import java.util.UUID

data class ComponentGroup(
    val rule: ComponentRule = fallbackRule,
    val components: List<ComponentModel> = emptyList(),
    val id: String = UUID.randomUUID().toString()
)

data class MultipleSelectState(
    val isSelectMode: Boolean = false,
    val selectedItems: Set<ComponentModel> = emptySet(),
)

data class BatchOpState(
    val isWorking: Boolean = false,
    val progressText: String = ""
)

@SuppressLint("StaticFieldLeak")
abstract class ComponentsVM(
    val context: Context,
) :
    ViewModel() {
    private val thanox: ThanosManager by lazy { ThanosManager.from(context) }

    private val _appInfo = MutableStateFlow(AppInfo.dummy())
    private val _searchQuery = MutableStateFlow("")
    private val _refresh = MutableStateFlow(System.currentTimeMillis())

    val collapsedGroups = MutableStateFlow(emptySet<String>())

    val selectState = MutableStateFlow(MultipleSelectState())
    val batchOpState = MutableStateFlow(BatchOpState())

    val components =
        combineTransform<AppInfo, String, Long, UiState<List<ComponentGroup>>>(
            _appInfo,
            _searchQuery,
            _refresh,
            transform = { appInfo, query, _ ->
                emit(UiState.Loading)
                kotlin.runCatching {
                    emit(UiState.Loaded(loadComponentsGroups(appInfo, query)))
                }.onFailure {
                    emit(UiState.Error(it))
                }
            }
        ).onEach { uiState ->
            if (uiState is UiState.Loaded) {
                val fallbackGroup = uiState.data.firstOrNull { it.rule == fallbackRule }
                val fallbackSize =
                    fallbackGroup?.components?.size ?: 0
                if (fallbackSize > 100) {
                    fallbackGroup?.let { expand(it, false) }
                }
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, initialValue = UiState.Loading)

    private suspend fun loadComponentsGroups(
        appInfo: AppInfo,
        query: String
    ): List<ComponentGroup> {
        return withContext(Dispatchers.IO) {
            val res: MutableList<ComponentModel> = ArrayList()
            for (i in 0 until Int.MAX_VALUE) {
                val batch =
                    thanox.getComponentsInBatch(
                        /* userId = */ appInfo.userId,
                        /* packageName = */ appInfo.pkgName,
                        /* itemCountInEachBatch = */ 20,
                        /* batchIndex = */ i
                    ) ?: break
                batch
                    .filter {
                        TextUtils.isEmpty(query) || it.name.lowercase().contains(query.lowercase())
                    }
                    .forEach { info ->
                        res.add(
                            ComponentModel.builder()
                                .name(info.name)
                                .componentName(info.componentName)
                                .isDisabledByThanox(info.isDisabledByThanox)
                                .label(info.label)
                                .componentObject(info)
                                .enableSetting(info.enableSetting)
                                .componentRule(getActivityRule(info.componentName))
                                .build()
                        )
                    }
            }
            res.sort()

            res.groupBy { it.componentRule }.toSortedMap { o1, o2 ->
                if (o1 == fallbackRule && o2 != fallbackRule) return@toSortedMap 1
                if (o1 != fallbackRule && o2 == fallbackRule) return@toSortedMap -1
                PinyinComparatorUtils.compare(
                    o1?.label.orEmpty(),
                    o2?.label.orEmpty()
                )
            }.map {
                ComponentGroup(it.key, it.value)
            }
        }
    }

    abstract fun ThanosManager.getComponentsInBatch(
        userId: Int, packageName: String,
        itemCountInEachBatch: Int,
        batchIndex: Int
    ): List<ComponentInfo>?

    fun initApp(appInfo: AppInfo) {
        _appInfo.update { appInfo }
    }

    fun search(query: String) {
        _searchQuery.update { query }
    }

    fun refresh() {
        _refresh.update { System.currentTimeMillis() }
    }

    fun expand(group: ComponentGroup, expand: Boolean) {
        collapsedGroups.update {
            if (expand) {
                collapsedGroups.value.minus(group.id)
            } else {
                collapsedGroups.value.plus(group.id)
            }
        }
    }

    fun exitSelectionState() {
        selectState.update {
            it.copy(
                isSelectMode = false,
                selectedItems = emptySet()
            )
        }
    }

    fun select(model: ComponentModel, select: Boolean) {
        val updatedItems = if (select) {
            selectState.value.selectedItems + model
        } else {
            selectState.value.selectedItems - model
        }
        val isSelectMode = updatedItems.isNotEmpty()
        selectState.update {
            it.copy(
                selectedItems = updatedItems,
                isSelectMode = isSelectMode
            )
        }
    }

    fun select(group: ComponentGroup, select: Boolean) {
        val updatedItems = if (select) {
            selectState.value.selectedItems + group.components
        } else {
            selectState.value.selectedItems - group.components.toSet()
        }
        val isSelectMode = updatedItems.isNotEmpty()
        selectState.update {
            it.copy(
                selectedItems = updatedItems,
                isSelectMode = isSelectMode
            )
        }
    }

    fun setComponentState(
        componentModel: ComponentModel,
        setToEnabled: Boolean
    ): Boolean {
        XLog.d("setComponentState: $componentModel $setToEnabled")
        val appInfo = _appInfo.value
        val newState =
            if (setToEnabled) PackageManager.COMPONENT_ENABLED_STATE_ENABLED else PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        if (newState == componentModel.enableSetting) {
            return false
        }
        if (thanox.isServiceInstalled) {
            componentModel.enableSetting = newState
            thanox.pkgManager.setComponentEnabledSetting(
                appInfo.userId,
                componentModel.componentName,
                newState,
                0 /* Kill it */
            )
            return true
        }
        return false
    }

    fun appBatchOp(enabled: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val appInfo = _appInfo.value
                val modelList = selectState.value.selectedItems.toList()
                val totalCount = modelList.size
                thanox.activityManager.forceStopPackage(
                    Pkg.fromAppInfo(
                        appInfo
                    ), "ComponentList UI selectAll"
                )
                // Wait 1s.
                batchOpState.update { it.copy(isWorking = true, progressText = "") }
                for (i in modelList.indices) {
                    val componentModel = modelList[i]
                    batchOpState.update {
                        it.copy(
                            progressText = (i + 1).toString() + "/" + totalCount
                        )
                    }
                    if (enabled != componentModel.isEnabled && setComponentState(
                            componentModel,
                            enabled
                        )
                    ) {
                        try {
                            // Maybe a short delay will make it safer.
                            Thread.sleep(30)
                        } catch (ignored: InterruptedException) {
                        }
                    }
                }
                batchOpState.update { it.copy(isWorking = false, progressText = "") }

                exitSelectionState()
                refresh()
            }
        }
    }
}