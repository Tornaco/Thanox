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

package github.tornaco.android.thanos.module.compose.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

data class MenuItem(val id: String, val title: String, @DrawableRes val iconRes: Int = 0)

@Composable
fun DropdownPopUpMenu(
    popMenuExpend: MutableState<Boolean>,
    items: List<MenuItem>,
    onItemClick: (MenuItem) -> Unit
) {
    androidx.compose.material.DropdownMenu(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        expanded = popMenuExpend.value,
        onDismissRequest = { popMenuExpend.value = false }
    ) {
        items.forEach {
            DropdownMenuItem(onClick = {
                popMenuExpend.value = false
                onItemClick(it)
            }, text = {
                Row(verticalAlignment = CenterVertically) {
                    Icon(painterResource(id = it.iconRes), it.title)
                    SmallSpacer()
                    Text(text = it.title)
                }
            })
        }
    }
}