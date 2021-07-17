/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

/**
 * A new {@code Drawable} for determinate circular {@code ProgressBar}.
 */
public class CircularProgressDrawable extends BaseProgressLayerDrawable<
        SingleCircularProgressDrawable, CircularProgressBackgroundDrawable> {

    /**
     * Create a new {@code CircularProgressDrawable}.
     *
     * @param context the {@code Context} for retrieving style information.
     */
    public CircularProgressDrawable(int style, @NonNull Context context) {
        super(new Drawable[] {
                new CircularProgressBackgroundDrawable(),
                new SingleCircularProgressDrawable(style),
                new SingleCircularProgressDrawable(style),
        }, context);
    }
}
