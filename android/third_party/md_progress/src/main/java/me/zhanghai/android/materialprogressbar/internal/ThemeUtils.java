/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar.internal;

import android.content.Context;
import android.content.res.TypedArray;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

public class ThemeUtils {

    private ThemeUtils() {}

    @ColorInt
    public static int getColorFromAttrRes(@AttrRes int attrRes, @ColorInt int defaultValue,
                                          @NonNull Context context) {
        TypedArray a = context.obtainStyledAttributes(new int[] { attrRes });
        try {
            return a.getColor(0, defaultValue);
        } finally {
            a.recycle();
        }
    }

    public static float getFloatFromAttrRes(@AttrRes int attrRes, float defaultValue,
                                            @NonNull Context context) {
        TypedArray a = context.obtainStyledAttributes(new int[] { attrRes });
        try {
            return a.getFloat(0, defaultValue);
        } finally {
            a.recycle();
        }
    }
}
