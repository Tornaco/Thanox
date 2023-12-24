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

package now.fortuitous.thanos.notification

import android.content.Context
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxSmallAppBarScaffold
import github.tornaco.android.thanos.util.ActivityUtils

@AndroidEntryPoint
class NotificationCenterActivity : ComposeThemeActivity() {
    object Starter {
        fun start(context: Context?) {
            ActivityUtils.startActivity(context, NotificationCenterActivity::class.java)
        }
    }

    @Composable
    override fun Content() {
        ThanoxSmallAppBarScaffold(title = {
            Text(
                text = stringResource(id = R.string.module_notification_recorder_feature_title_notification_recorder),
                style = TypographyDefaults.appBarTitleTextStyle()
            )
        },
            onBackPressed = { finish() },
            actions = {
            }) { contentPadding ->

        }
    }
}