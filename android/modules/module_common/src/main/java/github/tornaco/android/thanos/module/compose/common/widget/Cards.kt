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

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import github.tornaco.android.thanos.module.compose.common.theme.ColorDefaults

@Composable
fun CardContainer(content: @Composable () -> Unit) {
    androidx.compose.material.Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
        backgroundColor = ColorDefaults.backgroundSurfaceColor(),
        shape = RoundedCornerShape(12.dp),
        elevation = 0.dp
    ) {
        content()
    }
}