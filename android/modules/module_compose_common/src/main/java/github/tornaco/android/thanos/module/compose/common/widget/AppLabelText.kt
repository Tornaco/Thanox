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

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import github.tornaco.android.thanos.module.compose.common.widget.AutoResizeText
import github.tornaco.android.thanos.module.compose.common.widget.FontSizeRange

@Composable
fun AppLabelText(modifier: Modifier = Modifier, appLabel: String) {
    AutoResizeText(
        modifier = modifier,
        text = appLabel,
        maxLines = 2,
        fontSizeRange = FontSizeRange(
            min = 12.sp,
            max = 16.sp,
        ),
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.titleMedium,
    )
}