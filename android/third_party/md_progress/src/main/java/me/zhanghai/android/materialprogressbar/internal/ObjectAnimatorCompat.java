/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar.internal;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.os.Build;
import android.util.Property;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Helper for accessing features in {@link ObjectAnimator} introduced after API level 11 (for
 * {@link android.animation.PropertyValuesHolder}) in a backward compatible fashion.
 */
public class ObjectAnimatorCompat {

    private ObjectAnimatorCompat() {}

    /**
     * Constructs and returns an ObjectAnimator that animates between color values. A single
     * value implies that that value is the one being animated to. Two values imply starting
     * and ending values. More than two values imply a starting value, values to animate through
     * along the way, and an ending value (these values will be distributed evenly across
     * the duration of the animation).
     *
     * @param target The object whose property is to be animated. This object should have a public
     *               method on it called {@code setName()}, where {@code name} is the value of the
     *               {@code propertyName} parameter.
     * @param propertyName The name of the property being animated.
     * @param values A set of values that the animation will animate between over time.
     * @return An ObjectAnimator object that is set up to animate between the given values.
     */
    @NonNull
    public static ObjectAnimator ofArgb(@Nullable Object target, @NonNull String propertyName,
                                        int... values) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ObjectAnimatorCompatLollipop.ofArgb(target, propertyName, values);
        } else {
            return ObjectAnimatorCompatBase.ofArgb(target, propertyName, values);
        }
    }

    /**
     * Constructs and returns an ObjectAnimator that animates between color values. A single
     * value implies that that value is the one being animated to. Two values imply starting
     * and ending values. More than two values imply a starting value, values to animate through
     * along the way, and an ending value (these values will be distributed evenly across
     * the duration of the animation).
     *
     * @param target The object whose property is to be animated.
     * @param property The property being animated.
     * @param values A set of values that the animation will animate between over time.
     * @return An ObjectAnimator object that is set up to animate between the given values.
     */
    @NonNull
    public static <T> ObjectAnimator ofArgb(@Nullable T target,
                                            @NonNull Property<T, Integer> property, int... values) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ObjectAnimatorCompatLollipop.ofArgb(target, property, values);
        } else {
            return ObjectAnimatorCompatBase.ofArgb(target, property, values);
        }
    }

    /**
     * Constructs and returns an ObjectAnimator that animates coordinates along a {@code Path} using
     * two properties. A {@code Path} animation moves in two dimensions, animating coordinates
     * {@code (x, y)} together to follow the line. In this variation, the coordinates are floats
     * that are set to separate properties designated by {@code xPropertyName} and
     * {@code yPropertyName}.
     *
     * @param target The object whose properties are to be animated. This object should have public
     *               methods on it called {@code setNameX()} and {@code setNameY}, where
     *               {@code nameX} and {@code nameY} are the value of the {@code xPropertyName} and
     *               {@code yPropertyName} parameters, respectively.
     * @param xPropertyName The name of the property for the x coordinate being animated.
     * @param yPropertyName The name of the property for the y coordinate being animated.
     * @param path The {@code Path} to animate values along.
     * @return An ObjectAnimator object that is set up to animate along {@code path}.
     */
    @NonNull
    public static ObjectAnimator ofFloat(@Nullable Object target, @NonNull String xPropertyName,
                                         @NonNull String yPropertyName, @NonNull Path path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ObjectAnimatorCompatLollipop.ofFloat(target, xPropertyName, yPropertyName, path);
        } else {
            return ObjectAnimatorCompatBase.ofFloat(target, xPropertyName, yPropertyName, path);
        }
    }

    /**
     * Constructs and returns an ObjectAnimator that animates coordinates along a {@code Path} using
     * two properties. A {@code Path} animation moves in two dimensions, animating coordinates
     * {@code (x, y)} together to follow the line. In this variation, the coordinates are floats
     * that are set to separate properties, {@code xProperty} and {@code yProperty}.
     *
     * @param target The object whose properties are to be animated.
     * @param xProperty The property for the x coordinate being animated.
     * @param yProperty The property for the y coordinate being animated.
     * @param path The {@code Path} to animate values along.
     * @return An ObjectAnimator object that is set up to animate along {@code path}.
     */
    @NonNull
    public static <T> ObjectAnimator ofFloat(@Nullable T target,
                                             @NonNull Property<T, Float> xProperty,
                                             @NonNull Property<T, Float> yProperty,
                                             @NonNull Path path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ObjectAnimatorCompatLollipop.ofFloat(target, xProperty, yProperty, path);
        } else {
            return ObjectAnimatorCompatBase.ofFloat(target, xProperty, yProperty, path);
        }
    }

    /**
     * Constructs and returns an ObjectAnimator that animates coordinates along a {@code Path} using
     * two properties. A {@code Path} animation moves in two dimensions, animating coordinates
     * {@code (x, y)} together to follow the line. In this variation, the coordinates are integers
     * that are set to separate properties designated by {@code xPropertyName} and
     * {@code yPropertyName}.
     *
     * @param target The object whose properties are to be animated. This object should have public
     *               methods on it called {@code setNameX()} and {@code setNameY}, where
     *               {@code nameX} and {@code nameY} are the value of {@code xPropertyName} and
     *               {@code yPropertyName} parameters, respectively.
     * @param xPropertyName The name of the property for the x coordinate being animated.
     * @param yPropertyName The name of the property for the y coordinate being animated.
     * @param path The {@code Path} to animate values along.
     * @return An ObjectAnimator object that is set up to animate along {@code path}.
     */
    @NonNull
    public static ObjectAnimator ofInt(@Nullable Object target, @NonNull String xPropertyName,
                                       @NonNull String yPropertyName, @NonNull Path path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ObjectAnimatorCompatLollipop.ofInt(target, xPropertyName, yPropertyName, path);
        } else {
            return ObjectAnimatorCompatBase.ofInt(target, xPropertyName, yPropertyName, path);
        }
    }

    /**
     * Constructs and returns an ObjectAnimator that animates coordinates along a {@code Path} using
     * two properties. A {@code Path} animation moves in two dimensions, animating coordinates
     * {@code (x, y)} together to follow the line. In this variation, the coordinates are integers
     * that are set to separate properties, {@code xProperty} and {@code yProperty}.
     *
     * @param target The object whose properties are to be animated.
     * @param xProperty The property for the x coordinate being animated.
     * @param yProperty The property for the y coordinate being animated.
     * @param path The {@code Path} to animate values along.
     * @return An ObjectAnimator object that is set up to animate along {@code path}.
     */
    @NonNull
    public static <T> ObjectAnimator ofInt(@Nullable T target,
                                           @NonNull Property<T, Integer> xProperty,
                                           @NonNull Property<T, Integer> yProperty,
                                           @NonNull Path path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ObjectAnimatorCompatLollipop.ofInt(target, xProperty, yProperty, path);
        } else {
            return ObjectAnimatorCompatBase.ofInt(target, xProperty, yProperty, path);
        }
    }
}
