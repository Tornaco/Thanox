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

@file:OptIn(ExperimentalMaterial3Api::class)

package github.tornaco.android.thanos.module.compose.common.widget

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MD3Badge(text: String, modifier: Modifier = Modifier) {
    MD3Badge(
        text = text,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        textSize = 10.sp
    )
}

@Composable
fun MD3Badge(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    textSize: TextUnit = 10.sp,
    padding: Dp = 2.dp
) {
    androidx.compose.material3.Badge(
        modifier = modifier,
        containerColor = containerColor
    ) {
        Text(
            modifier = Modifier.padding(padding),
            text = text,
            fontSize = textSize
        )
    }
}

@Composable
fun OutlineBadge(modifier: Modifier = Modifier, text: String) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = ThanoxCardRoundedCornerShape
            )
            .padding(1.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = text,
            fontSize = 9.sp,
            fontFamily = fontFamilyProductSans(),
            color = MaterialTheme.colorScheme.primary
        )
    }
}
