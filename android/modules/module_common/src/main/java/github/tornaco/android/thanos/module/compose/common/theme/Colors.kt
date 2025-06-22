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

package github.tornaco.android.thanos.module.compose.common.theme

import androidx.annotation.ColorInt
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.ln

object ColorDefaults {
    @Composable
    fun backgroundSurfaceColor(size: Dp = 2.dp) = MaterialTheme.colorScheme.applyTonalElevation(
        MaterialTheme.colorScheme.surface,
        size
    )

    private fun ColorScheme.applyTonalElevation(backgroundColor: Color, elevation: Dp): Color {
        return if (backgroundColor == surface) {
            surfaceColorAtElevation(elevation)
        } else {
            backgroundColor
        }
    }

    /**
     * Returns the [ColorScheme.surface] color with an alpha of the [ColorScheme.primary] color overlaid
     * on top of it.
     * Computes the surface tonal color at different elevation levels e.g. surface1 through surface5.
     *
     * @param elevation Elevation value used to compute alpha of the color overlay layer.
     */
    private fun ColorScheme.surfaceColorAtElevation(
        elevation: Dp,
    ): Color {
        if (elevation == 0.dp) return surface
        val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
        return primary.copy(alpha = alpha).compositeOver(surface)
    }
}


val primaryLight = Color(0xFF4D5C92)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFFDCE1FF)
val onPrimaryContainerLight = Color(0xFF354479)
val secondaryLight = Color(0xFF595D72)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFDEE1F9)
val onSecondaryContainerLight = Color(0xFF424659)
val tertiaryLight = Color(0xFF75546F)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFFFD7F5)
val onTertiaryContainerLight = Color(0xFF5B3D57)
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF93000A)
val backgroundLight = Color(0xFFFAF8FF)
val onBackgroundLight = Color(0xFF1A1B21)
val surfaceLight = Color(0xFFFAF8FF)
val onSurfaceLight = Color(0xFF1A1B21)
val surfaceVariantLight = Color(0xFFE2E1EC)
val onSurfaceVariantLight = Color(0xFF45464F)
val outlineLight = Color(0xFF767680)
val outlineVariantLight = Color(0xFFC6C5D0)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF2F3036)
val inverseOnSurfaceLight = Color(0xFFF1F0F7)
val inversePrimaryLight = Color(0xFFB6C4FF)
val surfaceDimLight = Color(0xFFDAD9E0)
val surfaceBrightLight = Color(0xFFFAF8FF)
val surfaceContainerLowestLight = Color(0xFFFFFFFF)
val surfaceContainerLowLight = Color(0xFFF4F3FA)
val surfaceContainerLight = Color(0xFFEFEDF4)
val surfaceContainerHighLight = Color(0xFFE9E7EF)
val surfaceContainerHighestLight = Color(0xFFE3E1E9)

val primaryLightMediumContrast = Color(0xFF243367)
val onPrimaryLightMediumContrast = Color(0xFFFFFFFF)
val primaryContainerLightMediumContrast = Color(0xFF5C6AA2)
val onPrimaryContainerLightMediumContrast = Color(0xFFFFFFFF)
val secondaryLightMediumContrast = Color(0xFF313548)
val onSecondaryLightMediumContrast = Color(0xFFFFFFFF)
val secondaryContainerLightMediumContrast = Color(0xFF686C81)
val onSecondaryContainerLightMediumContrast = Color(0xFFFFFFFF)
val tertiaryLightMediumContrast = Color(0xFF492C45)
val onTertiaryLightMediumContrast = Color(0xFFFFFFFF)
val tertiaryContainerLightMediumContrast = Color(0xFF85627E)
val onTertiaryContainerLightMediumContrast = Color(0xFFFFFFFF)
val errorLightMediumContrast = Color(0xFF740006)
val onErrorLightMediumContrast = Color(0xFFFFFFFF)
val errorContainerLightMediumContrast = Color(0xFFCF2C27)
val onErrorContainerLightMediumContrast = Color(0xFFFFFFFF)
val backgroundLightMediumContrast = Color(0xFFFAF8FF)
val onBackgroundLightMediumContrast = Color(0xFF1A1B21)
val surfaceLightMediumContrast = Color(0xFFFAF8FF)
val onSurfaceLightMediumContrast = Color(0xFF101116)
val surfaceVariantLightMediumContrast = Color(0xFFE2E1EC)
val onSurfaceVariantLightMediumContrast = Color(0xFF34363E)
val outlineLightMediumContrast = Color(0xFF51525B)
val outlineVariantLightMediumContrast = Color(0xFF6C6C76)
val scrimLightMediumContrast = Color(0xFF000000)
val inverseSurfaceLightMediumContrast = Color(0xFF2F3036)
val inverseOnSurfaceLightMediumContrast = Color(0xFFF1F0F7)
val inversePrimaryLightMediumContrast = Color(0xFFB6C4FF)
val surfaceDimLightMediumContrast = Color(0xFFC7C6CD)
val surfaceBrightLightMediumContrast = Color(0xFFFAF8FF)
val surfaceContainerLowestLightMediumContrast = Color(0xFFFFFFFF)
val surfaceContainerLowLightMediumContrast = Color(0xFFF4F3FA)
val surfaceContainerLightMediumContrast = Color(0xFFE9E7EF)
val surfaceContainerHighLightMediumContrast = Color(0xFFDDDCE3)
val surfaceContainerHighestLightMediumContrast = Color(0xFFD2D1D8)

val primaryLightHighContrast = Color(0xFF19285C)
val onPrimaryLightHighContrast = Color(0xFFFFFFFF)
val primaryContainerLightHighContrast = Color(0xFF38467B)
val onPrimaryContainerLightHighContrast = Color(0xFFFFFFFF)
val secondaryLightHighContrast = Color(0xFF272B3D)
val onSecondaryLightHighContrast = Color(0xFFFFFFFF)
val secondaryContainerLightHighContrast = Color(0xFF44485C)
val onSecondaryContainerLightHighContrast = Color(0xFFFFFFFF)
val tertiaryLightHighContrast = Color(0xFF3E223B)
val onTertiaryLightHighContrast = Color(0xFFFFFFFF)
val tertiaryContainerLightHighContrast = Color(0xFF5E3F59)
val onTertiaryContainerLightHighContrast = Color(0xFFFFFFFF)
val errorLightHighContrast = Color(0xFF600004)
val onErrorLightHighContrast = Color(0xFFFFFFFF)
val errorContainerLightHighContrast = Color(0xFF98000A)
val onErrorContainerLightHighContrast = Color(0xFFFFFFFF)
val backgroundLightHighContrast = Color(0xFFFAF8FF)
val onBackgroundLightHighContrast = Color(0xFF1A1B21)
val surfaceLightHighContrast = Color(0xFFFAF8FF)
val onSurfaceLightHighContrast = Color(0xFF000000)
val surfaceVariantLightHighContrast = Color(0xFFE2E1EC)
val onSurfaceVariantLightHighContrast = Color(0xFF000000)
val outlineLightHighContrast = Color(0xFF2A2C34)
val outlineVariantLightHighContrast = Color(0xFF484951)
val scrimLightHighContrast = Color(0xFF000000)
val inverseSurfaceLightHighContrast = Color(0xFF2F3036)
val inverseOnSurfaceLightHighContrast = Color(0xFFFFFFFF)
val inversePrimaryLightHighContrast = Color(0xFFB6C4FF)
val surfaceDimLightHighContrast = Color(0xFFB9B8BF)
val surfaceBrightLightHighContrast = Color(0xFFFAF8FF)
val surfaceContainerLowestLightHighContrast = Color(0xFFFFFFFF)
val surfaceContainerLowLightHighContrast = Color(0xFFF1F0F7)
val surfaceContainerLightHighContrast = Color(0xFFE3E1E9)
val surfaceContainerHighLightHighContrast = Color(0xFFD5D3DB)
val surfaceContainerHighestLightHighContrast = Color(0xFFC7C6CD)

val primaryDark = Color(0xFFB6C4FF)
val onPrimaryDark = Color(0xFF1E2D61)
val primaryContainerDark = Color(0xFF354479)
val onPrimaryContainerDark = Color(0xFFDCE1FF)
val secondaryDark = Color(0xFFC2C5DD)
val onSecondaryDark = Color(0xFF2B3042)
val secondaryContainerDark = Color(0xFF424659)
val onSecondaryContainerDark = Color(0xFFDEE1F9)
val tertiaryDark = Color(0xFFE3BADA)
val onTertiaryDark = Color(0xFF43273F)
val tertiaryContainerDark = Color(0xFF5B3D57)
val onTertiaryContainerDark = Color(0xFFFFD7F5)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val backgroundDark = Color(0xFF121318)
val onBackgroundDark = Color(0xFFE3E1E9)
val surfaceDark = Color(0xFF121318)
val onSurfaceDark = Color(0xFFE3E1E9)
val surfaceVariantDark = Color(0xFF45464F)
val onSurfaceVariantDark = Color(0xFFC6C5D0)
val outlineDark = Color(0xFF90909A)
val outlineVariantDark = Color(0xFF45464F)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFE3E1E9)
val inverseOnSurfaceDark = Color(0xFF2F3036)
val inversePrimaryDark = Color(0xFF4D5C92)
val surfaceDimDark = Color(0xFF121318)
val surfaceBrightDark = Color(0xFF38393F)
val surfaceContainerLowestDark = Color(0xFF0D0E13)
val surfaceContainerLowDark = Color(0xFF1A1B21)
val surfaceContainerDark = Color(0xFF1E1F25)
val surfaceContainerHighDark = Color(0xFF292A2F)
val surfaceContainerHighestDark = Color(0xFF34343A)

val primaryDarkMediumContrast = Color(0xFFD4DBFF)
val onPrimaryDarkMediumContrast = Color(0xFF112255)
val primaryContainerDarkMediumContrast = Color(0xFF808EC8)
val onPrimaryContainerDarkMediumContrast = Color(0xFF000000)
val secondaryDarkMediumContrast = Color(0xFFD8DBF3)
val onSecondaryDarkMediumContrast = Color(0xFF202536)
val secondaryContainerDarkMediumContrast = Color(0xFF8C90A6)
val onSecondaryContainerDarkMediumContrast = Color(0xFF000000)
val tertiaryDarkMediumContrast = Color(0xFFFAD0F0)
val onTertiaryDarkMediumContrast = Color(0xFF371C34)
val tertiaryContainerDarkMediumContrast = Color(0xFFAA85A3)
val onTertiaryContainerDarkMediumContrast = Color(0xFF000000)
val errorDarkMediumContrast = Color(0xFFFFD2CC)
val onErrorDarkMediumContrast = Color(0xFF540003)
val errorContainerDarkMediumContrast = Color(0xFFFF5449)
val onErrorContainerDarkMediumContrast = Color(0xFF000000)
val backgroundDarkMediumContrast = Color(0xFF121318)
val onBackgroundDarkMediumContrast = Color(0xFFE3E1E9)
val surfaceDarkMediumContrast = Color(0xFF121318)
val onSurfaceDarkMediumContrast = Color(0xFFFFFFFF)
val surfaceVariantDarkMediumContrast = Color(0xFF45464F)
val onSurfaceVariantDarkMediumContrast = Color(0xFFDCDBE6)
val outlineDarkMediumContrast = Color(0xFFB1B1BB)
val outlineVariantDarkMediumContrast = Color(0xFF8F8F99)
val scrimDarkMediumContrast = Color(0xFF000000)
val inverseSurfaceDarkMediumContrast = Color(0xFFE3E1E9)
val inverseOnSurfaceDarkMediumContrast = Color(0xFF292A2F)
val inversePrimaryDarkMediumContrast = Color(0xFF36457A)
val surfaceDimDarkMediumContrast = Color(0xFF121318)
val surfaceBrightDarkMediumContrast = Color(0xFF44444A)
val surfaceContainerLowestDarkMediumContrast = Color(0xFF06070C)
val surfaceContainerLowDarkMediumContrast = Color(0xFF1C1D23)
val surfaceContainerDarkMediumContrast = Color(0xFF27272D)
val surfaceContainerHighDarkMediumContrast = Color(0xFF323238)
val surfaceContainerHighestDarkMediumContrast = Color(0xFF3D3D43)

val primaryDarkHighContrast = Color(0xFFEEEFFF)
val onPrimaryDarkHighContrast = Color(0xFF000000)
val primaryContainerDarkHighContrast = Color(0xFFB2C0FD)
val onPrimaryContainerDarkHighContrast = Color(0xFF00082B)
val secondaryDarkHighContrast = Color(0xFFEEEFFF)
val onSecondaryDarkHighContrast = Color(0xFF000000)
val secondaryContainerDarkHighContrast = Color(0xFFBEC1D9)
val onSecondaryContainerDarkHighContrast = Color(0xFF060A1B)
val tertiaryDarkHighContrast = Color(0xFFFFEAF7)
val onTertiaryDarkHighContrast = Color(0xFF000000)
val tertiaryContainerDarkHighContrast = Color(0xFFDFB6D6)
val onTertiaryContainerDarkHighContrast = Color(0xFF190318)
val errorDarkHighContrast = Color(0xFFFFECE9)
val onErrorDarkHighContrast = Color(0xFF000000)
val errorContainerDarkHighContrast = Color(0xFFFFAEA4)
val onErrorContainerDarkHighContrast = Color(0xFF220001)
val backgroundDarkHighContrast = Color(0xFF121318)
val onBackgroundDarkHighContrast = Color(0xFFE3E1E9)
val surfaceDarkHighContrast = Color(0xFF121318)
val onSurfaceDarkHighContrast = Color(0xFFFFFFFF)
val surfaceVariantDarkHighContrast = Color(0xFF45464F)
val onSurfaceVariantDarkHighContrast = Color(0xFFFFFFFF)
val outlineDarkHighContrast = Color(0xFFF0EFFA)
val outlineVariantDarkHighContrast = Color(0xFFC2C2CC)
val scrimDarkHighContrast = Color(0xFF000000)
val inverseSurfaceDarkHighContrast = Color(0xFFE3E1E9)
val inverseOnSurfaceDarkHighContrast = Color(0xFF000000)
val inversePrimaryDarkHighContrast = Color(0xFF36457A)
val surfaceDimDarkHighContrast = Color(0xFF121318)
val surfaceBrightDarkHighContrast = Color(0xFF4F5056)
val surfaceContainerLowestDarkHighContrast = Color(0xFF000000)
val surfaceContainerLowDarkHighContrast = Color(0xFF1E1F25)
val surfaceContainerDarkHighContrast = Color(0xFF2F3036)
val surfaceContainerHighDarkHighContrast = Color(0xFF3A3B41)
val surfaceContainerHighestDarkHighContrast = Color(0xFF46464C)

@Composable
fun themedTextColor(color: Color): Color {
    val isDarkTheme = isSystemInDarkTheme()
    return if (isDarkTheme) Color.Unspecified else darkenColor(color)
}

@ColorInt
fun darkenColor(color: Color, factor: Float = 0.27f): Color {
    return darkenColor(color.toArgb(), factor).toComposeColor()
}

@ColorInt
fun darkenColor(@ColorInt color: Int, factor: Float): Int {
    return android.graphics.Color.HSVToColor(FloatArray(3).apply {
        android.graphics.Color.colorToHSV(color, this)
        this[2] *= factor
    })
}

fun Int.toComposeColor(): Color {
    return Color(this)
}

val NoteTextColor = Color(0xFF777777)
val VipColorGold = Color(0XFFFF8F00)
val WeChatGreen = Color(0XFF4CAF50)
val AliBlue = Color(0XFF2962ff)
val PayPalBlue = Color(0XFF0277BD)
val VerifiedGreen = Color(0XFF43a047)
val ExperimentalYellow = Color(0xFFF8B62E)
val GoogleBlue = Color(0xFF2962ff)
val ThanoxBlue = Color(0xFF2962ff)