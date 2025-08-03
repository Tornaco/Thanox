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

@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package github.tornaco.android.thanos.module.compose.common.widget

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ThanoxAlertDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = null,
    text: @Composable (() -> Unit)? = null,
    shape: Shape = AlertDialogDefaults.shape,
    containerColor: Color = AlertDialogDefaults.containerColor,
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
    iconContentColor: Color = AlertDialogDefaults.iconContentColor,
    titleContentColor: Color = AlertDialogDefaults.titleContentColor,
    textContentColor: Color = AlertDialogDefaults.textContentColor,
    properties: DialogProperties = DialogProperties(),
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        modifier = Modifier.fillMaxWidth(fraction = 0.82f),
        dismissButton = dismissButton,
        icon = icon,
        title = title,
        text = text,
        shape = shape,
        containerColor = containerColor,
        tonalElevation = tonalElevation,
        iconContentColor = iconContentColor,
        titleContentColor = titleContentColor,
        textContentColor = textContentColor,
        properties = DialogProperties(
            dismissOnBackPress = properties.dismissOnBackPress,
            dismissOnClickOutside = properties.dismissOnClickOutside,
            securePolicy = properties.securePolicy,
            usePlatformDefaultWidth = false
        )
    )
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ThanoxDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    title: @Composable () -> Unit = {},
    buttons: @Composable RowScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = properties.dismissOnBackPress,
            dismissOnClickOutside = properties.dismissOnClickOutside,
            securePolicy = properties.securePolicy,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(fraction = 0.82f),
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                title()
                Spacer(modifier = Modifier.size(16.dp))
                content()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    buttons()
                }
            }

        }
    }
}

@Composable
fun ShortFullScreenXDialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(
                topStart = 28.dp,
                topEnd = 28.dp,
                bottomEnd = 0.dp,
                bottomStart = 0.dp
            ),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp
        ) {
            BackHandler {
                onDismissRequest()
            }
            content()
        }
    }
}

@Composable
fun ShortXAlertDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = null,
    text: @Composable (() -> Unit)? = null,
    shape: Shape = AlertDialogDefaults.shape,
    containerColor: Color = AlertDialogDefaults.containerColor,
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
    iconContentColor: Color = AlertDialogDefaults.iconContentColor,
    titleContentColor: Color = AlertDialogDefaults.titleContentColor,
    textContentColor: Color = AlertDialogDefaults.textContentColor,
    properties: DialogProperties = DialogProperties(),
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        modifier = Modifier.fillMaxWidth(fraction = 0.82f),
        dismissButton = dismissButton,
        icon = icon,
        title = title,
        text = text,
        shape = shape,
        containerColor = containerColor,
        tonalElevation = tonalElevation,
        iconContentColor = iconContentColor,
        titleContentColor = titleContentColor,
        textContentColor = textContentColor,
        properties = DialogProperties(
            dismissOnBackPress = properties.dismissOnBackPress,
            dismissOnClickOutside = properties.dismissOnClickOutside,
            securePolicy = properties.securePolicy,
            usePlatformDefaultWidth = false
        )
    )
}

@Composable
fun ShortXDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    title: @Composable () -> Unit = {},
    buttons: @Composable RowScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = properties.dismissOnBackPress,
            dismissOnClickOutside = properties.dismissOnClickOutside,
            securePolicy = properties.securePolicy,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(fraction = 0.82f),
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                title()
                Spacer(modifier = Modifier.size(16.dp))
                content()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    buttons()
                }
            }

        }
    }
}

class ConfirmDialogState : CommonDialogState()

@Composable
fun rememberConfirmDialogState(): ConfirmDialogState {
    return remember {
        ConfirmDialogState()
    }
}

@Composable
fun <T> ConfirmDialog(
    title: String,
    state: ConfirmDialogState,
    data: T,
    messageHint: (T) -> String = { "" },
    confirmButton: String = stringResource(id = android.R.string.ok),
    dismissButton: String = stringResource(id = android.R.string.cancel),
    onConfirm: (T) -> Unit
) {
    if (state.isShowing) {
        ShortXAlertDialog(
            onDismissRequest = {
                state.dismiss()
            },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm(data)
                    state.dismiss()
                }) {
                    Text(text = confirmButton)
                }
            },
            dismissButton = {
                TextButton(onClick = { state.dismiss() }) {
                    Text(text = dismissButton)
                }
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(text = messageHint(data))
            })
    }
}

open class CommonDialogState {
    private var _isShow by mutableStateOf(false)
    val isShowing get() = _isShow

    open fun show() {
        _isShow = true
    }

    open fun dismiss() {
        _isShow = false
    }
}

@Composable
fun rememberCommonDialogState(): CommonDialogState {
    return remember {
        CommonDialogState()
    }
}

@Composable
fun RebootConfirmDialog(
    state: CommonDialogState,
) {
    if (state.isShowing) {
        ShortXAlertDialog(
            onDismissRequest = {
                state.dismiss()
            },
            confirmButton = {
                TextButton(onClick = {
                    state.dismiss()
                }) {
                    Text(text = stringResource(id = android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { state.dismiss() }) {
                    Text(text = stringResource(id = android.R.string.cancel))
                }
            },
            title = {
                Text(text = "Reboot")
            },
            text = {
                Text(text = "Reboot your device?")
            })
    }
}


class TipDialogState(
    val title: String,
    val tip: String,
    val textToCopy: String?
) : CommonDialogState()

@Composable
fun rememberTipDialogState(
    title: String,
    tip: String,
    textToCopy: String? = null
): TipDialogState {
    return remember {
        TipDialogState(title, tip, textToCopy)
    }
}

@Composable
fun TipDialog(state: TipDialogState) {
    if (state.isShowing) {
        val context = LocalContext.current
        ShortXAlertDialog(
            onDismissRequest = {
                state.dismiss()
            },
            confirmButton = {
                TextButton(onClick = {
                    state.dismiss()
                    state.textToCopy?.let {
                    }
                }) {
                    Text(
                        text = if (state.textToCopy == null) {
                            stringResource(id = android.R.string.ok)
                        } else {
                            stringResource(id = android.R.string.copy)
                        }
                    )
                }
            },
            title = {
                Text(text = state.title)
            },
            text = {
                val textColor = LocalContentColor.current.takeOrElse {
                    MaterialTheme.colorScheme.onPrimaryContainer
                }
                Text(text = state.tip, color = textColor)
            })
    }
}


class ProgressDialogState(
    val title: String,
    val message: String,
) : CommonDialogState()

@Composable
fun rememberProgressDialogState(
    title: String,
    message: String,
): ProgressDialogState {
    return remember {
        ProgressDialogState(title, message)
    }
}

@Composable
fun ProgressDialog(state: ProgressDialogState) {
    if (state.isShowing) {
        ShortXDialog(
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            ),
            onDismissRequest = {
                state.dismiss()
            },
            title = {
                DialogTitle(text = state.title)
            }) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                ContainedLoadingIndicator()
                MediumSpacer()
                DialogMessage(text = state.message)
            }
        }
    }
}


@Composable
fun ImageDialog(state: CommonDialogState, resId: Int) {
    if (state.isShowing) {
        ShortXDialog(
            onDismissRequest = {
                state.dismiss()
            },
            title = {}
        ) {
            Column {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(id = resId),
                    contentDescription = ""
                )
            }
        }
    }
}


@Composable
fun CommonDialog(state: CommonDialogState, content: @Composable () -> Unit) {
    if (state.isShowing) {
        ShortXDialog(
            onDismissRequest = {
                state.dismiss()
            },
            title = {}
        ) {
            content()
        }
    }
}


