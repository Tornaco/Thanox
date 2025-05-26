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

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import github.tornaco.android.thanos.module.compose.common.theme.LocalThanoxColorSchema
import github.tornaco.android.thanos.module.compose.common.theme.cardCornerSize
import github.tornaco.android.thanos.module.compose.common.theme.getColorAttribute
import github.tornaco.android.thanos.module.compose.common.widget.AnimatedTextContainer
import github.tornaco.android.thanos.module.compose.common.widget.AppIcon
import github.tornaco.android.thanos.module.compose.common.widget.CircularProgressBar
import github.tornaco.android.thanos.module.compose.common.widget.ExpandableState
import github.tornaco.android.thanos.module.compose.common.widget.MediumSpacer
import github.tornaco.android.thanos.module.compose.common.widget.SmallSpacer
import github.tornaco.android.thanos.module.compose.common.widget.StandardSpacer
import github.tornaco.android.thanos.module.compose.common.widget.TinySpacer
import github.tornaco.android.thanos.module.compose.common.widget.productSansBoldTypography
import github.tornaco.android.thanos.support.NavHeaderContainer
import github.tornaco.android.thanos.support.clickableWithRippleBorderless
import kotlinx.coroutines.delay
import now.fortuitous.thanos.dashboard.AppCpuUsage
import now.fortuitous.thanos.dashboard.MemType
import now.fortuitous.thanos.dashboard.MemUsage
import now.fortuitous.thanos.dashboard.StatusHeaderInfo

@Composable
fun NavHeaderContent(
    modifier: Modifier = Modifier,
    headerInfo: StatusHeaderInfo,
    onHeaderClick: () -> Unit
) {
    val expandState = remember { mutableStateOf(ExpandableState.Collapsed) }
    NavHeaderContainer(
        expandState = expandState,
        mainContent = {
            MainNavHeaderContent(
                modifier = modifier,
                headerInfo = headerInfo,
                onHeaderClick = onHeaderClick
            )
        },
        expandContent = {
            Text(text = "Coming soon~")
        })
}

@Composable
private fun MainNavHeaderContent(
    modifier: Modifier = Modifier,
    headerInfo: StatusHeaderInfo,
    onHeaderClick: () -> Unit,
) {
    val primaryContainerColor =
        getColorAttribute(com.google.android.material.R.attr.colorPrimaryContainer)
    val onSurfaceColor = getColorAttribute(com.google.android.material.R.attr.colorOnSurface)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(cardCornerSize))
            .background(color = LocalThanoxColorSchema.current.cardBgColor)
            .clickableWithRippleBorderless {
                onHeaderClick()
            }
            .padding(16.dp)

    ) {
        Column {
            Row {
                FilledTonalButton(
                    modifier = Modifier.animateContentSize(),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color(
                            primaryContainerColor
                        )
                    ),
                    onClick = {
                        onHeaderClick()
                    }) {
                    AnimatedTextContainer(text = "${headerInfo.runningAppsCount}") {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(onSurfaceColor),
                            fontWeight = W500
                        )
                    }
                    Text(
                        text = stringResource(id = github.tornaco.android.thanos.res.R.string.boost_status_running_apps),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                            fontWeight = W700
                        ),
                        color = Color(onSurfaceColor)
                    )
                }
            }

            StandardSpacer()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = if (headerInfo.swap.isEnabled) 0.dp else 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CpuProgressBar(headerInfo, onSurfaceColor)
                if (headerInfo.swap.isEnabled) {
                    FatMemProgressBar(headerInfo, onSurfaceColor)
                } else {
                    MemProgressBar(headerInfo, onSurfaceColor)
                }
            }
        }
    }
}

@Composable
private fun CpuProgressBar(
    headerInfo: StatusHeaderInfo,
    onSurfaceColor: Int,
) {
    val progressColor = getColorAttribute(com.google.android.material.R.attr.colorPrimary)
    val progressTrackColor =
        getColorAttribute(github.tornaco.android.thanos.module.common.R.attr.progressTrackColor)

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        Box(
            modifier = Modifier.Companion.align(Alignment.CenterVertically),
            contentAlignment = Alignment.Center
        ) {
            val progressBarWidth = 16.dp
            val mainProgressSize = 90.dp
            CircularProgressBar(
                modifier = Modifier
                    .size(mainProgressSize),
                progress = headerInfo.cpu.totalPercent.toFloat(),
                progressMax = 100f,
                progressBarColor = Color(progressColor),
                progressBarWidth = progressBarWidth,
                backgroundProgressBarColor = Color(progressTrackColor),
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
                            color = Color(onSurfaceColor)
                        )
                        Text(
                            modifier = Modifier,
                            textAlign = TextAlign.Center,
                            text = "${headerInfo.cpu.totalPercent}%",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                            color = Color(onSurfaceColor)
                        )
                    }
                }
            )
        }
        MediumSpacer()
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            headerInfo.cpu.topNPkgs.forEach {
                AppCpuUsage(it)
            }
        }
    }
}

@Composable
private fun MemProgressBar(
    headerInfo: StatusHeaderInfo,
    onSurfaceColor: Int,
) {
    val progressColor = getColorAttribute(com.google.android.material.R.attr.colorPrimary)
    val progressTrackColor =
        getColorAttribute(github.tornaco.android.thanos.module.common.R.attr.progressTrackColor)

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        Box(
            modifier = Modifier.Companion.align(Alignment.CenterVertically),
            contentAlignment = Alignment.Center
        ) {
            val progressBarWidth = 16.dp
            val mainProgressSize = 90.dp
            CircularProgressBar(
                modifier = Modifier
                    .size(mainProgressSize),
                progress = headerInfo.memory.memUsagePercent.toFloat(),
                progressMax = 100f,
                progressBarColor = Color(progressColor),
                progressBarWidth = progressBarWidth,
                backgroundProgressBarColor = Color(progressTrackColor),
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
                            style = productSansBoldTypography().caption,
                            color = Color(onSurfaceColor)
                        )
                        Text(
                            modifier = Modifier,
                            textAlign = TextAlign.Center,
                            text = "${headerInfo.memory.memUsagePercent}%",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                            color = Color(onSurfaceColor)
                        )
                    }
                }
            )
        }
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
private fun FatMemProgressBar(
    headerInfo: StatusHeaderInfo,
    onSurfaceColor: Int,
) {
    val progressColor = getColorAttribute(com.google.android.material.R.attr.colorPrimary)
    val secondaryProgressColor = getColorAttribute(com.google.android.material.R.attr.colorTertiary)
    val progressTrackColor =
        getColorAttribute(github.tornaco.android.thanos.module.common.R.attr.progressTrackColor)

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        Box(
            modifier = Modifier.Companion.align(Alignment.CenterVertically),
            contentAlignment = Alignment.Center
        ) {
            val progressBarWidth = 12.dp
            val mainProgressSize = 90.dp
            val progressBarPadding = 4.dp
            val secondProgressSize = mainProgressSize - (2 * progressBarWidth) - progressBarPadding
            CircularProgressBar(
                modifier = Modifier
                    .size(mainProgressSize),
                progress = headerInfo.memory.memUsagePercent.toFloat(),
                progressMax = 100f,
                progressBarColor = Color(progressColor),
                progressBarWidth = progressBarWidth,
                backgroundProgressBarColor = Color(progressTrackColor),
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
                progressBarColor = Color(secondaryProgressColor),
                progressBarWidth = progressBarWidth,
                backgroundProgressBarColor = Color(progressTrackColor),
                backgroundProgressBarWidth = progressBarWidth,
                roundBorder = true,
                startAngle = 0f,
                centerContent = {
                    Text(
                        text = "Mem",
                        style = productSansBoldTypography().caption.copy(fontSize = 8.sp),
                        color = Color(onSurfaceColor)
                    )
                }
            )
        }
        MediumSpacer()
        Column {
            MemStats(headerInfo.memory, Color(progressColor))
            MediumSpacer()
            MemStats(headerInfo.swap, Color(secondaryProgressColor))
        }
    }
}

@Composable
private fun MemStats(
    memUsage: MemUsage,
    color: Color,
) {
    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Start) {
        Box(
            // For alignment.
            modifier = Modifier
                .offset(y = 2.dp)
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        TinySpacer()
        Column(modifier = Modifier) {
            val onSurfaceColor =
                getColorAttribute(com.google.android.material.R.attr.colorOnSurface)
            Text(
                modifier = Modifier,
                textAlign = TextAlign.Center,
                text = "${if (memUsage.memType == MemType.MEMORY) "Mem" else "Swap"} ${memUsage.memUsagePercent}%",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                color = Color(onSurfaceColor)
            )
            SmallSpacer()

            val extraDesc = if (memUsage.isEnabled) {
                stringResource(
                    id = github.tornaco.android.thanos.res.R.string.boost_status_available,
                    memUsage.memAvailableSizeString
                )
            } else {
                stringResource(id = github.tornaco.android.thanos.res.R.string.boost_status_not_enabled)
            }
            Text(
                modifier = Modifier,
                textAlign = TextAlign.Center,
                text = "($extraDesc)",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                color = Color(onSurfaceColor)
            )
        }
    }
}

@Composable
private fun AnimatedLinearProgressIndicator(
    memUsage: MemUsage,
    progressColor: Int,
    progressTrackColor: Int,
) {
    var startAnim by remember {
        mutableStateOf(false)
    }
    val animatePercent by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    LaunchedEffect(memUsage) {
        delay(240)
        startAnim = true
    }
    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 48.dp)
            .height(8.dp)
            .clip(RoundedCornerShape(6.dp)),
        color = Color(progressColor),
        trackColor = Color(progressTrackColor),
        progress = memUsage.memUsagePercent.toFloat() / 100f * animatePercent
    )
}