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

package github.tornaco.android.thanos.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.module.compose.common.*
import github.tornaco.android.thanos.module.compose.common.widget.CircularProgressBar

@Composable
fun HeaderContent(state: HeaderState, onHeaderClick: () -> Unit) {
    val headerInfo = state.headerInfo
    val cardBgColor = getColorAttribute(R.attr.appCardBackground)
    val primaryColor = getColorAttribute(R.attr.colorPrimary)
    val primaryContainerColor = getColorAttribute(R.attr.colorPrimaryContainer)
    val onSurfaceColor = getColorAttribute(R.attr.colorOnSurface)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color = Color(cardBgColor))
            .clickableWithRipple {
                onHeaderClick()
            }
            .padding(16.dp)

    ) {
        Column {
            Row {
                FilledTonalButton(
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color(
                            primaryContainerColor
                        )
                    ),
                    onClick = {
                        onHeaderClick()
                    }) {
                    Text(
                        text = "${headerInfo.runningAppsCount}${stringResource(id = R.string.boost_status_running_apps)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(onSurfaceColor)
                    )
                }
            }

            StandardSpacer()

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circle
                CircularProgressBar(
                    modifier = Modifier
                        .size(88.dp)
                        .align(Alignment.CenterVertically),
                    progress = headerInfo.memory.memUsagePercent.toFloat(),
                    progressMax = 100f,
                    progressBarColor = Color(primaryColor),
                    progressBarWidth = 16.dp,
                    backgroundProgressBarColor = Color(primaryContainerColor),
                    backgroundProgressBarWidth = 16.dp,
                    roundBorder = true,
                    startAngle = 90f,
                    centerContent = {
                        Text(
                            text = "Memory",
                            style = productSansBoldTypography().caption,
                            color = Color(onSurfaceColor)
                        )
                    }
                )

                StandardSpacer()

                Column {
                    MemStats(headerInfo.memory)
                    LargeSpacer()
                    MemStats(headerInfo.swap)
                }
            }
        }
    }
}

@Composable
private fun MemStats(
    memUsage: MemUsage
) {
    val primaryColor = getColorAttribute(R.attr.colorPrimary)
    val primaryContainerColor = getColorAttribute(R.attr.colorPrimaryContainer)
    val onSurfaceColor = getColorAttribute(R.attr.colorOnSurface)
    Row {
        Text(
            modifier = Modifier.alignByBaseline(),
            text = stringResource(
                id = if (memUsage.memType == MemType.MEMORY) R.string.boost_status_mem_usage_percent else R.string.boost_status_swap_usage_percent,
                "${memUsage.memUsagePercent}%"
            ),
            style = MaterialTheme.typography.bodySmall,
            color = Color(onSurfaceColor)
        )
        SmallSpacer()
        Text(
            modifier = Modifier.alignByBaseline(),
            text = " (${memUsage.memUsageSizeString}/${memUsage.memTotalSizeString})",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = Color(onSurfaceColor)
        )
    }
    TinySpacer()
    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 48.dp)
            .height(8.dp)
            .clip(RoundedCornerShape(6.dp)),
        color = Color(primaryColor),
        trackColor = Color(primaryContainerColor),
        progress = 0.72f
    )
}