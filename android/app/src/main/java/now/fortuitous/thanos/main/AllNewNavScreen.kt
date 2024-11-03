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


@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class, ExperimentalLayoutApi::class
)

package now.fortuitous.thanos.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import github.tornaco.android.thanos.main.launchSubscribeActivity
import github.tornaco.android.thanos.module.compose.common.requireActivity
import github.tornaco.android.thanos.module.compose.common.theme.cardCornerSize
import github.tornaco.android.thanos.module.compose.common.widget.AutoResizeText
import github.tornaco.android.thanos.module.compose.common.widget.FontSizeRange
import github.tornaco.android.thanos.module.compose.common.widget.LargeSpacer
import github.tornaco.android.thanos.module.compose.common.widget.MD3Badge
import github.tornaco.android.thanos.module.compose.common.widget.TinySpacer
import github.tornaco.android.thanos.support.FlowLayout

@Composable
fun AllNewNavScreen() {
    val viewModel = hiltViewModel<NavViewModel2>(LocalContext.current.requireActivity())
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    viewModel.bindLifecycle(lifecycle)
    val state by viewModel.state.collectAsState()
    val activity = LocalContext.current.requireActivity()

    var isShowRebootConfirmationDialog by remember {
        mutableStateOf(false)
    }
    var isShowActiveDialog by remember {
        mutableStateOf(false)
    }
    var isShowFrameworkErrorDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(viewModel) {
        viewModel.loadCoreStatus()
        viewModel.loadPurchaseStatus()
        viewModel.loadFeatures()
        viewModel.loadAppStatus()
        viewModel.loadHeaderStatus()
        viewModel.autoRefresh()
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(),
                title = {
                    Row(verticalAlignment = CenterVertically) {
                        Text(
                            text = stringResource(id = github.tornaco.android.thanos.res.R.string.app_name_thanox),
                            fontWeight = FontWeight.W500
                        )
                        TinySpacer()
                        AppBarBadges(state = state, onInactiveClick = {
                            isShowActiveDialog = true
                        }, onNeedRestartClick = {
                            NeedToRestartActivity.Starter.start(activity)
                        }, onTryingAppClick = {
                            launchSubscribeActivity(activity) {}
                        }, onFrameworkErrorClick = {
                            isShowFrameworkErrorDialog = true
                        })
                    }
                },
                actions = {
                    Row(modifier = Modifier, verticalAlignment = CenterVertically) {
                        IconButton(onClick = {
                            isShowRebootConfirmationDialog = true
                        }) {
                            Icon(
                                painter = painterResource(id = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_restart_fill),
                                contentDescription = "Reboot"
                            )
                        }
                        IconButton(onClick = {
                            now.fortuitous.thanos.settings.SettingsDashboardActivity.start(activity)
                        }) {
                            Icon(
                                painter = painterResource(id = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_settings_4_fill),
                                contentDescription = "Settings"
                            )
                        }
                    }
                })
        }
    ) { contentPadding ->
        val pullRefreshState =
            rememberPullRefreshState(state.isLoading, {
                viewModel.refresh()
            })
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .pullRefresh(pullRefreshState),
        ) {
            NavContent(
                state = state,
                onFeatureItemClick = {
                    viewModel.featureItemClick(activity, it.id)
                },
                onHeaderClick = {
                    viewModel.headerClick(activity)
                },
                createShortcut = {
                    PrebuiltFeatureShortcutActivity.ShortcutHelper.addShortcut(
                        activity,
                        it
                    )
                })

            if (isShowRebootConfirmationDialog) {
                RestartDeviceConfirmationDialog(onRebootConfirmed = {
                    isShowRebootConfirmationDialog = false
                    viewModel.rebootDevice()
                }, onDismissRequest = {
                    isShowRebootConfirmationDialog = false
                })
            }
            if (isShowActiveDialog) {
                ActiveDialog(onDismissRequest = {
                    isShowActiveDialog = false
                })
            }
            if (isShowFrameworkErrorDialog) {
                FrameworkErrorDialog(onDismissRequest = {
                    isShowFrameworkErrorDialog = false
                })
            }
            if (state.showPrivacyStatement) {
                PrivacyStatementDialog(onDismissRequest = {
                    viewModel.privacyStatementAccepted()
                })
            }
            if (state.showFirstRunTips) {
                FirstRunDialog(
                    onAccept = {
                        viewModel.firstRunTipNoticed()
                    }, onDeny = {
                        activity.finish()
                    })
            }
            if (state.patchSources.size > 1) {
                MultiplePatchDialog(patchSources = state.patchSources, onDismissRequest = {
                    activity.finish()
                })
            }

            if (state.isAndroidVersionTooLow) {
                AndroidVersionNotSupportedDialog {
                    viewModel.androidVersionTooLowAccepted()
                }
            }

            PullRefreshIndicator(
                state.isLoading,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun AppBarBadges(
    state: NavState,
    onInactiveClick: () -> Unit,
    onNeedRestartClick: () -> Unit,
    onTryingAppClick: () -> Unit,
    onFrameworkErrorClick: () -> Unit,
) {
    if (state.activeStatus == ActiveStatus.InActive) {
        ClickableBadge(text = stringResource(id = github.tornaco.android.thanos.res.R.string.status_not_active)) {
            onInactiveClick()
        }
    }
    if (state.activeStatus == ActiveStatus.RebootNeeded) {
        ClickableBadge(text = stringResource(id = github.tornaco.android.thanos.res.R.string.status_need_reboot)) {
            onNeedRestartClick()
        }
    }
    if (state.hasFrameworkError) {
        ClickableBadge(text = stringResource(id = github.tornaco.android.thanos.res.R.string.badge_framework_err)) {
            onFrameworkErrorClick()
        }
    }
    if (!state.isPurchased) {
        ClickableBadge(text = stringResource(id = github.tornaco.android.thanos.res.R.string.badge_trying_app)) {
            onTryingAppClick()
        }
    }
}

@Composable
private fun ClickableBadge(text: String, onClick: () -> Unit) {
    TextButton(contentPadding = PaddingValues(4.dp), onClick = { onClick() }) {
        MD3Badge(
            text = text,
            containerColor = MaterialTheme.colorScheme.errorContainer,
            textSize = 12.sp,
            padding = 6.dp
        )
    }
}

@Composable
private fun NavContent(
    state: NavState,
    onHeaderClick: () -> Unit,
    onFeatureItemClick: (FeatureItem) -> Unit,
    createShortcut: (FeatureItem) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AllNewNavHeaderContent(
            headerInfo = state.statusHeaderInfo
        ) {
            onHeaderClick()
        }

        Features(state = state, onItemClick = onFeatureItemClick, createShortcut = createShortcut)

        LargeSpacer()
        LargeSpacer()
    }
}

@Composable
private fun Features(
    state: NavState,
    onItemClick: (FeatureItem) -> Unit,
    createShortcut: (FeatureItem) -> Unit,
) {
    state.features.forEach { group ->
        FeatureGroup(group, onItemClick, createShortcut)
    }
}

@Composable
private fun FeatureGroup(
    group: FeatureItemGroup,
    onItemClick: (FeatureItem) -> Unit,
    createShortcut: (FeatureItem) -> Unit,
) {

    val activity = LocalContext.current.requireActivity()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
    ) {
        Text(
            text = stringResource(id = group.titleRes),
            fontSize = 14.sp,
            fontWeight = FontWeight.W500,
            color = themedTextColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier.padding(start = 16.dp)
        )

        FlowLayout(
            lineSpacing = 16.dp,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp)
        ) {
            group.items.forEach { item ->
                Box {
                    var isMenuOpen by remember(item) { mutableStateOf(false) }
                    val menuItems = remember(item) { item.menuItems }
                    FeatureItem(item = item, onItemClick = onItemClick, onItemLongClick = {
                        isMenuOpen = true
                    })
                    MaterialTheme(
                        shapes = MaterialTheme.shapes.copy(
                            extraSmall = RoundedCornerShape(
                                cardCornerSize
                            )
                        )
                    ) {
                        DropdownMenu(
                            modifier = Modifier.fillMaxWidth(.68f),
                            expanded = isMenuOpen,
                            onDismissRequest = {
                                isMenuOpen = false
                            },
                        ) {
                            menuItems.forEach {
                                DropdownMenuItem(
                                    text = {
                                        Text(stringResource(id = it.first))
                                    },
                                    onClick = {
                                        isMenuOpen = false
                                        it.second(activity)
                                    }
                                )
                            }

                            DropdownMenuItem(
                                text = {
                                    Text(stringResource(id = github.tornaco.android.thanos.res.R.string.menu_title_create_shortcut))
                                },
                                onClick = {
                                    isMenuOpen = false
                                    createShortcut(item)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureItem(
    item: FeatureItem,
    onItemClick: (FeatureItem) -> Unit,
    onItemLongClick: (FeatureItem) -> Unit,
) {
    val context = LocalContext.current
    val itemColor = Color(ContextCompat.getColor(context, item.themeColor))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.width(64.dp)
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(itemColor.copy(alpha = 0.12f))
                .combinedClickable(
                    onLongClick = {
                        onItemLongClick(item)
                    }, onClick = {
                        onItemClick(item)
                    })
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            ColoredIcon(
                modifier = Modifier,
                size = 28.dp,
                padding = 0.dp,
                color = itemColor,
            ) {
                Icon(
                    modifier = Modifier.size(14.dp),
                    painter = painterResource(item.iconRes),
                    tint = Color.White,
                    contentDescription = null
                )
            }
        }
        TinySpacer()
        AutoResizeText(
            modifier = Modifier.wrapContentSize(),
            text = stringResource(id = item.titleRes),
            textAlign = TextAlign.Center,
            maxLines = 3,
            fontSizeRange = FontSizeRange(
                min = 8.5.sp,
                max = 12.5.sp,
            ),
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium.copy(
                color = themedTextColor(itemColor),
                lineHeight = 13.5.sp
            ),
        )

    }
}