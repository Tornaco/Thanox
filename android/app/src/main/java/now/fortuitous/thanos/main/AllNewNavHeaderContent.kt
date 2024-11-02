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

package now.fortuitous.thanos.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import github.tornaco.android.thanos.module.compose.common.theme.cardCornerSize
import github.tornaco.android.thanos.module.compose.common.theme.getColorAttribute
import github.tornaco.android.thanos.module.compose.common.widget.AnimatedTextContainer
import github.tornaco.android.thanos.module.compose.common.widget.AppIcon
import github.tornaco.android.thanos.module.compose.common.widget.CircularProgressBar
import github.tornaco.android.thanos.module.compose.common.widget.MediumSpacer
import github.tornaco.android.thanos.module.compose.common.widget.StandardSpacer
import github.tornaco.android.thanos.module.compose.common.widget.productSansBoldTypography
import now.fortuitous.thanos.apps.AppsManageActivity
import now.fortuitous.thanos.dashboard.AppCpuUsage
import now.fortuitous.thanos.dashboard.StatusHeaderInfo


@Composable
fun primaryProgressBarColor() = MaterialTheme.colorScheme.primary

@Composable
fun primaryProgressTrackColor() = MaterialTheme.colorScheme.secondaryContainer

@Composable
fun secondaryProgressBarColor() = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)

@Composable
fun secondaryProgressTrackColor() = MaterialTheme.colorScheme.secondaryContainer

@Composable
fun AllNewNavHeaderContent(
    headerInfo: StatusHeaderInfo,
    onHeaderClick: () -> Unit
) {
    StandardSpacer()
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        HeaderContentContainer(
            background = Color(0xffFF5145).copy(alpha = 0.06f),
            onClick = onHeaderClick
        ) {
            CpuProgressBar(headerInfo)
        }

        HeaderContentContainer(
            background = Color(0xff00BBDF).copy(alpha = 0.06f),
            modifier = Modifier.weight(1f, fill = true),
            onClick = onHeaderClick
        ) {
            if (headerInfo.swap.isEnabled) {
                FatMemProgressBar(headerInfo)
            } else {
                MemProgressBar(headerInfo)
            }
        }
    }
    StandardSpacer()
    RunningApps(headerInfo, onHeaderClick)
}

@Composable
private fun RunningApps(headerInfo: StatusHeaderInfo, onClick: () -> Unit) {
    val context = LocalContext.current

    HeaderContentContainer(
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        background = Color(0xff35AA53).copy(alpha = 0.06f),
        onClick = onClick
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AnimatedTextContainer(text = "${headerInfo.runningAppsCount}") {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 22.sp,
                        fontWeight = W400,
                        color = themedTextColor(MaterialTheme.colorScheme.primary),
                    ),
                    fontWeight = W500
                )
            }
            Text(
                text = stringResource(id = github.tornaco.android.thanos.res.R.string.boost_status_running_apps),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = W400
                ),
                color = MaterialTheme.colorScheme.onSurface,
            )

            StandardSpacer()
            ColoredIcon(
                size = 32.dp,
                padding = 0.dp,
                onClick = {
                    AppsManageActivity.start(context)
                }
            ) {
                Icon(
                    painter = painterResource(github.tornaco.android.thanos.R.drawable.ic_apps_2_line),
                    tint = LocalContentColor.current,
                    modifier = Modifier.size(16.dp),
                    contentDescription = null
                )
            }

            StandardSpacer()
            ColoredIcon(
                size = 32.dp,
                padding = 0.dp,
                onClick = {
                    onKeyClear(context) {}

                }
            ) {
                Icon(
                    painter = painterResource(github.tornaco.android.thanos.R.drawable.ic_rocket_line),
                    tint = LocalContentColor.current,
                    modifier = Modifier.size(16.dp),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun HeaderContentContainer(
    background: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(12.dp),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(cardCornerSize))
            .background(background)
            .clickable { onClick() }
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}


@Composable
private fun CpuProgressBar(
    headerInfo: StatusHeaderInfo,
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        val progressBarWidth = 10.5.dp
        val mainProgressSize = 68.dp
        CircularProgressBar(
            modifier = Modifier
                .size(mainProgressSize),
            progress = headerInfo.cpu.totalPercent.toFloat(),
            progressMax = 100f,
            progressBarColor = primaryProgressBarColor(),
            progressBarWidth = progressBarWidth,
            backgroundProgressBarColor = primaryProgressTrackColor(),
            backgroundProgressBarWidth = progressBarWidth,
            roundBorder = true,
            startAngle = 0f,
            centerContent = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CPU",
                        style = productSansBoldTypography().caption,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        modifier = Modifier,
                        textAlign = TextAlign.Center,
                        text = "${headerInfo.cpu.totalPercent}%",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        )
    }
}


@Composable
private fun AppCpuUsage(usage: AppCpuUsage) {
    val onSurfaceColor = getColorAttribute(com.google.android.material.R.attr.colorOnSurface)
    Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
        AppIcon(modifier = Modifier.size(16.dp), usage.appInfo)
        Text(
            modifier = Modifier,
            textAlign = TextAlign.Center,
            text = "${usage.percent}%",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
            color = Color(onSurfaceColor)
        )
    }
}


@Composable
private fun MemProgressBar(
    headerInfo: StatusHeaderInfo,
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        Box(
            modifier = Modifier.Companion.align(Alignment.CenterVertically),
            contentAlignment = Alignment.Center
        ) {
            val progressBarWidth = 10.5.dp
            val mainProgressSize = 68.dp
            CircularProgressBar(
                modifier = Modifier
                    .size(mainProgressSize),
                progress = headerInfo.memory.memUsagePercent.toFloat(),
                progressMax = 100f,
                progressBarColor = primaryProgressBarColor(),
                progressBarWidth = progressBarWidth,
                backgroundProgressBarColor = primaryProgressTrackColor(),
                backgroundProgressBarWidth = progressBarWidth,
                roundBorder = true,
                startAngle = 0f,
                centerContent = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Mem",
                            style = productSansBoldTypography().caption.copy(
                                fontSize = 9.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            modifier = Modifier,
                            textAlign = TextAlign.Center,
                            text = "${headerInfo.memory.memUsagePercent}%",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    }
}


@Composable
private fun FatMemProgressBar(
    headerInfo: StatusHeaderInfo,
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        Box(
            modifier = Modifier.Companion.align(Alignment.CenterVertically),
            contentAlignment = Alignment.Center
        ) {
            val progressBarWidth = 10.5.dp
            val mainProgressSize = 68.dp
            val progressBarPadding = 4.dp
            val secondProgressSize = mainProgressSize - (2 * progressBarWidth) - progressBarPadding
            CircularProgressBar(
                modifier = Modifier
                    .size(mainProgressSize),
                progress = headerInfo.memory.memUsagePercent.toFloat(),
                progressMax = 100f,
                progressBarColor = primaryProgressBarColor(),
                progressBarWidth = progressBarWidth,
                backgroundProgressBarColor = primaryProgressTrackColor(),
                backgroundProgressBarWidth = progressBarWidth,
                roundBorder = true,
                startAngle = 0f,
                centerContent = {}
            )
            CircularProgressBar(
                modifier = Modifier
                    .size(secondProgressSize),
                progress = headerInfo.swap.memUsagePercent.toFloat(),
                progressMax = 100f,
                progressBarColor = secondaryProgressBarColor(),
                progressBarWidth = progressBarWidth,
                backgroundProgressBarColor = secondaryProgressTrackColor(),
                backgroundProgressBarWidth = progressBarWidth,
                roundBorder = true,
                startAngle = 0f,
                centerContent = {
                }
            )
        }
        MediumSpacer()
        Column {
            MemStats(headerInfo.memory, primaryProgressBarColor())
            MediumSpacer()
            MemStats(headerInfo.swap, secondaryProgressBarColor())
        }
    }
}