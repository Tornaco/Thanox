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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import github.tornaco.android.thanos.module.compose.common.widget.md3.TopAppBarScrollBehaviorX
import github.tornaco.android.thanos.module.compose.common.widget.md3.XTopAppBarDefaults

interface FilterItem {
    val label: String

    val isSelected: Boolean
}

@Composable
fun <T : FilterItem> FilterDropDown(
    scrollBehavior: TopAppBarScrollBehaviorX,
    allItems: List<T>,
    onItemSelected: (T) -> Unit = {},
) {
    val backgroundColors = XTopAppBarDefaults.largeTopAppBarColors()
    val backgroundColor = backgroundColors.containerColor(
        scrollFraction = scrollBehavior.scrollFraction
    ).value
    var expanded by remember { mutableStateOf(false) }
    val selectedItem = allItems.find { it.isSelected }
    requireNotNull(selectedItem) { "At least 1 selected item required." }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(16.dp)
            .wrapContentSize(Alignment.TopStart)
    ) {
        FilledTonalButton(onClick = { expanded = true }) {
            Text(text = selectedItem.label)
        }
        DropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            allItems.forEach { item ->
                DropdownMenuItem(onClick = {
                    onItemSelected(item)
                    expanded = false
                }) {
                    Text(text = item.label)
                }
            }
        }
    }
}
