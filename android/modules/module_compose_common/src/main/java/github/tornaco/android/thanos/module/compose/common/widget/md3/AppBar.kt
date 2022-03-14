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

package github.tornaco.android.thanos.module.compose.common.widget.md3

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun LargeTopAppBarX(
    title: @Composable () -> Unit,
    smallTitle: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.largeTopAppBarColors(),
    scrollBehaviorX: TopAppBarScrollBehaviorX? = null
) {
    TwoRowsTopAppBar(
        title = title,
        titleTextStyle = MaterialTheme.typography.headlineLarge,
        smallTitleTextStyle = MaterialTheme.typography.titleMedium,
        titleBottomPadding = LargeTitleBottomPadding,
        smallTitle = smallTitle,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = colors,
        maxHeight = 152.dp,
        pinnedHeight = 64.dp,
        scrollBehaviorX = scrollBehaviorX
    )
}

/**
 * A TopAppBarScrollBehavior defines how an app bar should behave when the content under it is
 * scrolled.
 *
 * @see [TopAppBarDefaults.pinnedScrollBehavior]
 * @see [TopAppBarDefaults.enterAlwaysScrollBehavior]
 * @see [TopAppBarDefaults.exitUntilCollapsedScrollBehavior]
 */
@Stable
interface TopAppBarScrollBehaviorX {

    /**
     * A [NestedScrollConnection] that should be attached to a [Modifier.nestedScroll] in order to
     * keep track of the scroll events.
     */
    val nestedScrollConnection: NestedScrollConnection

    /**
     * Returns the top app bar's current scroll fraction.
     *
     * A scrollFraction is a value between `0.0` to `1.0` that provides a percentage of the app
     * bar scroll position when the content is scrolled. `0.0` represents an expanded app bar,
     * while `1.0`
     * represents a collapsed one (e.g. the app bar is scrolled to its target offset). Note that
     * this value will be updated on scroll even if the [offset] is pinned to a specific
     * value (see [TopAppBarDefaults.pinnedScrollBehavior]). In this case a value of 1.0 represents
     * that the scroll value has exceeded the height of the pinned app bar, as if the app bar was
     * collapsing.
     */
    val scrollFraction: Float

    /**
     * The top app bar's offset limit in pixels, which represents the offset that a top app bar is
     * allowed to scroll when the scrollable content is scrolled.
     *
     * This limit is represented by a negative [Float], and used to coerce the [offset] value when
     * the content is scrolled.
     */
    var offsetLimit: Float

    /**
     * The top app bar's current offset in pixels.
     *
     * The offset is usually between zero and the [offsetLimit].
     */
    var offset: Float

    /**
     * The current content offset that is updated when the nested scroll connection consumes scroll
     * events.
     *
     * A common behavior implementation would update this value to be the sum of all
     * [NestedScrollConnection.onPostScroll] `consumed.y` values.
     */
    var contentOffset: Float
}

/**
 * Represents the colors used by a top app bar in different states.
 *
 * Each app bar has their own default implementation available in [TopAppBarDefaults], such as
 * [TopAppBarDefaults.smallTopAppBarColors] for [SmallTopAppBar].
 */
@Stable
interface TopAppBarColors {
    /**
     * Represents the container color used for the top app bar, depending on whether the app bar is
     * scrolled, and the percentage of its area that is scrolled.
     *
     * @param scrollFraction the scroll percentage of the top app bar (0.0 when the app bar is
     * considered expanded to 1.0 when the app bar is scrolled to its target offset)
     */
    @Composable
    fun containerColor(scrollFraction: Float): State<Color>

    /**
     * Represents the content color used for the top app bar's navigation icon depending on whether
     * the app bar is scrolled, and the percentage of its area that is scrolled.
     *
     * @param scrollFraction the scroll percentage of the top app bar (0.0 when the app bar is
     * considered expanded to 1.0 when the app bar is scrolled to its target offset)
     */
    @Composable
    fun navigationIconContentColor(scrollFraction: Float): State<Color>

    /**
     * Represents the content color used for the top app bar's title depending on whether the app
     * bar is scrolled, and the percentage of its area that is scrolled.
     *
     * @param scrollFraction the scroll percentage of the top app bar (0.0 when the app bar is
     * considered expanded to 1.0 when the app bar is scrolled to its target offset)
     */
    @Composable
    fun titleContentColor(scrollFraction: Float): State<Color>

    /**
     * Represents the content color used for the top app bar's action icons depending on whether the
     * app bar is scrolled, and the percentage of its area that is scrolled.
     *
     * @param scrollFraction the scroll percentage of the top app bar (0.0 when the app bar is
     * considered expanded to 1.0 when the app bar is scrolled to its target offset)
     */
    @Composable
    fun actionIconContentColor(scrollFraction: Float): State<Color>
}

/** Contains default values used for the top app bar implementations. */
object TopAppBarDefaults {

    private fun ColorScheme.applyTonalElevation(backgroundColor: Color, elevation: Dp): Color {
        return if (backgroundColor == surface) {
            surfaceColorAtElevation(elevation)
        } else {
            backgroundColor
        }
    }

    private fun ColorScheme.surfaceColorAtElevation(
        elevation: Dp,
    ): Color {
        if (elevation == 0.dp) return surface
        val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
        return primary.copy(alpha = alpha).compositeOver(surface)
    }

    @Composable
    fun largeTopAppBarColors(
        containerColor: Color = MaterialTheme.colorScheme.surface,
        scrolledContainerColor: Color = MaterialTheme.colorScheme.surface,
        navigationIconContentColor: Color = MaterialTheme.colorScheme.onSurface,
        titleContentColor: Color = MaterialTheme.colorScheme.onSurface,
        actionIconContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    ): TopAppBarColors {
        return remember(
            containerColor,
            scrolledContainerColor,
            navigationIconContentColor,
            titleContentColor,
            actionIconContentColor
        ) {
            InterpolatingTopAppBarColors(
                containerColor,
                scrolledContainerColor,
                navigationIconContentColor,
                titleContentColor,
                actionIconContentColor
            )
        }
    }

    /**
     * Returns a pinned [TopAppBarScrollBehaviorX] that tracks nested-scroll callbacks and
     * updates its [TopAppBarScrollBehaviorX.contentOffset] accordingly.
     *
     * @param canScroll a callback used to determine whether scroll events are to be handled by this
     * pinned [TopAppBarScrollBehaviorX]
     */
    @ExperimentalMaterial3Api
    fun pinnedScrollBehavior(canScroll: () -> Boolean = { true }): TopAppBarScrollBehaviorX =
        PinnedScrollBehaviorX(canScroll)

    /**
     * Returns a [TopAppBarScrollBehaviorX]. A top app bar that is set up with this
     * [TopAppBarScrollBehaviorX] will immediately collapse when the content is pulled up, and will
     * immediately appear when the content is pulled down.
     *
     * @param canScroll a callback used to determine whether scroll events are to be
     * handled by this [EnterAlwaysScrollBehaviorX]
     */
    @ExperimentalMaterial3Api
    fun enterAlwaysScrollBehavior(canScroll: () -> Boolean = { true }): TopAppBarScrollBehaviorX =
        EnterAlwaysScrollBehaviorX(canScroll)

    /**
     * Returns a [TopAppBarScrollBehaviorX] that adjusts its properties to affect the colors and
     * height of the top app bar.
     *
     * A top app bar that is set up with this [TopAppBarScrollBehaviorX] will immediately collapse
     * when the nested content is pulled up, and will expand back the collapsed area when the
     * content is  pulled all the way down.
     *
     * @param decayAnimationSpec a [DecayAnimationSpec] that will be used by the top app bar motion
     * when the user flings the content. Preferably, this should match the animation spec used by
     * the scrollable content. See also [androidx.compose.animation.rememberSplineBasedDecay] for a
     * default [DecayAnimationSpec] that can be used with this behavior.
     * @param canScroll a callback used to determine whether scroll events are to be
     * handled by this [ExitUntilCollapsedScrollBehaviorX]
     */
    @ExperimentalMaterial3Api
    fun exitUntilCollapsedScrollBehavior(
        decayAnimationSpec: DecayAnimationSpec<Float>,
        canScroll: () -> Boolean = { true }
    ): TopAppBarScrollBehaviorX =
        ExitUntilCollapsedScrollBehaviorX(decayAnimationSpec, canScroll)
}

/**
 * A two-rows top app bar that is designed to be called by the Large and Medium top app bar
 * composables.
 *
 * @throws [IllegalArgumentException] if the given [maxHeight] is equal or smaller than the
 * [pinnedHeight]
 */
@Composable
private fun TwoRowsTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    titleTextStyle: TextStyle,
    titleBottomPadding: Dp,
    smallTitle: @Composable () -> Unit,
    smallTitleTextStyle: TextStyle,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    colors: TopAppBarColors,
    maxHeight: Dp,
    pinnedHeight: Dp,
    scrollBehaviorX: TopAppBarScrollBehaviorX?
) {
    if (maxHeight <= pinnedHeight) {
        throw IllegalArgumentException(
            "A TwoRowsTopAppBar max height should be greater than its pinned height"
        )
    }
    val pinnedHeightPx: Float
    val maxHeightPx: Float
    val titleBottomPaddingPx: Int
    LocalDensity.current.run {
        pinnedHeightPx = pinnedHeight.toPx()
        maxHeightPx = maxHeight.toPx()
        titleBottomPaddingPx = titleBottomPadding.roundToPx()
    }

    // Set a scroll offset limit that will hide just the title area and will keep the small title
    // area visible.
    SideEffect {
        if (scrollBehaviorX?.offsetLimit != pinnedHeightPx - maxHeightPx) {
            scrollBehaviorX?.offsetLimit = pinnedHeightPx - maxHeightPx
        }
    }

    val scrollPercentage =
        if (scrollBehaviorX == null || scrollBehaviorX.offsetLimit == 0f) {
            0f
        } else {
            scrollBehaviorX.offset / scrollBehaviorX.offsetLimit
        }

    // Obtain the container Color from the TopAppBarColors.
    // This will potentially animate or interpolate a transition between the container color and the
    // container's scrolled color according to the app bar's scroll state.
    val scrollFraction = scrollBehaviorX?.scrollFraction ?: 0f
    val appBarContainerColor by colors.containerColor(scrollFraction)

    // Wrap the given actions in a Row.
    val actionsRow = @Composable {
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            content = actions
        )
    }
    val titleAlpha = 1f - scrollPercentage
    // Hide the top row title semantics when its alpha value goes below 0.5 threshold.
    // Hide the bottom row title semantics when the top title semantics are active.
    val hideTopRowSemantics = scrollPercentage < 0.5f
    val hideBottomRowSemantics = !hideTopRowSemantics
    Surface(modifier = modifier, color = appBarContainerColor) {
        Column {
            TopAppBarLayout(
                modifier = Modifier,
                heightPx = pinnedHeightPx,
                navigationIconContentColor =
                colors.navigationIconContentColor(scrollFraction).value,
                titleContentColor = colors.titleContentColor(scrollFraction).value,
                actionIconContentColor = colors.actionIconContentColor(scrollFraction).value,
                title = smallTitle,
                titleTextStyle = smallTitleTextStyle,
                titleAlpha = 1f - titleAlpha,
                titleVerticalArrangement = Arrangement.Center,
                titleHorizontalArrangement = Arrangement.Start,
                titleBottomPadding = 0,
                hideTitleSemantics = hideTopRowSemantics,
                navigationIcon = navigationIcon,
                actions = actionsRow,
            )
            TopAppBarLayout(
                modifier = Modifier.clipToBounds(),
                heightPx = maxHeightPx - pinnedHeightPx + (scrollBehaviorX?.offset ?: 0f),
                navigationIconContentColor =
                colors.navigationIconContentColor(scrollFraction).value,
                titleContentColor = colors.titleContentColor(scrollFraction).value,
                actionIconContentColor = colors.actionIconContentColor(scrollFraction).value,
                title = title,
                titleTextStyle = titleTextStyle,
                titleAlpha = titleAlpha,
                titleVerticalArrangement = Arrangement.Bottom,
                titleHorizontalArrangement = Arrangement.Start,
                titleBottomPadding = titleBottomPaddingPx,
                hideTitleSemantics = hideBottomRowSemantics,
                navigationIcon = {},
                actions = {}
            )
        }
    }
}

/**
 * The base [Layout] for all top app bars. This function lays out a top app bar navigation icon
 * (leading icon), a title (header), and action icons (trailing icons). Note that the navigation and
 * the actions are optional.
 *
 * @param heightPx the total height this layout is capped to
 * @param navigationIconContentColor the content color that will be applied via a
 * [LocalContentColor] when composing the navigation icon
 * @param titleContentColor the color that will be applied via a [LocalContentColor] when composing
 * the title
 * @param actionIconContentColor the content color that will be applied via a [LocalContentColor]
 * when composing the action icons
 * @param title the top app bar title (header)
 * @param titleTextStyle the title's text style
 * @param modifier a [Modifier]
 * @param titleAlpha the title's alpha
 * @param titleVerticalArrangement the title's vertical arrangement
 * @param titleHorizontalArrangement the title's horizontal arrangement
 * @param titleBottomPadding the title's bottom padding
 * @param hideTitleSemantics hides the title node from the semantic tree. Apply this
 * boolean when this layout is part of a [TwoRowsTopAppBar] to hide the title's semantics
 * from accessibility services. This is needed to avoid having multiple titles visible to
 * accessibility services at the same time, when animating between collapsed / expanded states.
 * @param navigationIcon a navigation icon [Composable]
 * @param actions actions [Composable]
 */
@Composable
private fun TopAppBarLayout(
    modifier: Modifier,
    heightPx: Float,
    navigationIconContentColor: Color,
    titleContentColor: Color,
    actionIconContentColor: Color,
    title: @Composable () -> Unit,
    titleTextStyle: TextStyle,
    titleAlpha: Float,
    titleVerticalArrangement: Arrangement.Vertical,
    titleHorizontalArrangement: Arrangement.Horizontal,
    titleBottomPadding: Int,
    hideTitleSemantics: Boolean,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable () -> Unit,
) {
    Layout(
        {
            Box(
                Modifier
                    .layoutId("navigationIcon")
                    .padding(start = TopAppBarHorizontalPadding)
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides navigationIconContentColor,
                    content = navigationIcon
                )
            }
            Box(
                Modifier
                    .layoutId("title")
                    .padding(horizontal = TopAppBarHorizontalPadding)
                    .then(if (hideTitleSemantics) Modifier.clearAndSetSemantics { } else Modifier)
            ) {
                ProvideTextStyle(value = titleTextStyle) {
                    CompositionLocalProvider(
                        LocalContentColor provides titleContentColor.copy(alpha = titleAlpha),
                        content = title
                    )
                }
            }
            Box(
                Modifier
                    .layoutId("actionIcons")
                    .padding(end = TopAppBarHorizontalPadding)
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides actionIconContentColor,
                    content = actions
                )
            }
        },
        modifier = modifier
    ) { measurables, constraints ->
        val navigationIconPlaceable =
            measurables.first { it.layoutId == "navigationIcon" }.measure(constraints)
        val actionIconsPlaceable =
            measurables.first { it.layoutId == "actionIcons" }.measure(constraints)

        val maxTitleWidth =
            constraints.maxWidth - navigationIconPlaceable.width - actionIconsPlaceable.width
        val titlePlaceable =
            measurables
                .first { it.layoutId == "title" }
                .measure(constraints.copy(maxWidth = maxTitleWidth))
        // Locate the title's baseline.
        val titleBaseline =
            if (titlePlaceable[LastBaseline] != AlignmentLine.Unspecified) {
                titlePlaceable[LastBaseline]
            } else {
                0
            }

        val layoutHeight = heightPx.roundToInt()

        layout(constraints.maxWidth, layoutHeight) {
            // Navigation icon
            navigationIconPlaceable.placeRelative(
                x = 0,
                y = (layoutHeight - navigationIconPlaceable.height) / 2
            )

            // Title
            titlePlaceable.placeRelative(
                x = when (titleHorizontalArrangement) {
                    Arrangement.Center -> (constraints.maxWidth - titlePlaceable.width) / 2
                    Arrangement.End ->
                        constraints.maxWidth - titlePlaceable.width - actionIconsPlaceable.width
                    // Arrangement.Start.
                    // An TopAppBarTitleInset will make sure the title is offset in case the
                    // navigation icon is missing.
                    else -> max(TopAppBarTitleInset.roundToPx(), navigationIconPlaceable.width)
                },
                y = when (titleVerticalArrangement) {
                    Arrangement.Center -> (layoutHeight - titlePlaceable.height) / 2
                    // Apply bottom padding from the title's baseline only when the Arrangement is
                    // "Bottom".
                    Arrangement.Bottom ->
                        if (titleBottomPadding == 0) layoutHeight - titlePlaceable.height
                        else layoutHeight - titlePlaceable.height - max(
                            0,
                            titleBottomPadding - titlePlaceable.height + titleBaseline
                        )
                    // Arrangement.Top
                    else -> 0
                }
            )

            // Action icons
            actionIconsPlaceable.placeRelative(
                x = constraints.maxWidth - actionIconsPlaceable.width,
                y = (layoutHeight - actionIconsPlaceable.height) / 2
            )
        }
    }
}

/**
 * A [TopAppBarColors] implementation that interpolates the container color according to the top
 * app bar scroll state percentage.
 *
 * This default implementation does not interpolate the leading, headline, or trailing colors.
 */
@Stable
private class InterpolatingTopAppBarColors(
    private val containerColor: Color,
    private val scrolledContainerColor: Color,
    navigationIconContentColor: Color,
    titleContentColor: Color,
    actionIconContentColor: Color
) : TopAppBarColors {

    // In this TopAppBarColors implementation, the following colors never change their value as the
    // app bar scrolls.
    private val navigationIconColorState: State<Color> = mutableStateOf(navigationIconContentColor)
    private val titleColorState: State<Color> = mutableStateOf(titleContentColor)
    private val actionIconColorState: State<Color> = mutableStateOf(actionIconContentColor)

    @Composable
    override fun containerColor(scrollFraction: Float): State<Color> {
        return rememberUpdatedState(
            lerp(
                containerColor,
                scrolledContainerColor,
                FastOutLinearInEasing.transform(scrollFraction)
            )
        )
    }

    @Composable
    override fun navigationIconContentColor(scrollFraction: Float): State<Color> =
        navigationIconColorState

    @Composable
    override fun titleContentColor(scrollFraction: Float): State<Color> = titleColorState

    @Composable
    override fun actionIconContentColor(scrollFraction: Float): State<Color> = actionIconColorState
}

/**
 * Returns a [TopAppBarScrollBehaviorX] that only adjusts its content offset, without adjusting any
 * properties that affect the height of a top app bar.
 *
 * @param canScroll a callback used to determine whether scroll events are to be
 * handled by this [PinnedScrollBehaviorX]
 */
private class PinnedScrollBehaviorX(val canScroll: () -> Boolean = { true }) :
    TopAppBarScrollBehaviorX {
    override var offsetLimit = -Float.MAX_VALUE
    override val scrollFraction: Float
        get() = if (offsetLimit != 0f) {
            1 - ((offsetLimit - contentOffset).coerceIn(
                minimumValue = offsetLimit,
                maximumValue = 0f
            ) / offsetLimit)
        } else {
            0f
        }
    override var offset = 0f
    override var contentOffset by mutableStateOf(0f)
    override var nestedScrollConnection =
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (!canScroll()) return Offset.Zero
                if (consumed.y == 0f && available.y > 0f) {
                    // Reset the total offset to zero when scrolling all the way down. This will
                    // eliminate some float precision inaccuracies.
                    contentOffset = 0f
                } else {
                    contentOffset += consumed.y
                }
                return Offset.Zero
            }
        }
}

/**
 * A [TopAppBarScrollBehaviorX] that adjusts its properties to affect the colors and height of a top
 * app bar.
 *
 * A top app bar that is set up with this [TopAppBarScrollBehaviorX] will immediately collapse when
 * the nested content is pulled up, and will immediately appear when the content is pulled down.
 *
 * @param canScroll a callback used to determine whether scroll events are to be
 * handled by this [EnterAlwaysScrollBehaviorX]
 */
private class EnterAlwaysScrollBehaviorX(val canScroll: () -> Boolean = { true }) :
    TopAppBarScrollBehaviorX {
    override val scrollFraction: Float
        get() = if (offsetLimit != 0f) {
            1 - ((offsetLimit - contentOffset).coerceIn(
                minimumValue = offsetLimit,
                maximumValue = 0f
            ) / offsetLimit)
        } else {
            0f
        }
    override var offsetLimit by mutableStateOf(-Float.MAX_VALUE)
    override var offset by mutableStateOf(0f)
    override var contentOffset by mutableStateOf(0f)
    override var nestedScrollConnection =
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (!canScroll()) return Offset.Zero
                val newOffset = (offset + available.y)
                val coerced = newOffset.coerceIn(minimumValue = offsetLimit, maximumValue = 0f)
                return if (newOffset == coerced) {
                    // Nothing coerced, meaning we're in the middle of top app bar collapse or
                    // expand.
                    offset = coerced
                    // Consume only the scroll on the Y axis.
                    available.copy(x = 0f)
                } else {
                    Offset.Zero
                }
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (!canScroll()) return Offset.Zero
                contentOffset += consumed.y
                if (offset == 0f || offset == offsetLimit) {
                    if (consumed.y == 0f && available.y > 0f) {
                        // Reset the total offset to zero when scrolling all the way down.
                        // This will eliminate some float precision inaccuracies.
                        contentOffset = 0f
                    }
                }
                offset = (offset + consumed.y).coerceIn(
                    minimumValue = offsetLimit,
                    maximumValue = 0f
                )
                return Offset.Zero
            }
        }
}

/**
 * A [TopAppBarScrollBehaviorX] that adjusts its properties to affect the colors and height of a top
 * app bar.
 *
 * A top app bar that is set up with this [TopAppBarScrollBehaviorX] will immediately collapse when
 * the nested content is pulled up, and will expand back the collapsed area when the content is
 * pulled all the way down.
 *
 * @param decayAnimationSpec a [DecayAnimationSpec] that will be used by the top app bar motion
 * when the user flings the content. Preferably, this should match the animation spec used by the
 * scrollable content. See also [androidx.compose.animation.rememberSplineBasedDecay] for a
 * default [DecayAnimationSpec] that can be used with this behavior.
 * @param canScroll a callback used to determine whether scroll events are to be
 * handled by this [ExitUntilCollapsedScrollBehaviorX]
 */
private class ExitUntilCollapsedScrollBehaviorX(
    val decayAnimationSpec: DecayAnimationSpec<Float>,
    val canScroll: () -> Boolean = { true }
) : TopAppBarScrollBehaviorX {
    override val scrollFraction: Float
        get() = if (offsetLimit != 0f) offset / offsetLimit else 0f
    override var offsetLimit by mutableStateOf(-Float.MAX_VALUE)
    override var offset by mutableStateOf(0f)
    override var contentOffset by mutableStateOf(0f)
    override var nestedScrollConnection =
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // Don't intercept if scrolling down.
                if (!canScroll() || available.y > 0f) return Offset.Zero

                val newOffset = (offset + available.y)
                val coerced = newOffset.coerceIn(minimumValue = offsetLimit, maximumValue = 0f)
                return if (newOffset == coerced) {
                    // Nothing coerced, meaning we're in the middle of top app bar collapse or
                    // expand.
                    offset = coerced
                    // Consume only the scroll on the Y axis.
                    available.copy(x = 0f)
                } else {
                    Offset.Zero
                }
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (!canScroll()) return Offset.Zero
                contentOffset += consumed.y

                if (available.y < 0f || consumed.y < 0f) {
                    // When scrolling up, just update the state's offset.
                    val oldOffset = offset
                    offset = (offset + consumed.y).coerceIn(
                        minimumValue = offsetLimit,
                        maximumValue = 0f
                    )
                    return Offset(0f, offset - oldOffset)
                }

                if (consumed.y == 0f && available.y > 0) {
                    // Reset the total offset to zero when scrolling all the way down. This will
                    // eliminate some float precision inaccuracies.
                    contentOffset = 0f
                }

                if (available.y > 0f) {
                    // Adjust the offset in case the consumed delta Y is less than what was recorded
                    // as available delta Y in the pre-scroll.
                    val oldOffset = offset
                    offset = (offset + available.y).coerceIn(
                        minimumValue = offsetLimit,
                        maximumValue = 0f
                    )
                    return Offset(0f, offset - oldOffset)
                }
                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                val result = super.onPostFling(consumed, available)
                // TODO(b/179417109): We get positive Velocity when flinging up while the top app
                //  bar is changing its height. Track b/179417109 for a fix.
                if ((available.y < 0f && contentOffset == 0f) ||
                    (available.y > 0f && offset < 0f)
                ) {
                    return result +
                            onTopBarFling(
                                scrollBehaviorX = this@ExitUntilCollapsedScrollBehaviorX,
                                initialVelocity = available.y,
                                decayAnimationSpec = decayAnimationSpec,
                                snap = true
                            )
                }
                return result
            }
        }
}

private suspend fun onTopBarFling(
    scrollBehaviorX: TopAppBarScrollBehaviorX,
    initialVelocity: Float,
    decayAnimationSpec: DecayAnimationSpec<Float>,
    snap: Boolean
): Velocity {
    if (abs(initialVelocity) > 1f) {
        var remainingVelocity = initialVelocity
        var lastValue = 0f
        AnimationState(
            initialValue = 0f,
            initialVelocity = initialVelocity,
        )
            .animateDecay(decayAnimationSpec) {
                val delta = value - lastValue
                val initialOffset = scrollBehaviorX.offset
                scrollBehaviorX.offset =
                    (initialOffset + delta).coerceIn(
                        minimumValue = scrollBehaviorX.offsetLimit,
                        maximumValue = 0f
                    )
                val consumed = abs(initialOffset - scrollBehaviorX.offset)
                lastValue = value
                remainingVelocity = this.velocity
                // avoid rounding errors and stop if anything is unconsumed
                if (abs(delta - consumed) > 0.5f) this.cancelAnimation()
            }

        if (snap &&
            scrollBehaviorX.offset < 0 &&
            scrollBehaviorX.offset > scrollBehaviorX.offsetLimit
        ) {
            AnimationState(initialValue = scrollBehaviorX.offset).animateTo(
                // Snap the top app bar offset to completely collapse or completely expand according
                // to the initial velocity direction.
                if (initialVelocity > 0) 0f else scrollBehaviorX.offsetLimit,
                animationSpec = tween(
                    durationMillis = TopAppBarAnimationDurationMillis,
                    easing = LinearOutSlowInEasing
                )
            ) { scrollBehaviorX.offset = value }
        }
        return Velocity(0f, remainingVelocity)
    }
    return Velocity.Zero
}

private val MediumTitleBottomPadding = 24.dp
private val LargeTitleBottomPadding = 28.dp
private val TopAppBarHorizontalPadding = 4.dp

private val AppBarHorizontalPadding = 4.dp

// A title inset when the App-Bar is a Medium or Large one. Also used to size a spacer when the
// navigation icon is missing.
private val TopAppBarTitleInset = 16.dp - TopAppBarHorizontalPadding

private const val TopAppBarAnimationDurationMillis = 500