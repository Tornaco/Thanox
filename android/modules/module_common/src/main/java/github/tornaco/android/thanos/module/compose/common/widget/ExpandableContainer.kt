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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import github.tornaco.android.thanos.module.compose.common.theme.ColorDefaults

enum class ExpandableState { Expand, Collapsed }

@Composable
fun ExpandableContainer(
    modifier: Modifier = Modifier,
    expandState: MutableState<ExpandableState>,
    mainContent: @Composable () -> Unit,
    expandContent: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = ColorDefaults.backgroundSurfaceColor()
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickableWithRipple {
                    expandState.value =
                        if (expandState.value == ExpandableState.Expand) {
                            ExpandableState.Collapsed
                        } else {
                            ExpandableState.Expand
                        }
                },
            verticalArrangement = Arrangement.Center
        ) {
            mainContent()

            AnimatedVisibility(visible = expandState.value == ExpandableState.Expand) {
                expandContent()
            }
        }
    }

}