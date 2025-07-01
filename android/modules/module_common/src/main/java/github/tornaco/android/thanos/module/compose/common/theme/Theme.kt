@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package github.tornaco.android.thanos.module.compose.common.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.core.util.OsUtils

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)


@Composable
fun ThanoxExpressiveTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    disableDynamicTheming: Boolean = false,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current

    // Dynamic color is available on Android 12+
    val applyDynamicColor: Boolean = !disableDynamicTheming && OsUtils.isSOrAbove()

    XLog.d("applyDynamicColor: $applyDynamicColor")

    val colorScheme = when {
        applyDynamicColor -> {
            if (darkTheme) {
                dynamicDarkColorScheme(context).copy(
                    background = Color.Black,
                    surface = Color.Black,
                )
            } else {
                dynamicLightColorScheme(context).copy(
                    background = Color(0xfff5f5f5),
                    surface = Color(0xfff5f5f5),
                )
            }
        }

        darkTheme -> darkScheme.copy(
            background = Color.Black,
            surface = Color.Black,
        )

        else -> lightScheme.copy(
            background = Color(0xfff5f5f5),
            surface = Color(0xfff5f5f5),
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            // Maybe a Dialog.
            val window = (view.context as? Activity)?.window
            window?.let {
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                    !darkTheme
            }
        }
    }

    val thanoxColorScheme = when {
        darkTheme -> ThanoxColorScheme(
            cardBgColor = Color(0x72555555),
            isDarkTheme = true
        )

        else -> ThanoxColorScheme(
            cardBgColor = Color(0xeaffffff),
            isDarkTheme = false
        )
    }

    val shapes = Shapes(largeIncreased = RoundedCornerShape(36.0.dp))
    CompositionLocalProvider(LocalThanoxColorSchema provides thanoxColorScheme) {
        MaterialExpressiveTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = shapes,
        ) {
            content()
        }
    }
}

val LocalThanoxColorSchema = staticCompositionLocalOf<ThanoxColorScheme> {
    noLocalProvidedFor("LocalShortXColorSchema")
}

private fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}

@Stable
data class ThanoxColorScheme(
    val cardBgColor: Color,
    val isDarkTheme: Boolean
)