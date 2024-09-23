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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import github.tornaco.android.thanos.module.common.R
import github.tornaco.android.thanos.module.compose.common.theme.getColorAttribute

@Composable
fun SwitchBar(modifier: Modifier = Modifier, isChecked: Boolean, onCheckChange: (Boolean) -> Unit) {
    val bgColor = Color(getColorAttribute(attr = R.attr.switchBarBackground))
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp)
            .clip(
                RoundedCornerShape(32.dp)
            )
            .clickableWithRipple {
                onCheckChange(!isChecked)
            }
            .background(color = bgColor)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterStart),
            text = if (isChecked) stringResource(id = github.tornaco.android.thanos.res.R.string.switch_on_text) else stringResource(
                id = github.tornaco.android.thanos.res.R.string.switch_off_text
            )
        )
        Switch(
            modifier = Modifier
                .padding(end = 16.dp)
                .align(Alignment.CenterEnd),
            checked = isChecked,
            onCheckedChange = {
                onCheckChange(it)
            })
    }
}