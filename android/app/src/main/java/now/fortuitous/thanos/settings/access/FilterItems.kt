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

import github.tornaco.android.thanos.module.compose.common.widget.FilterItem

data class PackageFilterItem(
    override val id: String,
    override val label: String
) : FilterItem

data class ReadFilterItem(
    override val id: String = "read",
    override val label: String = "Read"
) : FilterItem

data class WriteFilterItem(
    override val id: String = "write",
    override val label: String = "Write"
) : FilterItem