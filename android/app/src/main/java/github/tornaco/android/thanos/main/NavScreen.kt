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

import android.graphics.drawable.LayerDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.platform.LocalLifecycleOwner
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
import coil.compose.AsyncImage
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.app.donate.DonateActivity
import github.tornaco.android.thanos.module.compose.common.requireActivity
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.theme.cardCornerSize
import github.tornaco.android.thanos.module.compose.common.theme.getColorAttribute
import github.tornaco.android.thanos.module.compose.common.widget.*
import github.tornaco.android.thanos.settings.SettingsDashboardActivity
import kotlinx.coroutines.delay
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
        viewModel.loadPurchaseStatus()
        viewModel.loadFeatures()
        viewModel.loadAppStatus()
        viewModel.loadHeaderStatus()
        viewModel.autoRefresh()
    }

    ThanoxBottomSheetScaffold(title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(id = R.string.app_name),
                style = TypographyDefaults.appBarTitleTextStyle())
            TinySpacer()
            AppBarBadges(state = state, onInactiveClick = {
                isShowActiveDialog = true
            }, onNeedRestartClick = {
                NeedToRestartActivity.Starter.start(activity)
            }, onTryingAppClick = {
                DonateActivity.start(activity)
            }, onFrameworkErrorClick = {
                isShowFrameworkErrorDialog = true
            })
        }
    }, actions = {
        Row(modifier = Modifier, verticalAlignment = CenterVertically) {
            IconButton(onClick = {
                isShowRebootConfirmationDialog = true
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_remix_restart_line),
                    contentDescription = "Reboot")
            }
            IconButton(onClick = {
                SettingsDashboardActivity.start(activity)
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_remix_settings_line),
                    contentDescription = "Settings")
            }
        }
    }, onBackPressed = null) { contentPadding ->
        SwipeRefresh(state = rememberSwipeRefreshState(state.isLoading),
            onRefresh = { viewModel.refresh() },
            indicatorPadding = contentPadding,
            clipIndicatorToPadding = false,
            indicator = { state, refreshTriggerDistance ->
                SwipeRefreshIndicator(state = state,
                    refreshTriggerDistance = refreshTriggerDistance,
                    scale = true,
                    arrowEnabled = false,
                    contentColor = MaterialTheme.colorScheme.primary)
            }) {
            NavContent(
                contentPadding = contentPadding,
                state = state,
                onFeatureItemClick = {
                    viewModel.featureItemClick(activity, it.id)
                },
                onHeaderClick = {
                    viewModel.headerClick(activity)
                },
                createShortcut = {
                    PrebuiltFeatureShortcutActivity.ShortcutHelper.addShortcut(activity, it)
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
        ClickableBadge(text = stringResource(id = R.string.status_not_active)) {
            onInactiveClick()
        }
    }
    if (state.activeStatus == ActiveStatus.RebootNeeded) {
        ClickableBadge(text = stringResource(id = R.string.status_need_reboot)) {
            onNeedRestartClick()
        }
    }
    if (state.hasFrameworkError) {
        ClickableBadge(text = stringResource(id = R.string.badge_framework_err)) {
            onFrameworkErrorClick()
        }
    }
    if (!state.isPurchased) {
        ClickableBadge(text = stringResource(id = R.string.badge_trying_app)) {
            onTryingAppClick()
        }
    }
}

@Composable
private fun ClickableBadge(text: String, onClick: () -> Unit) {
    TextButton(contentPadding = PaddingValues(4.dp), onClick = { onClick() }) {
        MD3Badge(text = text,
            containerColor = MaterialTheme.colorScheme.errorContainer,
            textSize = 12.sp,
            padding = 6.dp)
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
    val windowBgColor = getColorAttribute(android.R.attr.windowBackground)
    Column(modifier = Modifier
        .padding(contentPadding)
        .navigationBarsPadding()
        .fillMaxSize()
        .background(color = Color(windowBgColor))
        .verticalScroll(rememberScrollState())) {
        NavHeaderContent(modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
            headerInfo = state.statusHeaderInfo) {
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
    val cardBgColor = getColorAttribute(R.attr.appCardBackground)
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .padding(top = 16.dp),
        shape = RoundedCornerShape(cardCornerSize),
        colors = CardDefaults.cardColors(containerColor = Color(cardBgColor))) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(id = group.titleRes),
                fontSize = 14.sp,
                fontWeight = FontWeight.W600)
            StandardSpacer()

            FlowLayout(lineSpacing = 16.dp) {
                group.items.forEach { item ->
                    Box {
                        var isMenuOpen by remember(item) { mutableStateOf(false) }
                        FeatureItem(item = item, onItemClick = onItemClick, onItemLongClick = {
                            isMenuOpen = true
                        })
                        DropdownMenu(
                            modifier = Modifier.fillMaxWidth(.68f),
                            expanded = isMenuOpen,
                            onDismissRequest = {
                                isMenuOpen = false
                            },
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(stringResource(id = R.string.menu_title_create_shortcut))
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
    Column(modifier = Modifier
        .width(64.dp)
        .clickableWithRippleBorderless(
            onLongClick = {
                onItemLongClick(item)
            }, onClick = {
                onItemClick(item)
            }),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {
        val density = LocalDensity.current
        val iconSize: Float = remember {
            with(density) {
                52.dp.toPx()
            }
        }
        val color = MaterialTheme.colorScheme.surface
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
        AsyncImage(model = item.iconRes,
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
        MediumSpacer()
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
        )
    }
}

@Composable
private fun RestartDeviceConfirmationDialog(
    onRebootConfirmed: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    ThanoxAlertDialog(title = {
        Text(text = stringResource(id = R.string.reboot_now))
    }, text = {
        Text(text = stringResource(id = R.string.common_dialog_message_are_you_sure))
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
private fun ActiveDialog(
    onDismissRequest: () -> Unit,
) {
    ThanoxAlertDialog(title = {
        Text(text = stringResource(id = R.string.status_not_active))
    }, text = {
        Text(text = stringResource(id = R.string.message_active_needed))
    }, onDismissRequest = onDismissRequest, confirmButton = {
        TextButton(onClick = {
            onDismissRequest()
        }) {
            Text(text = stringResource(android.R.string.ok))
        }
    })
}


@Composable
private fun FrameworkErrorDialog(
    onDismissRequest: () -> Unit,
) {
    ThanoxAlertDialog(title = {
        Text(text = stringResource(id = R.string.title_framework_error))
    }, text = {
        Text(text = stringResource(id = R.string.message_framework_error))
    }, onDismissRequest = onDismissRequest, confirmButton = {
        TextButton(onClick = {
            onDismissRequest()
        }) {
            Text(text = stringResource(android.R.string.ok))
        }
    })
}

@Composable
private fun PrivacyStatementDialog(
    onDismissRequest: () -> Unit,
) {
    val context = LocalContext.current
    val privacyAgreement = remember {
        AssetUtils.readFileToStringOrThrow(context, "privacy/privacy_agreement.html")
    }
    ThanoxAlertDialog(title = {
        Text(text = stringResource(id = R.string.privacy_agreement_title))
    }, text = {
        Column(modifier = Modifier
            .fillMaxHeight(fraction = 0.64f)
            .verticalScroll(rememberScrollState())) {
            Text(text = HtmlCompat.fromHtml(privacyAgreement, HtmlCompat.FROM_HTML_MODE_COMPACT)
                .toAnnotatedString())
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
private fun FirstRunDialog(
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
            Text(text = stringResource(id = R.string.title_app_notice))
        },
        text = {
            Text(text = stringResource(id = R.string.message_app_notice))
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
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false))
}


@Composable
private fun MultiplePatchDialog(
    patchSources: List<String>,
    onDismissRequest: () -> Unit,
) {
    ThanoxAlertDialog(title = {
        Text(text = stringResource(id = R.string.title_multiple_patch_applied_error))
    }, text = {
        Text(text = stringResource(id = R.string.message_multiple_patch_applied_error,
            patchSources.joinToString(" ")))
    }, onDismissRequest = onDismissRequest, confirmButton = {
        TextButton(onClick = {
            onDismissRequest()
        }) {
            Text(text = stringResource(android.R.string.ok))
        }
    })
}

