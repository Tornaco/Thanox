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

import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.secure.ops.SettingsAccessRecord
import github.tornaco.android.thanos.module.compose.common.widget.FilterItem

data class ViewerState(
    val isLoading: Boolean = false,

    val isRecordEnabled: Boolean = false,

    val rawReadRecords: List<SettingsAccessRecord> = emptyList(),
    val rawWriteRecords: List<SettingsAccessRecord> = emptyList(),

    val mergedRecords: List<DetailedSettingsAccessRecord> = emptyList(),
    val appFilterItems: List<SelectableFilterItem> = emptyList(),
)

data class DetailedSettingsAccessRecord(
    val appInfo: AppInfo,
    val record: SettingsAccessRecord,
    val timeStr: String,
    val isRead: Boolean
)

data class SelectableFilterItem(var isSelected: Boolean, val filterItem: FilterItem)