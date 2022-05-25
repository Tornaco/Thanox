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

package github.tornaco.android.thanos.module.compose.common.widget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import github.tornaco.android.thanos.module.compose.common.TinySpacer
import github.tornaco.android.thanos.module.compose.common.clickableWithRipple


data class SingleChoiceItem(val id: String, val icon: ImageVector? = null, val label: String)

class SingleChoiceDialogState(
    val title: String,
    val items: List<SingleChoiceItem>,
    val onItemClick: (String) -> Unit
) {
    private var _isShowing: Boolean by mutableStateOf(false)
    val isShowing get() = _isShowing

    fun show() {
        _isShowing = true
    }

    fun dismiss() {
        _isShowing = false
    }
}

@Composable
fun rememberSingleChoiceDialogState(
    title: String,
    items: List<SingleChoiceItem>,
    onItemClick: (String) -> Unit
): SingleChoiceDialogState {
    return remember {
        SingleChoiceDialogState(title, items, onItemClick)
    }
}

@Composable
fun SingleChoiceDialog(state: SingleChoiceDialogState) {
    if (state.isShowing) {
        AlertDialog(
            title = {
                Text(text = state.title)
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    state.items.forEach {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickableWithRipple {
                                    state.dismiss()
                                    state.onItemClick(it.id)
                                }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            it.icon?.let { icon ->
                                Icon(
                                    imageVector = icon,
                                    contentDescription = it.label
                                )
                                TinySpacer()
                            }
                            Text(text = it.label)
                        }
                    }
                }
            },
            confirmButton = {},
            onDismissRequest = {
                state.dismiss()
            },
            properties = DialogProperties()
        )
    }
}