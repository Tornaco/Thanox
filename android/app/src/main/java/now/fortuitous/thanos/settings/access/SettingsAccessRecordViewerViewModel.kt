/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package now.fortuitous.thanos.settings.access

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.common.LifeCycleAwareViewModel
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.secure.ops.SettingsAccessRecord
import github.tornaco.android.thanos.core.util.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class SettingsAccessRecordViewerViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    LifeCycleAwareViewModel() {
    private val _state = MutableStateFlow(ViewerState(isLoading = true))
    val state = _state.asStateFlow()

    private val thanox by lazy { ThanosManager.from(context) }

    private var searchJobs: MutableList<Job> = mutableListOf()

    fun init() {
        viewModelScope.launch {
            loadAccessRecords()
        }
    }

    fun refresh(delay: Long = 0L): Job {
        return viewModelScope.launch {
            delay(delay)
            loadAccessRecords()
        }
    }

    private suspend fun loadAccessRecords() = withContext(Dispatchers.IO) {
        updateLoadingState(true)

        val isRecordEnabled = thanox.appOpsManager.isSettingsRecordEnabled

        val readRecords =
            thanox.appOpsManager.getSettingsReadRecords(null)
        val writeRecords =
            thanox.appOpsManager.getSettingsWriteRecords(null)

        // Filter items
        val rwFilterItems = listOf(ReadFilterItem(), WriteFilterItem())
        val packageFilterItems =
            (writeRecords + readRecords).map { it.callerPackageName }.distinct()
                .mapNotNull { pkgName ->
                    val appInfo = thanox.pkgManager.getAppInfo(pkgName)
                    appInfo?.let { app ->
                        PackageFilterItem(pkgName) { app.appLabel }
                    }
                }
        val selectableFilterItems = (rwFilterItems + packageFilterItems).map {
            SelectableFilterItem(isSelected = true, filterItem = it)
        }

        _state.value = _state.value.copy(
            isRecordEnabled = isRecordEnabled,
            rawReadRecords = readRecords,
            rawWriteRecords = writeRecords,
            appFilterItems = selectableFilterItems
        )

        transformAndFilterRecords(writeRecords, readRecords)
    }

    private suspend fun transformAndFilterRecords(
        writeRecords: List<SettingsAccessRecord>,
        readRecords: List<SettingsAccessRecord>
    ) = withContext(Dispatchers.IO) {
        // Filter now.
        val filteredReadRecords = readRecords.mapNotNull { record ->
            val appInfo = thanox.pkgManager.getAppInfo(record.callerPackageName)
            appInfo?.let { app ->
                DetailedSettingsAccessRecord(
                    appInfo = app,
                    record = record,
                    timeStr = DateUtils.formatLongForMessageTime(record.timeMillis),
                    isRead = true
                )
            }
        }.filter { filterItemFilter(it.record, true) }

        val filteredWriteRecords = writeRecords
            .mapNotNull { record ->
                val appInfo = thanox.pkgManager.getAppInfo(record.callerPackageName)
                appInfo?.let { app ->
                    DetailedSettingsAccessRecord(
                        app,
                        record,
                        DateUtils.formatLongForMessageTime(record.timeMillis),
                        isRead = false
                    )
                }
            }.filter { filterItemFilter(it.record, false) }

        val mergedRecords =
            (filteredReadRecords + filteredWriteRecords).sortedByDescending { it.record.timeMillis }

        _state.value = _state.value.copy(
            isLoading = false,
            mergedRecords = mergedRecords
        )
    }

    private fun filterItemFilter(record: SettingsAccessRecord, isRead: Boolean): Boolean {
        val filterItem: List<SelectableFilterItem> =
            state.value.appFilterItems.filter { it.isSelected }

        // Black list mode for Write Only...
        if (filterItem.firstOrNull { it.filterItem is ReadFilterItem } == null && isRead) {
            return false
        }

        // Black list mode
        if (filterItem.firstOrNull { it.filterItem is WriteFilterItem } == null && !isRead) {
            return false
        }

        if (filterItem.firstOrNull { it.filterItem is PackageFilterItem && it.filterItem.id == record.callerPackageName } != null) {
            return true
        }

        return false
    }

    private fun updateLoadingState(isLoading: Boolean) {
        _state.value = _state.value.copy(isLoading = isLoading)
    }

    fun onFilterItemSelected(item: SelectableFilterItem, selected: Boolean) {
        val updatedFilterItems = _state.value.appFilterItems.toMutableList().apply {
            firstOrNull { it.filterItem == item.filterItem }?.let { it.isSelected = selected }
        }
        _state.value = _state.value.copy(appFilterItems = updatedFilterItems)

        viewModelScope.launch {
            transformAndFilterRecords(state.value.rawWriteRecords, state.value.rawReadRecords)
        }
    }

    fun clearAllRecords() {
        updateLoadingState(true)
        thanox.appOpsManager.clearSettingsReadRecords()
        thanox.appOpsManager.clearSettingsWriteRecords()
        refresh(delay = 300)
    }

    fun setRecordEnabled(enabled: Boolean) {
        thanox.appOpsManager.isSettingsRecordEnabled = enabled
        _state.value = _state.value.copy(
            isRecordEnabled = thanox.appOpsManager.isSettingsRecordEnabled
        )
    }
}