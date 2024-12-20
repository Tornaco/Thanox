package github.tornaco.thanos.module.component.manager.redesign

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import github.tornaco.android.thanos.common.UiState
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.thanos.module.component.manager.model.ComponentModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@SuppressLint("StaticFieldLeak")
abstract class ComponentsVM(
    val context: Context,
) :
    ViewModel() {
    protected val thanox: ThanosManager by lazy { ThanosManager.from(context) }

    private val _appInfo = MutableStateFlow(AppInfo.dummy())
    private val _searchQuery = MutableStateFlow("")
    private val _refresh = MutableStateFlow(System.currentTimeMillis())

    val components =
        combineTransform<AppInfo, String, Long, UiState<List<ComponentModel>>>(
            _appInfo,
            _searchQuery,
            _refresh,
            transform = { appInfo, query, _ ->
                emit(UiState.Loading)
                kotlin.runCatching {
                    emit(UiState.Loaded(loadComponents(appInfo, query)))
                }.onFailure {
                    emit(UiState.Error(it))
                }
            }
        ).stateIn(viewModelScope, SharingStarted.Lazily, initialValue = UiState.Loading)

    abstract fun loadComponents(
        appInfo: AppInfo,
        query: String
    ): List<ComponentModel>

    fun initApp(appInfo: AppInfo) {
        _appInfo.update { appInfo }
    }

    fun search(query: String) {
        _searchQuery.update { query }
    }

    fun refresh() {
        _refresh.update { System.currentTimeMillis() }
    }
}