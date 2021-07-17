/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar.internal;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.Property;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;

class ObjectAnimatorCompatBase {

    // As per android.support.v4.view.animation.FastOutLinearInInterpolator.
    private static final int NUM_POINTS = 201;

    private ObjectAnimatorCompatBase() {}

    @NonNull
    public static ObjectAnimator ofArgb(@Nullable Object target, @NonNull String propertyName,
                                        int... values) {
        ObjectAnimator animator = ObjectAnimator.ofInt(target, propertyName, values);
        animator.setEvaluator(new ArgbEvaluator());
        return animator;
    }

    @NonNull
    public static <T> ObjectAnimator ofArgb(@Nullable T target,
                                            @NonNull Property<T, Integer> property, int... values) {
        ObjectAnimator animator = ObjectAnimator.ofInt(target, property, values);
        animator.setEvaluator(new ArgbEvaluator());
        return animator;
    }

    @NonNull
    public static ObjectAnimator ofFloat(@Nullable Object target, @NonNull String xPropertyName,
                                         @NonNull String yPropertyName, @NonNull Path path) {

        float[] xValues = new float[NUM_POINTS];
        float[] yValues = new float[NUM_POINTS];
        calculateXYValues(path, xValues, yValues);

        PropertyValuesHolder xPvh = PropertyValuesHolder.ofFloat(xPropertyName, xValues);
        PropertyValuesHolder yPvh = PropertyValuesHolder.ofFloat(yPropertyName, yValues);

        return ObjectAnimator.ofPropertyValuesHolder(target, xPvh, yPvh);
    }

    @NonNull
    public static <T> ObjectAnimator ofFloat(@Nullable T target,
                                             @NonNull Property<T, Float> xProperty,
                                             @NonNull Property<T, Float> yProperty,
                                             @NonNull Path path) {

        float[] xValues = new float[NUM_POINTS];
        float[] yValues = new float[NUM_POINTS];
        calculateXYValues(path, xValues, yValues);

        PropertyValuesHolder xPvh = PropertyValuesHolder.ofFloat(xProperty, xValues);
        PropertyValuesHolder yPvh = PropertyValuesHolder.ofFloat(yProperty, yValues);

        return ObjectAnimator.ofPropertyValuesHolder(target, xPvh, yPvh);
    }

    @NonNull
    public static ObjectAnimator ofInt(@Nullable Object target, @NonNull String xPropertyName,
                                       @NonNull String yPropertyName, @NonNull Path path) {

        int[] xValues = new int[NUM_POINTS];
        int[] yValues = new int[NUM_POINTS];
        calculateXYValues(path, xValues, yValues);

        PropertyValuesHolder xPvh = PropertyValuesHolder.ofInt(xPropertyName, xValues);
        PropertyValuesHolder yPvh = PropertyValuesHolder.ofInt(yPropertyName, yValues);

        return ObjectAnimator.ofPropertyValuesHolder(target, xPvh, yPvh);
    }

    @NonNull
    public static <T> ObjectAnimator ofInt(@Nullable T target,
                                           @NonNull Property<T, Integer> xProperty,
                                           @NonNull Property<T, Integer> yProperty,
                                           @NonNull Path path) {

        int[] xValues = new int[NUM_POINTS];
        int[] yValues = new int[NUM_POINTS];
        calculateXYValues(path, xValues, yValues);

        PropertyValuesHolder xPvh = PropertyValuesHolder.ofInt(xProperty, xValues);
        PropertyValuesHolder yPvh = PropertyValuesHolder.ofInt(yProperty, yValues);

        return ObjectAnimator.ofPropertyValuesHolder(target, xPvh, yPvh);
    }

    private static void calculateXYValues(@NonNull Path path,
                                          @NonNull @Size(NUM_POINTS) float[] xValues,
                                          @NonNull @Size(NUM_POINTS) float[] yValues) {

        PathMeasure pathMeasure = new PathMeasure(path, false /* forceClosed */);
        float pathLength = pathMeasure.getLength();

        float[] position = new float[2];
        for (int i = 0; i < NUM_POINTS; ++i) {
            float distance = (i * pathLength) / (NUM_POINTS - 1);
            pathMeasure.getPosTan(distance, position, null /* tangent */);
            xValues[i] = position[0];
            yValues[i] = position[1];
        }
    }

    private static void calculateXYValues(@NonNull Path path,
                                          @NonNull @Size(NUM_POINTS) int[] xValues,
                                          @NonNull @Size(NUM_POINTS) int[] yValues) {

        PathMeasure pathMeasure = new PathMeasure(path, false /* forceClosed */);
        float pathLength = pathMeasure.getLength();

        float[] position = new float[2];
        for (int i = 0; i < NUM_POINTS; ++i) {
            float distance = (i * pathLength) / (NUM_POINTS - 1);
            pathMeasure.getPosTan(distance, position, null /* tangent */);
            xValues[i] = Math.round(position[0]);
            yValues[i] = Math.round(position[1]);
        }
    }
}
