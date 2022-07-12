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

package github.tornaco.thanos.android.module.profile.repo

import androidx.annotation.Keep


/*
* {
    "author": "Tornaco",
    "version": 1,
    "tags": [
        "app_opt"
    ],
    "profile": {
        "name": "The first profile",
        "description": "Contribute online profile via github.",
        "priority": 1,
        "condition": "frontPkgChanged",
        "actions": [
            "ui.showShortToast(\"frontPkgChanged\");"
        ]
    }
}
* */

@Keep
data class OnlineProfile(
    val author: String,
    val version: Int,
    val tags: List<String>,
    val profile: Profile
)

@Keep
data class Profile(
    val name: String,
    val description: String,
    val priority: Int,
    val condition: String,
    val actions: List<String>
)