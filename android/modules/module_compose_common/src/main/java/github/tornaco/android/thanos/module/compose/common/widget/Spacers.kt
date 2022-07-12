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

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SmallSpacer() {
    Spacer(modifier = Modifier.size(2.dp))
}

@Composable
fun TinySpacer() {
    Spacer(modifier = Modifier.size(4.dp))
}

@Composable
fun MediumSpacer() {
    Spacer(modifier = Modifier.size(8.dp))
}

@Composable
fun StandardSpacer() {
    Spacer(modifier = Modifier.size(16.dp))
}

@Composable
fun LargeSpacer() {
    Spacer(modifier = Modifier.size(20.dp))
}