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

import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import androidx.annotation.AttrRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

inline fun <T> Resources.Theme.getAttribute(
    @AttrRes attr: Int,
    block: (TypedArray) -> T,
): T {
    val a = obtainStyledAttributes(intArrayOf(attr))
    return block(a).also { a.recycle() }
}

fun Resources.Theme.getDrawableAttribute(@AttrRes attr: Int): Drawable =
    getAttribute(attr) { it.getDrawable(0)!! }

fun Resources.Theme.getDimensionAttribute(@AttrRes attr: Int, defaultValue: Float = 0f): Float =
    getAttribute(attr) { it.getDimension(0, defaultValue) }

fun Resources.Theme.getStringAttribute(@AttrRes attr: Int): String? =
    getAttribute(attr) { it.getString(0) }

fun Resources.Theme.getColorAttribute(@AttrRes attr: Int, defaultValue: Int = 0): Int =
    getAttribute(attr) { it.getColor(0, defaultValue) }


@Composable
fun getDimensionAttribute(@AttrRes attr: Int, defaultValue: Float = 0f): Float =
    LocalContext.current.theme.getDimensionAttribute(attr, defaultValue)

@Composable
fun getStringAttribute(@AttrRes attr: Int): String? =
    LocalContext.current.theme.getStringAttribute(attr)

@Composable
fun getColorAttribute(@AttrRes attr: Int, defaultValue: Int = 0): Int =
    LocalContext.current.theme.getColorAttribute(attr, defaultValue)
