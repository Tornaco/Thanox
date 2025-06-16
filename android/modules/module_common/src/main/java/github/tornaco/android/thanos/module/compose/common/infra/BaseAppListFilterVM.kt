package github.tornaco.android.thanos.module.compose.common.infra

import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.common.AppLabelSearchFilter
import github.tornaco.android.thanos.common.AppListModel
import github.tornaco.android.thanos.core.pm.PREBUILT_PACKAGE_SET_ID_3RD
import github.tornaco.android.thanos.module.compose.common.loader.AppSetFilterItem
import github.tornaco.android.thanos.module.compose.common.loader.Loader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class AppListUiState(
    val apps: List<AppListModel> = emptyList(),
    val isLoading: Boolean = false,

    val appFilterItems: List<AppSetFilterItem> = emptyList(),
    val selectedAppSetFilterItem: AppSetFilterItem? = null,

    val searchKeyword: String = ""
)

@HiltViewModel
class BaseAppListFilterVM @Inject constructor(@ApplicationContext private val context: Context) :
    StateViewModel<AppListUiState>(initState = { AppListUiState() }) {
    private lateinit var config: BaseAppListFilterContainerConfig

    private val appLabelSearchFilter by lazy { AppLabelSearchFilter() }
    private val searchJobs: MutableList<Job> = mutableListOf()

    fun installIn(config: BaseAppListFilterContainerConfig) {
        this.config = config
        viewModelScope.launch {
            loadDefaultAppFilterItems()
            loadApps()
        }
    }

    private suspend fun loadApps() {
        updateState { copy(isLoading = true) }
        withContext(Dispatchers.IO) {
            with(config) {
                val searchKeyword = state.value.searchKeyword.takeIf { it.isNotBlank() }.orEmpty()
                val appModels = loader(
                    context,
                    state.value.selectedAppSetFilterItem?.id ?: PREBUILT_PACKAGE_SET_ID_3RD
                ).filter {
                    searchKeyword.isEmpty() || (searchKeyword.length > 2 && it.appInfo.pkgName.contains(
                        searchKeyword
                    )) || appLabelSearchFilter.matches(searchKeyword, it.appInfo.appLabel)
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
        refresh()
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

    fun refresh() {
        viewModelScope.launch { loadApps() }
    }

    fun keywordChanged(it: String) {
        updateState { copy(searchKeyword = it) }
        refresh()
    }
}