package me.zhanghai.android.materialprogressbar.internal;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Build;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ValueAnimatorCompat {

    @NonNull
    private static final Object sValueAnimatorGetDurationScaleMethodLock = new Object();
    private static boolean sValueAnimatorGetDurationScaleMethodInitialized;
    @Nullable
    private static Method sValueAnimatorGetDurationScaleMethod;

    @NonNull
    private static final Object sValueAnimatorSDurationScaleFieldLock = new Object();
    private static boolean sValueAnimatorSDurationScaleFieldInitialized;
    @Nullable
    private static Field sValueAnimatorSDurationScaleField;


    private ValueAnimatorCompat() {}

    public static boolean areAnimatorsEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return ValueAnimator.areAnimatorsEnabled();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Method valueAnimatorGetDurationScaleMethod = getValueAnimatorGetDurationScaleMethod();
            if (valueAnimatorGetDurationScaleMethod != null) {
                try {
                    float durationScale = (float) valueAnimatorGetDurationScaleMethod.invoke(null);
                    return durationScale != 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Field valueAnimatorSDurationScaleField = getValueAnimatorSDurationScaleField();
            if (valueAnimatorSDurationScaleField != null) {
                try {
                    float durationScale = (float) valueAnimatorSDurationScaleField.get(null);
                    return durationScale != 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Nullable
    @SuppressLint("PrivateApi")
    private static Method getValueAnimatorGetDurationScaleMethod() {
        synchronized (sValueAnimatorGetDurationScaleMethodLock) {
            if (!sValueAnimatorGetDurationScaleMethodInitialized) {
                try {
                    //noinspection JavaReflectionMemberAccess
                    sValueAnimatorGetDurationScaleMethod = ValueAnimator.class.getDeclaredMethod(
                            "getDurationScale");
                    sValueAnimatorGetDurationScaleMethod.setAccessible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sValueAnimatorGetDurationScaleMethodInitialized = true;
            }
            return sValueAnimatorGetDurationScaleMethod;
        }
    }

    @Nullable
    private static Field getValueAnimatorSDurationScaleField() {
        synchronized (sValueAnimatorSDurationScaleFieldLock) {
            if (!sValueAnimatorSDurationScaleFieldInitialized) {
                try {
                    //noinspection JavaReflectionMemberAccess
                    sValueAnimatorSDurationScaleField = ValueAnimator.class.getDeclaredField(
                            "sDurationScale");
                    sValueAnimatorSDurationScaleField.setAccessible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sValueAnimatorSDurationScaleFieldInitialized = true;
            }
            return sValueAnimatorSDurationScaleField;
        }
    }
}
