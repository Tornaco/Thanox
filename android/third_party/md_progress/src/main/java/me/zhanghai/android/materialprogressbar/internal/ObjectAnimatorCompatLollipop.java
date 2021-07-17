/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar.internal;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.graphics.Path;
import android.os.Build;
import android.util.Property;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class ObjectAnimatorCompatLollipop {

    private ObjectAnimatorCompatLollipop() {}

    @NonNull
    public static ObjectAnimator ofArgb(@Nullable Object target, @NonNull String propertyName,
                                        int... values) {
        return ObjectAnimator.ofArgb(target, propertyName, values);
    }

    @NonNull
    public static <T> ObjectAnimator ofArgb(@Nullable T target,
                                            @NonNull Property<T, Integer> property, int... values) {
        return ObjectAnimator.ofArgb(target, property, values);
    }

    @NonNull
    public static ObjectAnimator ofFloat(@Nullable Object target, @NonNull String xPropertyName,
                                         @NonNull String yPropertyName, @NonNull Path path) {
        return ObjectAnimator.ofFloat(target, xPropertyName, yPropertyName, path);
    }

    @NonNull
    public static <T> ObjectAnimator ofFloat(@Nullable T target,
                                             @NonNull Property<T, Float> xProperty,
                                             @NonNull Property<T, Float> yProperty,
                                             @NonNull Path path) {
        return ObjectAnimator.ofFloat(target, xProperty, yProperty, path);
    }

    @NonNull
    public static ObjectAnimator ofInt(@Nullable Object target, @NonNull String xPropertyName,
                                       @NonNull String yPropertyName, @NonNull Path path) {
        return ObjectAnimator.ofInt(target, xPropertyName, yPropertyName, path);
    }

    @NonNull
    public static <T> ObjectAnimator ofInt(@Nullable T target,
                                           @NonNull Property<T, Integer> xProperty,
                                           @NonNull Property<T, Integer> yProperty,
                                           @NonNull Path path) {
        return ObjectAnimator.ofInt(target, xProperty, yProperty, path);
    }
}

