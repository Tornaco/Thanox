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
    fun backgroundSurfaceColor() = MaterialTheme.colorScheme.applyTonalElevation(
        MaterialTheme.colorScheme.surface,
        2.dp
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