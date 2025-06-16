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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import github.tornaco.android.thanos.module.compose.common.infra.sort.AppSortTools
import github.tornaco.android.thanos.res.R

@Composable
fun SortToolDropdown(
    selectedItem: AppSortTools,
    allItems: List<AppSortTools>,
    isReverse: Boolean,
    setReverse: (Boolean) -> Unit,
    onItemSelected: (AppSortTools) -> Unit = {},
) {
    if (allItems.isNotEmpty()) {
        Box(
            modifier = Modifier.wrapContentSize(Alignment.TopStart)
        ) {
            var expanded by remember { mutableStateOf(false) }

            DropdownButtonLayout(
                text = stringResource(selectedItem.labelRes),
                leadingIcon = Icons.Filled.Sort,
                isMenuOpen = expanded,
                open = {
                    expanded = true
                },
            )

            MaterialTheme(
                shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(18.dp))
            ) {
                androidx.compose.material3.DropdownMenu(
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(onClick = {
                        setReverse(!isReverse)
                        expanded = false
                    }, text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(stringResource(R.string.common_sort_reverse))
                            Checkbox(isReverse, onCheckedChange = {
                                setReverse(it)
                            })
                        }
                    }
                    )


                    allItems.forEach { item ->
                        DropdownMenuItem(onClick = {
                            onItemSelected(item)
                            expanded = false
                        }, text = { Text(stringResource(item.labelRes)) })
                    }
                }
            }

        }
    }
}
