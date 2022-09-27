package com.godaddy.android.colorpicker

import kotlin.math.PI

internal fun Float.toRadian(): Float = this / 180.0f * PI.toFloat()
internal fun Double.toRadian(): Double = this / 180 * PI
internal fun Float.toDegree(): Float = this * 180.0f / PI.toFloat()
internal fun Double.toDegree(): Double = this * 180 / PI
