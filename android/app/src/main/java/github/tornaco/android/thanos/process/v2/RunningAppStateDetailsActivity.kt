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

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import dev.enro.annotations.NavigationDestination
import dev.enro.core.NavigationKey
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import kotlinx.parcelize.Parcelize

@Parcelize
data class RunningAppStateDetails(val state: RunningAppState) : NavigationKey.WithResult<Boolean>

@AndroidEntryPoint
@NavigationDestination(RunningAppStateDetails::class)
class RunningAppStateDetailsActivity : ComposeThemeActivity() {
    override fun isF(): Boolean {
        return true
    }

    override fun isADVF(): Boolean {
        return true
    }

    @Composable
    override fun Content() {
        Surface {
            RunningAppStateDetailsPage()
        }
    }
}