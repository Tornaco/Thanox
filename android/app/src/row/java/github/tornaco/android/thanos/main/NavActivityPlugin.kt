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

package github.tornaco.android.thanos.main

import github.tornaco.android.thanos.app.PLayLvlCheckActivity

object NavActivityPlugin {
    fun onCreate(activity: NavActivity): Boolean {
        return if (!github.tornaco.android.thanos.app.Init.isLVLChecked(activity)
            && github.tornaco.android.thanos.app.Init.s == 0
        ) {
            PLayLvlCheckActivity.Starter.start(activity)
            true
        } else false
    }

    fun showPrivacyAgreement(activity: NavActivity): Boolean = false
}