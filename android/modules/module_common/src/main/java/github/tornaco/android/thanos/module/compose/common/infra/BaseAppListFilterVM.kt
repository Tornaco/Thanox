package github.tornaco.android.thanos.module.compose.common.infra

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.common.AppLabelSearchFilter
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.PREBUILT_PACKAGE_SET_ID_3RD
import github.tornaco.android.thanos.module.compose.common.infra.sort.AppSortTools
import github.tornaco.android.thanos.module.compose.common.loader.AppSetFilterItem
import github.tornaco.android.thanos.module.compose.common.loader.Loader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class AppListUiState(
    val apps: List<AppUiModel> = emptyList(),
    val isLoading: Boolean = false,

    val appFilterItems: List<AppSetFilterItem> = emptyList(),
    val selectedAppSetFilterItem: AppSetFilterItem? = null,

    val appSort: AppSortTools = AppSortTools.Default,
    val sortReverse: Boolean = false,

    val searchKeyword: String = ""
)

@HiltViewModel
class BaseAppListFilterVM @Inject constructor(@ApplicationContext private val context: Context) :
    StateViewModel<AppListUiState>(initState = { AppListUiState() }) {
    private var config: BaseAppListFilterContainerConfig = BaseAppListFilterContainerConfig(
        title = { "" },
        featureId = "",
        appItemConfig = AppItemConfig(loader = { _, _ -> emptyList() })
    )

    private val thanox by lazy { ThanosManager.from(context) }

    private val appLabelSearchFilter by lazy { AppLabelSearchFilter() }
    private val searchJobs: MutableList<Job> = mutableListOf()

    fun installIn(config: BaseAppListFilterContainerConfig) {
        this.config = config
        viewModelScope.launch {
            loadDefaultAppFilterItems()
            loadApps("installIn")
        }
    }

    private suspend fun loadApps(reason: String) {
        XLog.d("loadApps: $reason")
        updateState { copy(isLoading = true) }
        withContext(Dispatchers.IO) {
            with(config) {
                val searchKeyword = state.value.searchKeyword.takeIf { it.isNotBlank() }.orEmpty()
                val sort = state.value.appSort
                val sortReverse = state.value.sortReverse

                val appModels = appItemConfig.loader(
                    context,
                    state.value.selectedAppSetFilterItem?.id ?: PREBUILT_PACKAGE_SET_ID_3RD
                ).filter {
                    searchKeyword.isEmpty() || (searchKeyword.length > 2 && it.appInfo.pkgName.contains(
                        searchKeyword
                    )) || appLabelSearchFilter.matches(searchKeyword, it.appInfo.appLabel)
                }.let {
                    val appSorterProvider = sort.provider
                    val sortApplied =
                        it.sortedWith(appSorterProvider.comparator(context))
                    if (sortReverse) {
                        sortApplied.reversed()
                    } else {
                        sortApplied
                    }
                }.map { model ->
                    val appSorterProvider = sort.provider
                    val appSortDescription = appSorterProvider.getAppSortDescription(
                        context,
                        model
                    )
                    if (appSortDescription.isNullOrEmpty()) {
                        model
                    } else {
                        model.copy(
                            description = model.description?.let {
                                it + System.lineSeparator() + appSortDescription
                            } ?: appSortDescription
                        )
                    }
                }.let {
                    if (sort.relyOnUsageStats()) {
                        inflateAppUsageStats(it)
                    } else {
                        it
                    }
                }
                updateState {
                    copy(apps = appModels, isLoading = false)
                }
            }
        }
    }

    fun onFilterItemSelected(appSetFilterItem: AppSetFilterItem) {
        updateState {
            copy(selectedAppSetFilterItem = appSetFilterItem)
        }
        refresh("onFilterItemSelected")
    }

    private suspend fun loadDefaultAppFilterItems() {
        val appFilterListItems = Loader.loadAllFromAppSet(context)
        updateState {
            copy(
                // Default select 3-rd
                selectedAppSetFilterItem = appFilterListItems.find {
                    it.id == PREBUILT_PACKAGE_SET_ID_3RD
                },
                appFilterItems = appFilterListItems
            )
        }
    }

    private fun inflateAppUsageStats(res: List<AppUiModel>): List<AppUiModel> {
        val statsMap =
            thanox.usageStatsManager.queryAndAggregateUsageStats(0, System.currentTimeMillis())
        return res.map { app ->
            if (statsMap.containsKey(app.appInfo.pkgName)) {
                val stats = statsMap[app.appInfo.pkgName]
                if (stats != null) {
                    app.copy(
                        lastUsedTimeMills = stats.lastTimeUsed,
                        totalUsedTimeMills = stats.totalTimeInForeground
                    )
                } else app
            } else {
                app
            }
        }
    }

    fun refresh(reason: String) {
        viewModelScope.launch { loadApps(reason) }
    }

    fun keywordChanged(it: String) {
        updateState { copy(searchKeyword = it) }
        searchJobs.forEach { it.cancel() }
        searchJobs.clear()
        searchJobs.add(
            viewModelScope.launch {
                loadApps("keywordChanged-$it")
            }
        )
    }

    fun updateSort(sort: AppSortTools) {
        updateState { copy(appSort = sort) }
        refresh("updateSort")
    }

    fun updateSortReverse(reverse: Boolean) {
        updateState { copy(sortReverse = reverse) }
        refresh("updateSortReverse")
    }

    fun updateAppCheckState(app: AppUiModel, checked: Boolean) {
        updateState {
            copy(apps = apps.toMutableList().apply {
                val index = indexOfFirst { app.appInfo.pkgName == it.appInfo.pkgName }
                if (index >= 0) {
                    set(index, get(index).copy(isChecked = checked))
                }
            })
        }
    }
}