/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar.internal;

import android.graphics.PorterDuff;

import androidx.annotation.Nullable;

public class DrawableCompat {

    /**
     * Parses a {@link android.graphics.PorterDuff.Mode} from a tintMode attribute's enum value.
     */
    public static @Nullable PorterDuff.Mode parseTintMode(int value,
                                                          @Nullable PorterDuff.Mode defaultMode) {
        switch (value) {
            case 3: return PorterDuff.Mode.SRC_OVER;
            case 5: return PorterDuff.Mode.SRC_IN;
            case 9: return PorterDuff.Mode.SRC_ATOP;
            case 14: return PorterDuff.Mode.MULTIPLY;
            case 15: return PorterDuff.Mode.SCREEN;
            case 16: return PorterDuff.Mode.ADD;
            default: return defaultMode;
        }
    }
}
