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


@file:OptIn(ExperimentalMaterial3Api::class)

package now.fortuitous.thanos.main

import android.graphics.drawable.LayerDrawable
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.main.launchSubscribeActivity
import github.tornaco.android.thanos.module.compose.common.requireActivity
import github.tornaco.android.thanos.module.compose.common.theme.LocalThanoxColorSchema
import github.tornaco.android.thanos.module.compose.common.theme.cardCornerSize
import github.tornaco.android.thanos.module.compose.common.widget.AnimatedTextContainer
import github.tornaco.android.thanos.module.compose.common.widget.AutoResizeText
import github.tornaco.android.thanos.module.compose.common.widget.FontSizeRange
import github.tornaco.android.thanos.module.compose.common.widget.LargeSpacer
import github.tornaco.android.thanos.module.compose.common.widget.MD3Badge
import github.tornaco.android.thanos.module.compose.common.widget.Md3ExpPullRefreshIndicator
import github.tornaco.android.thanos.module.compose.common.widget.StandardSpacer
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxAlertDialog
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxMediumAppBarScaffold
import github.tornaco.android.thanos.module.compose.common.widget.TinySpacer
import github.tornaco.android.thanos.module.compose.common.widget.rememberThanoxBottomSheetState
import github.tornaco.android.thanos.module.compose.common.widget.toAnnotatedString
import github.tornaco.android.thanos.support.FeatureAccessDialog
import github.tornaco.android.thanos.support.FeatureGrid
import github.tornaco.android.thanos.support.clickableWithRippleBorderless
import github.tornaco.android.thanos.support.subscribe.LVLStateHolder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import now.fortuitous.thanos.settings.v2.SettingsScreen
import org.orbitmvi.orbit.compose.collectAsState
import util.AssetUtils

@Composable
@OptIn(androidx.compose.material.ExperimentalMaterialApi::class)
fun NavScreen() {
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
        viewModel.loadFeatures()
        viewModel.loadAppStatus()
        viewModel.loadHeaderStatus()
        viewModel.autoRefresh()
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val subState by LVLStateHolder.collectAsState()

    DismissibleNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DismissibleDrawerSheet(
                drawerTonalElevation = 0.5.dp
            ) {
                StandardSpacer()
                SettingsScreen()
                BackHandler(enabled = drawerState.isOpen) {
                    scope.launch { drawerState.close() }
                }
            }
        },
        content = {
            ThanoxMediumAppBarScaffold(
                title = {
                    Row(verticalAlignment = CenterVertically) {
                        Text(stringResource(id = R.string.app_name_thanox))
                        TinySpacer()
                        AppBarBadges(
                            state = state,
                            subState = subState,
                            onInactiveClick = {
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
                navigationIcon = {
                    SettingsAppBarActions(drawerState, false)
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
                    }
                }
            ) { contentPadding ->
                val subscribeDialogState = rememberThanoxBottomSheetState()
                FeatureAccessDialog(subscribeDialogState)

                val pullRefreshState =
                    rememberPullRefreshState(state.isLoading, { viewModel.refresh() })
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                        .pullRefresh(pullRefreshState),
                ) {
                    NavContent(
                        contentPadding = PaddingValues(0.dp),
                        state = state,
                        onFeatureItemClick = {
                            PrebuiltFeatureLauncher(activity) {}.launch(it.id)
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

                    Md3ExpPullRefreshIndicator(
                        state.isLoading,
                        pullRefreshState,
                        Modifier.align(Alignment.TopCenter)
                    )
                }
            }
        }
    )
}

@Composable
fun SettingsAppBarActions(localDrawerState: DrawerState, hasUnReadMsg: Boolean) {
    val scope = rememberCoroutineScope()
    IconButton(onClick = {
        scope.launch { localDrawerState.open() }
    }) {
        BadgedBox(badge = {
            if (hasUnReadMsg) {
                Badge(containerColor = Color.Red)
            }
        }) {
            val iconSize by animateDpAsState(
                targetValue = if (hasUnReadMsg) 18.dp else 24.dp,
                animationSpec = tween(durationMillis = 300), label = "Icon size"
            )
            Icon(
                imageVector = Icons.Outlined.Menu,
                modifier = Modifier.size(iconSize),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun AppBarBadges(
    state: NavState,
    subState: LVLStateHolder.State,
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
    if (!subState.isSubscribed) {
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
    contentPadding: PaddingValues,
    state: NavState,
    onHeaderClick: () -> Unit,
    onFeatureItemClick: (FeatureItem) -> Unit,
    createShortcut: (FeatureItem) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(contentPadding)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        NavHeaderContent(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        shape = RoundedCornerShape(cardCornerSize),
        colors = CardDefaults.cardColors(containerColor = LocalThanoxColorSchema.current.cardBgColor)
    ) {
        val activity = LocalContext.current.requireActivity()
        FeatureGrid(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .heightIn(max = 1000.dp),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = stringResource(id = group.titleRes),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W500,
                    color = themedTextColor(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            items(group.items) { item ->
                Box(Modifier.padding(8.dp)) {
                    var isMenuOpen by remember(item) { mutableStateOf(false) }
                    val menuItems = remember(item) { item.menuItems }
                    FeatureItem(item = item, onItemClick = onItemClick, onItemLongClick = {
                        isMenuOpen = true
                    })
                    MaterialTheme(
                        shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(18.dp))
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
    Column(
        modifier = Modifier
            .width(64.dp)
            .clickableWithRippleBorderless(
                onLongClick = {
                    onItemLongClick(item)
                }, onClick = {
                    onItemClick(item)
                }),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        val density = LocalDensity.current
        val iconSize: Float = remember {
            with(density) {
                52.dp.toPx()
            }
        }
        val color = Color.Transparent
        val customPainter = remember {
            object : Painter() {
                override val intrinsicSize: Size
                    get() = Size(iconSize, iconSize)

                override fun DrawScope.onDraw() {
                    drawCircle(color = color)
                }
            }
        }
        val context = LocalContext.current
        AsyncImage(
            model = item.packedIconRes,
            placeholder = customPainter,
            contentDescription = null,
            filterQuality = FilterQuality.High,
            onSuccess = {
                val ld = it.result.drawable as LayerDrawable
                val layer = ld.findDrawableByLayerId(R.id.settings_ic_foreground)
                if (layer != null) {
                    layer.setTint(ContextCompat.getColor(context, item.themeColor))
                    ld.setDrawableByLayerId(R.id.settings_ic_foreground, layer)
                }
            })
        Spacer(modifier = Modifier.size(6.dp))
        AutoResizeText(
            modifier = Modifier,
            text = stringResource(id = item.titleRes),
            textAlign = TextAlign.Center,
            maxLines = 3,
            fontSizeRange = FontSizeRange(
                min = 11.sp,
                max = 12.sp,
            ),
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
            color = themedTextColor(Color(ContextCompat.getColor(context, item.themeColor))),
            lineHeight = 12.5.sp
        )
    }
}

@Composable
fun RestartDeviceConfirmationDialog(
    onRebootConfirmed: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    ThanoxAlertDialog(title = {
        Text(text = stringResource(id = github.tornaco.android.thanos.res.R.string.reboot_now))
    }, text = {
        Text(text = stringResource(id = github.tornaco.android.thanos.res.R.string.common_dialog_message_are_you_sure))
    }, onDismissRequest = onDismissRequest, confirmButton = {
        TextButton(onClick = {
            onRebootConfirmed()
        }) {
            Text(text = stringResource(android.R.string.ok))
        }
    }, dismissButton = {
        TextButton(onClick = { onDismissRequest() }) {
            Text(text = stringResource(android.R.string.cancel))
        }
    })
}


@Composable
fun ActiveDialog(
    onDismissRequest: () -> Unit,
) {
    ThanoxAlertDialog(title = {
        Text(text = stringResource(id = github.tornaco.android.thanos.res.R.string.status_not_active))
    }, text = {
        Text(text = stringResource(id = github.tornaco.android.thanos.res.R.string.message_active_needed))
    }, onDismissRequest = onDismissRequest, confirmButton = {
        TextButton(onClick = {
            onDismissRequest()
        }) {
            Text(text = stringResource(android.R.string.ok))
        }
    })
}


@Composable
fun FrameworkErrorDialog(
    onDismissRequest: () -> Unit,
) {
    ThanoxAlertDialog(title = {
        Text(text = stringResource(id = github.tornaco.android.thanos.res.R.string.title_framework_error))
    }, text = {
        Text(text = stringResource(id = github.tornaco.android.thanos.res.R.string.message_framework_error))
    }, onDismissRequest = onDismissRequest, confirmButton = {
        TextButton(onClick = {
            onDismissRequest()
        }) {
            Text(text = stringResource(android.R.string.ok))
        }
    })
}

@Composable
fun PrivacyStatementDialog(
    onDismissRequest: () -> Unit,
) {
    val context = LocalContext.current
    val fileName =
        context.getString(github.tornaco.android.thanos.res.R.string.privacy_agreement_file)
    val privacyAgreement = remember {
        AssetUtils.readFileToStringOrThrow(context, fileName)
    }
    ThanoxAlertDialog(title = {
        Text(text = stringResource(id = github.tornaco.android.thanos.res.R.string.privacy_agreement_title))
    }, text = {
        Column(
            modifier = Modifier
                .fillMaxHeight(fraction = 0.64f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = HtmlCompat.fromHtml(privacyAgreement, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    .toAnnotatedString()
            )
        }
    }, onDismissRequest = onDismissRequest, confirmButton = {
        TextButton(onClick = {
            onDismissRequest()
        }) {
            Text(text = stringResource(android.R.string.ok))
        }
    })
}


@Composable
fun FirstRunDialog(
    onAccept: () -> Unit,
    onDeny: () -> Unit,
) {
    var remainingTimeSec by remember {
        mutableStateOf(6)
    }
    LaunchedEffect(true) {
        while (remainingTimeSec > 0) {
            delay(1000L)
            remainingTimeSec -= 1
        }
    }
    ThanoxAlertDialog(
        title = {
            Text(text = stringResource(id = github.tornaco.android.thanos.res.R.string.title_app_notice))
        },
        text = {
            Text(text = stringResource(id = github.tornaco.android.thanos.res.R.string.message_app_notice))
        },
        onDismissRequest = onDeny,
        confirmButton = {
            TextButton(
                modifier = Modifier.widthIn(min = 64.dp),
                enabled = remainingTimeSec <= 0,
                onClick = {
                    onAccept()
                }) {
                if (remainingTimeSec > 0) {
                    AnimatedTextContainer(text = "${stringResource(android.R.string.ok)} ${remainingTimeSec}s") {
                        Text(text = it)
                    }
                } else {
                    Text(text = stringResource(android.R.string.ok))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDeny()
            }) {
                Text(text = stringResource(android.R.string.cancel))
            }
        },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    )
}


@Composable
fun MultiplePatchDialog(
    patchSources: List<String>,
    onDismissRequest: () -> Unit,
) {
    ThanoxAlertDialog(title = {
        Text(text = stringResource(id = github.tornaco.android.thanos.res.R.string.title_multiple_patch_applied_error))
    }, text = {
        Text(
            text = stringResource(
                id = github.tornaco.android.thanos.res.R.string.message_multiple_patch_applied_error,
                patchSources.joinToString(" ")
            )
        )
    }, onDismissRequest = onDismissRequest, confirmButton = {
        TextButton(onClick = {
            onDismissRequest()
        }) {
            Text(text = stringResource(android.R.string.ok))
        }
    })
}


@Composable
fun AndroidVersionNotSupportedDialog(
    onDismissRequest: () -> Unit
) {
    ThanoxAlertDialog(title = {
        Text(text = "Not Supported")
    }, text = {
        Text(text = "Android version of you device is too low, some features may not work properly.")
    }, onDismissRequest = onDismissRequest, confirmButton = {
        TextButton(onClick = {
            onDismissRequest()
        }) {
            Text(text = stringResource(android.R.string.ok))
        }
    })
}

