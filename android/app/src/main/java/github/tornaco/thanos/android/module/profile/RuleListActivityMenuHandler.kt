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

package github.tornaco.thanos.android.module.profile

import android.view.MenuItem
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.core.util.OsUtils
import github.tornaco.android.thanos.support.AppFeatureManager
import github.tornaco.android.thanos.util.BrowserUtils
import github.tornaco.thanos.android.module.profile.example.ProfileExampleActivity
import github.tornaco.thanos.android.module.profile.online.OnlineProfileActivity

class RuleListActivityMenuHandler {

    fun RuleListActivity.handleOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.action_view_wiki == item.itemId) {
            BrowserUtils.launch(this, BuildProp.THANOX_URL_DOCS_PROFILE)
            return true
        }
        if (R.id.action_import_from_file == item.itemId) {
            AppFeatureManager.withSubscriptionStatus(this) {
                if (it || viewModel.ruleInfoList != null && viewModel.ruleInfoList.size <= 3) {
                    if (OsUtils.isTOrAbove()) {
                        RuleListActivityPermissionRequester.importFromFileTOrAboveChecked(this)
                    } else {
                        RuleListActivityPermissionRequester.importFromFileTBelowChecked(this)
                    }
                } else {
                    AppFeatureManager.showSubscribeDialog(this)
                }
            }
            return true
        }
        if (R.id.action_import_examples == item.itemId) {
            ProfileExampleActivity.Starter.start(this)
            return true
        }
        if (R.id.action_online == item.itemId) {
            AppFeatureManager.withSubscriptionStatus(this) {
                if (it) {
                    OnlineProfileActivity.Starter.start(this)
                } else {
                    AppFeatureManager.showSubscribeDialog(this)
                }
            }
            return true
        }
        if (R.id.action_global_var == item.itemId) {
            GlobalVarListActivity.start(this)
            return true
        }
        if (R.id.action_add == item.itemId) {
            AppFeatureManager.withSubscriptionStatus(this) {
                if (it || viewModel.ruleInfoList != null && viewModel.ruleInfoList.size <= 3) {
                    onRequestAddNewRule()
                } else {
                    AppFeatureManager.showSubscribeDialog(this)
                }
            }
            return true
        }
        if (R.id.action_rule_engine == item.itemId) {
            RuleEngineSettingsActivity.start(this)
            return true
        }
        if (R.id.action_rule_console == item.itemId) {
            AppFeatureManager.withSubscriptionStatus(this) {
                if (it) {
                    ConsoleActivity.Starter.start(this)
                } else {
                    AppFeatureManager.showSubscribeDialog(this)
                }
            }
            return true
        }
        if (R.id.action_rule_log == item.itemId) {
            LogActivity.Starter.start(this)
            return true
        }
        return false
    }
}