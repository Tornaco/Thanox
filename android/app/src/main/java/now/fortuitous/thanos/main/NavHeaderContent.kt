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

@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package now.fortuitous.thanos.main

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import github.tornaco.android.thanos.module.compose.common.widget.ExpandableState
import github.tornaco.android.thanos.module.compose.common.widget.MediumSpacer
import github.tornaco.android.thanos.module.compose.common.widget.SmallSpacer
import github.tornaco.android.thanos.module.compose.common.widget.StandardSpacer
import github.tornaco.android.thanos.module.compose.common.widget.TinySpacer
import github.tornaco.android.thanos.module.compose.common.widget.productSansBoldTypography
import github.tornaco.android.thanos.support.NavHeaderContainer
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
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(cardCornerSize))
            .background(color = LocalThanoxColorSchema.current.cardBgColor)
            .clickable {
                onHeaderClick()
            }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (headerInfo.swap.isEnabled) 0.dp else 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CpuProgressBar(headerInfo)
            if (headerInfo.swap.isEnabled) {
                FatMemProgressBar(headerInfo)
            } else {
                MemProgressBar(headerInfo)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(cardCornerSize))
            .background(color = LocalThanoxColorSchema.current.cardBgColor)
            .clickable {
                onHeaderClick()
            }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .animateContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalButton(onClick = { onHeaderClick() }) {
                    AnimatedTextContainer(text = "${headerInfo.runningAppsCount}") {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = W700
                        )
                    }
                    SmallSpacer()
                    Text(
                        text = stringResource(id = github.tornaco.android.thanos.res.R.string.boost_status_running_apps),
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = W500
                    )
                }
            }

            StandardSpacer()

            FlowRow(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .padding(top = 4.dp)
            ) {
                headerInfo.runningApps.forEach {
                    AppIcon(
                        Modifier
                            .padding(2.dp)
                            .size(if (headerInfo.runningApps.size > 6) 14.dp else 18.dp), it
                    )
                }
            }
        }

    }
}

@Composable
private fun CpuProgressBar(
    headerInfo: StatusHeaderInfo,
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        Box(
            modifier = Modifier.Companion.align(Alignment.CenterVertically),
            contentAlignment = Alignment.Center
        ) {
            val progressBarWidth = 16.dp
            val mainProgressSize = 90.dp
            val animatedProgress by
            animateFloatAsState(
                targetValue = headerInfo.cpu.totalPercent.toFloat() / 100f,
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
            )
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.size(mainProgressSize),
                    strokeWidth = progressBarWidth
                )
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
    headerInfo: StatusHeaderInfo
) {
    val progressBarWidth = 16.dp
    val mainProgressSize = 90.dp
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        val animatedProgress by
        animateFloatAsState(
            targetValue = headerInfo.memory.memUsagePercent.toFloat() / 100f,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.size(mainProgressSize),
                strokeWidth = progressBarWidth
            )
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Mem",
                    style = productSansBoldTypography().caption,
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
) {
    val progressColor = MaterialTheme.colorScheme.primary
    val secondaryProgressColor = MaterialTheme.colorScheme.tertiary

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        Box(
            modifier = Modifier.Companion.align(Alignment.CenterVertically),
            contentAlignment = Alignment.Center
        ) {
            val progressBarWidth = 12.dp
            val mainProgressSize = 90.dp
            val progressBarPadding = 4.dp
            val secondProgressSize = mainProgressSize - (2 * progressBarWidth) - progressBarPadding

            val animatedMemUsagePercentProgress by
            animateFloatAsState(
                targetValue = headerInfo.memory.memUsagePercent.toFloat() / 100f,
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
            )
            CircularProgressIndicator(
                progress = { animatedMemUsagePercentProgress },
                modifier = Modifier.size(mainProgressSize),
                strokeWidth = progressBarWidth,
                color = progressColor
            )


            val animatedSwapUsagePercentProgress by
            animateFloatAsState(
                targetValue = headerInfo.swap.memUsagePercent.toFloat() / 100f,
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
            )
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { animatedSwapUsagePercentProgress },
                    modifier = Modifier.size(secondProgressSize),
                    strokeWidth = progressBarWidth,
                    color = secondaryProgressColor
                )
                Text(
                    text = "Mem",
                    style = productSansBoldTypography().caption.copy(fontSize = 8.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        MediumSpacer()
        Column {
            MemStats(headerInfo.memory, progressColor)
            MediumSpacer()
            MemStats(headerInfo.swap, secondaryProgressColor)
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