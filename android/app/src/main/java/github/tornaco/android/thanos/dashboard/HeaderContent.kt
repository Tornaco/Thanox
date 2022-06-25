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

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.module.compose.common.*
import github.tornaco.android.thanos.module.compose.common.widget.CircularProgressBar
import kotlinx.coroutines.delay

@Composable
fun HeaderContent(state: HeaderState, onHeaderClick: () -> Unit) {
    val headerInfo = state.headerInfo
    val cardBgColor = getColorAttribute(R.attr.appCardBackground)
    val primaryContainerColor = getColorAttribute(R.attr.colorPrimaryContainer)
    val onSurfaceColor = getColorAttribute(R.attr.colorOnSurface)

    val progressColor = getColorAttribute(R.attr.progressColor)
    val progressTrackColor = getColorAttribute(R.attr.progressTrackColor)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(color = Color(cardBgColor))
            .clickableWithRipple {
                onHeaderClick()
            }
            .padding(16.dp)

    ) {
        Column {
            Row {
                FilledTonalButton(modifier = Modifier.animateContentSize(),
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
                        text = stringResource(id = R.string.boost_status_running_apps),
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
                    progressBarColor = Color(progressColor),
                    progressBarWidth = 16.dp,
                    backgroundProgressBarColor = Color(progressTrackColor),
                    backgroundProgressBarWidth = 16.dp,
                    roundBorder = true,
                    startAngle = 0f,
                    centerContent = {
                        Text(
                            text = stringResource(id = R.string.boost_status_system_memory),
                            style = productSansBoldTypography().caption,
                            color = Color(onSurfaceColor)
                        )
                    }
                )

                StandardSpacer()
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
    val onSurfaceColor = getColorAttribute(R.attr.colorOnSurface)

    val progressColor = getColorAttribute(R.attr.progressColor)
    val progressTrackColor = getColorAttribute(R.attr.progressTrackColor)

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

        val extraDesc = if (memUsage.isEnabled) {
            stringResource(
                id = R.string.boost_status_available,
                memUsage.memAvailableSizeString
            )
        } else {
            stringResource(id = R.string.boost_status_not_enabled)
        }
        Text(
            modifier = Modifier.alignByBaseline(),
            text = " ($extraDesc)",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = Color(onSurfaceColor)
        )
    }
    TinySpacer()

    AnimatedLinearProgressIndicator(memUsage, progressColor, progressTrackColor)
}

@Composable
private fun AnimatedLinearProgressIndicator(
    memUsage: MemUsage,
    progressColor: Int,
    progressTrackColor: Int
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AnimatedTextContainer(text: String, content: @Composable (String) -> Unit) {
    AnimatedContent(
        targetState = text,
        transitionSpec = {
            // Compare the incoming number with the previous number.
            (slideInVertically { height -> height } + fadeIn() with
                    slideOutVertically { height -> -height } + fadeOut())
                .using(
                    // Disable clipping since the faded slide-in/out should
                    // be displayed out of bounds.
                    SizeTransform(clip = false)
                )
        }
    ) { targetText ->
        content(targetText)
    }
}