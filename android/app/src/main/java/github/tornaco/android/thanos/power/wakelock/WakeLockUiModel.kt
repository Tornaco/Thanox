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

package github.tornaco.android.thanos.power.wakelock

import androidx.compose.runtime.Stable
import github.tornaco.android.thanos.core.power.SeenWakeLock

@Stable
data class WakeLockUiModel(
    val tag: String,
    val flags: Int,
    val ownerPackageName: String,
    val ownerUserId: Int,
    val isHeld: Boolean,
    val isBlock: Boolean,
)

fun SeenWakeLock.toUiModel() = WakeLockUiModel(
    tag = this.tag,
    flags = this.flags,
    ownerPackageName = this.ownerPackageName,
    ownerUserId = this.ownerUserId,
    isHeld = this.isHeld,
    isBlock = this.isBlock
)

fun WakeLockUiModel.toWakeLock() = SeenWakeLock(
    tag, flags, ownerPackageName, ownerUserId, 0L, isHeld, isBlock
)