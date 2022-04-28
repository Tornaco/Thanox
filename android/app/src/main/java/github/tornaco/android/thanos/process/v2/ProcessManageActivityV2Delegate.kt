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


import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.ThanosApp.Companion.isPrc
import github.tornaco.android.thanos.app.donate.DonateSettings

class ProcessManageActivityV2Delegate : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isPrc() && !DonateSettings.isActivated(this)) {
            Toast.makeText(
                this, R.string.module_donate_donated_available, Toast.LENGTH_SHORT
            ).show()
            return
        }

        ProcessManageActivityV2.Starter.start(this)
        finish()
    }
}