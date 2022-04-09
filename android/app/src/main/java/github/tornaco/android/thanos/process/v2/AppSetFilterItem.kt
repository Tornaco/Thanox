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

package github.tornaco.android.thanos.process.v2

import android.content.Context
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.module.compose.common.widget.FilterItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class AppSetFilterItem(
    override val id: String,
    override val label: String
) : FilterItem

object Loader {
    suspend fun loadAllFromAppSet(
        context: Context
    ): List<AppSetFilterItem> {
        val thanos = ThanosManager.from(context)
        return withContext(Dispatchers.IO) {
            if (!thanos.isServiceInstalled) {
                emptyList()
            } else {
                thanos.pkgManager.getAllPackageSets(false).mapIndexed { _, packageSet ->
                    AppSetFilterItem(
                        packageSet.id,
                        packageSet.label,
                    )
                }
            }
        }
    }
}