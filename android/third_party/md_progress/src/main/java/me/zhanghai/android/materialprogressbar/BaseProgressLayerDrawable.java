/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import me.zhanghai.android.materialprogressbar.internal.ThemeUtils;

class BaseProgressLayerDrawable<
        ProgressDrawableType extends IntrinsicPaddingDrawable & ShowBackgroundDrawable
                & TintableDrawable, BackgroundDrawableType extends IntrinsicPaddingDrawable
                & ShowBackgroundDrawable & TintableDrawable>
        extends LayerDrawable implements IntrinsicPaddingDrawable, MaterialProgressDrawable,
        ShowBackgroundDrawable, TintableDrawable {

    @FloatRange(from = 0, to = 1)
    private float mBackgroundAlpha;

    @NonNull
    private final BackgroundDrawableType mBackgroundDrawable;
    @NonNull
    private final ProgressDrawableType mSecondaryProgressDrawable;
    @NonNull
    private final ProgressDrawableType mProgressDrawable;

    public BaseProgressLayerDrawable(@NonNull Drawable[] layers, @NonNull Context context) {
        super(layers);

        mBackgroundAlpha = ThemeUtils.getFloatFromAttrRes(android.R.attr.disabledAlpha, 0, context);

        setId(0, android.R.id.background);
        //noinspection unchecked
        mBackgroundDrawable = (BackgroundDrawableType) getDrawable(0);
        setId(1, android.R.id.secondaryProgress);
        //noinspection unchecked
        mSecondaryProgressDrawable = (ProgressDrawableType) getDrawable(1);
        setId(2, android.R.id.progress);
        //noinspection unchecked
        mProgressDrawable = (ProgressDrawableType) getDrawable(2);

        int controlActivatedColor = ThemeUtils.getColorFromAttrRes(R.attr.colorControlActivated,
                Color.BLACK, context);
        setTint(controlActivatedColor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getShowBackground() {
        return mBackgroundDrawable.getShowBackground();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setShowBackground(boolean show) {
        if (mBackgroundDrawable.getShowBackground() != show) {
            mBackgroundDrawable.setShowBackground(show);
            mSecondaryProgressDrawable.setShowBackground(!show);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getUseIntrinsicPadding() {
        return mBackgroundDrawable.getUseIntrinsicPadding();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUseIntrinsicPadding(boolean useIntrinsicPadding) {
        mBackgroundDrawable.setUseIntrinsicPadding(useIntrinsicPadding);
        mSecondaryProgressDrawable.setUseIntrinsicPadding(useIntrinsicPadding);
        mProgressDrawable.setUseIntrinsicPadding(useIntrinsicPadding);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressLint("NewApi")
    public void setTint(@ColorInt int tintColor) {
        // Modulate alpha of tintColor against mBackgroundAlpha.
        int backgroundTintColor = ColorUtils.setAlphaComponent(tintColor, Math.round(Color.alpha(
                tintColor) * mBackgroundAlpha));
        mBackgroundDrawable.setTint(backgroundTintColor);
        mSecondaryProgressDrawable.setTint(backgroundTintColor);
        mProgressDrawable.setTint(tintColor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressLint("NewApi")
    public void setTintList(@Nullable ColorStateList tint) {
        ColorStateList backgroundTint;
        if (tint != null) {
            if (!tint.isOpaque()) {
                Log.w(getClass().getSimpleName(), "setTintList() called with a non-opaque" +
                        " ColorStateList, its original alpha will be discarded");
            }
            backgroundTint = tint.withAlpha(Math.round(mBackgroundAlpha * 255));
        } else {
            backgroundTint = null;
        }
        mBackgroundDrawable.setTintList(backgroundTint);
        mSecondaryProgressDrawable.setTintList(backgroundTint);
        mProgressDrawable.setTintList(tint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressLint("NewApi")
    public void setTintMode(@NonNull PorterDuff.Mode tintMode) {
        mBackgroundDrawable.setTintMode(tintMode);
        mSecondaryProgressDrawable.setTintMode(tintMode);
        mProgressDrawable.setTintMode(tintMode);
    }
}
