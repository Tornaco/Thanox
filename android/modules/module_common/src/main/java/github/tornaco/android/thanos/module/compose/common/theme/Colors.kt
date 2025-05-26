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

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
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


val md_theme_light_primary = Color(0xFFB91D1B)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFFFDAD5)
val md_theme_light_onPrimaryContainer = Color(0xFF410001)
val md_theme_light_secondary = Color(0xFF775652)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFFFDAD5)
val md_theme_light_onSecondaryContainer = Color(0xFF2C1512)
val md_theme_light_tertiary = Color(0xFF00677C)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFB1EBFF)
val md_theme_light_onTertiaryContainer = Color(0xFF001F27)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFFFBFF)
val md_theme_light_onBackground = Color(0xFF201A19)
val md_theme_light_surface = Color(0xFFFFFBFF)
val md_theme_light_onSurface = Color(0xFF201A19)
val md_theme_light_surfaceVariant = Color(0xFFF5DDDA)
val md_theme_light_onSurfaceVariant = Color(0xFF534341)
val md_theme_light_outline = Color(0xFF857370)
val md_theme_light_inverseOnSurface = Color(0xFFFBEEEC)
val md_theme_light_inverseSurface = Color(0xFF362F2E)
val md_theme_light_inversePrimary = Color(0xFFFFB4AA)
val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0xFFB91D1B)
val md_theme_light_outlineVariant = Color(0xFFD8C2BF)
val md_theme_light_scrim = Color(0xFF000000)

val md_theme_dark_primary = Color(0xFFFFB4AA)
val md_theme_dark_onPrimary = Color(0xFF690004)
val md_theme_dark_primaryContainer = Color(0xFF930009)
val md_theme_dark_onPrimaryContainer = Color(0xFFFFDAD5)
val md_theme_dark_secondary = Color(0xFFE7BDB7)
val md_theme_dark_onSecondary = Color(0xFF442926)
val md_theme_dark_secondaryContainer = Color(0xFF5D3F3C)
val md_theme_dark_onSecondaryContainer = Color(0xFFFFDAD5)
val md_theme_dark_tertiary = Color(0xFF45D7FC)
val md_theme_dark_onTertiary = Color(0xFF003642)
val md_theme_dark_tertiaryContainer = Color(0xFF004E5E)
val md_theme_dark_onTertiaryContainer = Color(0xFFB1EBFF)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_background = Color(0xFF201A19)
val md_theme_dark_onBackground = Color(0xFFEDE0DE)
val md_theme_dark_surface = Color(0xFF201A19)
val md_theme_dark_onSurface = Color(0xFFEDE0DE)
val md_theme_dark_surfaceVariant = Color(0xFF534341)
val md_theme_dark_onSurfaceVariant = Color(0xFFD8C2BF)
val md_theme_dark_outline = Color(0xFFA08C8A)
val md_theme_dark_inverseOnSurface = Color(0xFF201A19)
val md_theme_dark_inverseSurface = Color(0xFFEDE0DE)
val md_theme_dark_inversePrimary = Color(0xFFB91D1B)
val md_theme_dark_shadow = Color(0xFF000000)
val md_theme_dark_surfaceTint = Color(0xFFFFB4AA)
val md_theme_dark_outlineVariant = Color(0xFF534341)
val md_theme_dark_scrim = Color(0xFF000000)