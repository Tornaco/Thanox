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

package github.tornaco.thanos.android.module.profile.online

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.getColorAttribute
import github.tornaco.android.thanos.module.compose.common.requireActivity
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.*
import github.tornaco.android.thanos.util.ActivityUtils
import github.tornaco.android.thanos.util.ToastUtils
import github.tornaco.thanos.android.module.profile.R
import github.tornaco.thanos.android.module.profile.repo.OnlineProfile

@AndroidEntryPoint
class OnlineProfileActivity : ComposeThemeActivity() {

    object Starter {
        fun start(context: Context?) {
            ActivityUtils.startActivity(context, OnlineProfileActivity::class.java)
        }
    }

    override fun isADVF(): Boolean {
        return true
    }

    override fun isF(): Boolean {
        return true
    }

    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<OnlineProfileViewModel>()
        val state by viewModel.state.collectAsState()
        val context = LocalContext.current

        SideEffect {
            viewModel.loadOnlineProfiles()
        }

        LaunchedEffect(viewModel) {
            viewModel.events.collect { event ->
                when (event) {
                    is Event.ImportSuccess -> {
                        ToastUtils.ok(context)
                    }
                    is Event.ImportFail -> {
                        ToastUtils.nook(context, event.error)
                    }
                    is Event.ImportFailRuleWithSameNameAlreadyExists -> {
                        ToastUtils.nook(context, "${event.name} already exists")
                    }
                }
            }
        }

        ThanoxSmallAppBarScaffold(title = {
            Text(
                text = stringResource(id = R.string.module_profile_rule_online),
                style = TypographyDefaults.appBarTitleTextStyle()
            )
        },
            onBackPressed = { finish() },
            actions = {

            }) { contentPadding ->

            SwipeRefresh(
                modifier = Modifier
                    .fillMaxSize(),
                state = rememberSwipeRefreshState(state.isLoading),
                onRefresh = { viewModel.refresh() },
                indicatorPadding = contentPadding,
                clipIndicatorToPadding = false,
                indicator = { state, refreshTriggerDistance ->
                    SwipeRefreshIndicator(
                        state = state,
                        refreshTriggerDistance = refreshTriggerDistance,
                        scale = true,
                        arrowEnabled = false,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                ProfileList(contentPadding, state) {
                    viewModel.import(it)
                }
            }
        }
    }
}

@Composable
private fun ProfileList(
    contentPadding: PaddingValues,
    state: OnlineProfileState,
    import: (OnlineProfile) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {

        items(state.files) {
            ExampleItem(it, import)
        }
    }
}

@Composable
private fun ExampleItem(profile: OnlineProfile, import: (OnlineProfile) -> Unit) {
    val cardBgColor = getColorAttribute(R.attr.appCardBackground)
    val activity = LocalContext.current.requireActivity()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = Color(cardBgColor))
            .clickableWithRipple {
            }
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = profile.profile.name, style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = W700
                )
            )
            SmallSpacer()

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Filled.People,
                        contentDescription = "Author"
                    )
                    TinySpacer()
                    Text(
                        text = profile.author,
                        style = MaterialTheme.typography.labelLarge
                    )
                }


            }

            StandardSpacer()
            Text(
                text = profile.profile.description,
                style = MaterialTheme.typography.labelMedium
            )

            StandardSpacer()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                OutlinedButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = {
                    }) {
                    Icon(
                        imageVector = Icons.Filled.Download,
                        contentDescription = "Import",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = stringResource(id = R.string.common_menu_title_import))
                }
            }
        }
    }
}