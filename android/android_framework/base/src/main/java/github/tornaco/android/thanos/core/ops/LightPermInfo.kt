/*
 * Copyright (C) 2019 The Android Open Source Project
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
 */

package github.tornaco.android.thanos.core.ops

import android.content.pm.PermissionInfo

/**
 * A light version of the system PermissionInfo
 *
 * @param name The name of this permission
 * @param packageName The name of the package which defines this permission
 * @param group The optional name of the group this permission is in
 * @param backgroundPermission The background permission associated with this permission
 * @param flags The system flags of this permission
 */
data class LightPermInfo(
    val name: String,
    val packageName: String,
    val group: String?,
    val backgroundPermission: String?,
    val flags: Int
) {
    val hasBackgroundPermission get() = backgroundPermission != null

    constructor (permInfo: PermissionInfo) : this(
        permInfo.name,
        permInfo.packageName,
        permInfo.group,
        permInfo.backgroundPermission,
        permInfo.flags
    )
}