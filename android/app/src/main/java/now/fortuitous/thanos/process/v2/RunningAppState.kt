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

package now.fortuitous.thanos.process.v2

import android.app.ActivityManager
import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import github.tornaco.android.thanos.core.app.RunningAppProcessInfoCompat
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.thanos.module.component.manager.redesign.rule.BlockerRule
import github.tornaco.thanos.module.component.manager.redesign.rule.ComponentRule
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class RunningAppState(
    val appInfo: AppInfo,
    val processState: List<RunningProcessState>,
    val allProcessIsCached: Boolean,
    val totalPss: Long,
    val sizeStr: String,
    val runningTimeMillis: Long?,
    val isPlayingBack: Boolean
) : Parcelable {
    val serviceCount: Int get() = processState.flatMap { it.runningServices }.size
}

@Parcelize
data class RunningProcessState(
    val pkg: Pkg,
    val process: RunningAppProcessInfoCompat,
    val runningServices: List<RunningService>,
    val sizeStr: String,
    val processPss: Long,
) : Parcelable {
    @IgnoredOnParcel
    var isStopped by mutableStateOf(false)

    val isMain get() = process.processName == pkg.pkgName
}

@Parcelize
data class RunningService(
    val running: ActivityManager.RunningServiceInfo,
    val serviceLabel: String,
    val clientLabel: String? = null,
    val lcRule: ComponentRule? = null,
    val blockRule: BlockerRule? = null

) : Parcelable {
    @IgnoredOnParcel
    var isStopped by mutableStateOf(false)
}