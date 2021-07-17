/*
 * Copyright (C) 2013 The Android Open Source Project
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
 */
package github.tornaco.android.thanos.widget.checkable;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;

/**
 * A drawable that wraps two other drawables and allows flipping between them. The flipping
 * animation is a 2D rotation around the y axis.
 * <p>
 * <p/>
 * The 3 durations are: (best viewed in documentation form)
 * <pre>
 * &lt;pre&gt;[_][]|[][_]&lt;post&gt;
 *   |       |       |
 *   V       V       V
 * &lt;pre>&lt;   flip  &gt;&lt;post&gt;
 * </pre>
 */
public class FlipDrawable extends Drawable implements Drawable.Callback {

    /**
     * The inner drawables.
     */
    protected Drawable mFront;
    protected final Drawable mBack;

    protected final int mFlipDurationMs;
    protected final int mPreFlipDurationMs;
    protected final int mPostFlipDurationMs;
    private final ValueAnimator mFlipAnimator;

    private static final float END_VALUE = 2f;

    /**
     * From 0f to END_VALUE. Determines the flip progress between mFront and mBack. 0f means
     * mFront is fully shown, while END_VALUE means mBack is fully shown.
     */
    private float mFlipFraction = 0f;

    /**
     * True if flipping towards front, false if flipping towards back.
     */
    private boolean mFlipToSide = true;

    /**
     * Create a new FlipDrawable. The front is fully shown by default.
     * <p>
     * <p/>
     * The 3 durations are: (best viewed in documentation form)
     * <pre>
     * &lt;pre&gt;[_][]|[][_]&lt;post&gt;
     *   |       |       |
     *   V       V       V
     * &lt;pre>&lt;   flip  &gt;&lt;post&gt;
     * </pre>
     *
     * @param front              The front drawable.
     * @param back               The back drawable.
     * @param flipDurationMs     The duration of the actual flip. This duration includes both
     *                           animating away one side and showing the other.
     * @param preFlipDurationMs  The duration before the actual flip begins. Subclasses can use this
     *                           to add flourish.
     * @param postFlipDurationMs The duration after the actual flip begins. Subclasses can use this
     *                           to add flourish.
     */
    public FlipDrawable(final Drawable front, final Drawable back, final int flipDurationMs,
                        final int preFlipDurationMs, final int postFlipDurationMs) {
        if (front == null || back == null) {
            throw new IllegalArgumentException("Front and back drawables must not be null.");
        }
        mFront = front;
        mBack = back;

        mFront.setCallback(this);
        mBack.setCallback(this);

        mFlipDurationMs = flipDurationMs;
        mPreFlipDurationMs = preFlipDurationMs;
        mPostFlipDurationMs = postFlipDurationMs;

        mFlipAnimator = ValueAnimator.ofFloat(0f, END_VALUE)
                .setDuration(mPreFlipDurationMs + mFlipDurationMs + mPostFlipDurationMs);
        mFlipAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                final float old = mFlipFraction;
                //noinspection ConstantConditions
                mFlipFraction = (Float) animation.getAnimatedValue();
                if (old != mFlipFraction) {
                    invalidateSelf();
                }
            }
        });

        reset();
    }

    @Override
    protected void onBoundsChange(final Rect bounds) {
        super.onBoundsChange(bounds);
        if (bounds.isEmpty()) {
            mFront.setBounds(0, 0, 0, 0);
            mBack.setBounds(0, 0, 0, 0);
        } else {
            mFront.setBounds(bounds);
            mBack.setBounds(bounds);
        }
    }

    @Override
    public void draw(final Canvas canvas) {
        final Rect bounds = getBounds();
        if (!isVisible() || bounds.isEmpty()) {
            return;
        }

        final Drawable inner = getSideShown() /* == front */ ? mFront : mBack;

        final float totalDurationMs = mPreFlipDurationMs + mFlipDurationMs + mPostFlipDurationMs;

        final float scaleX;
        if (mFlipFraction / 2 <= mPreFlipDurationMs / totalDurationMs) {
            // During pre-flip.
            scaleX = 1;
        } else if (mFlipFraction / 2 >= (totalDurationMs - mPostFlipDurationMs) / totalDurationMs) {
            // During post-flip.
            scaleX = 1;
        } else {
            // During flip.
            final float flipFraction = mFlipFraction / 2;
            final float flipMiddle = (mPreFlipDurationMs / totalDurationMs
                    + (totalDurationMs - mPostFlipDurationMs) / totalDurationMs) / 2;
            final float distFraction = Math.abs(flipFraction - flipMiddle);
            final float multiplier = 1 / (flipMiddle - (mPreFlipDurationMs / totalDurationMs));
            scaleX = distFraction * multiplier;
        }

        canvas.save();
        // The flip is a simple 1 dimensional scale.
        canvas.scale(scaleX, 1, bounds.exactCenterX(), bounds.exactCenterY());
        inner.draw(canvas);
        canvas.restore();
    }

    @Override
    public void setAlpha(final int alpha) {
        mFront.setAlpha(alpha);
        mBack.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(final ColorFilter cf) {
        mFront.setColorFilter(cf);
        mBack.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return resolveOpacity(mFront.getOpacity(), mBack.getOpacity());
    }

    @Override
    protected boolean onLevelChange(final int level) {
        return mFront.setLevel(level) || mBack.setLevel(level);
    }

    @Override
    public void invalidateDrawable(final Drawable who) {
        invalidateSelf();
    }

    @Override
    public void scheduleDrawable(final Drawable who, final Runnable what, final long when) {
        scheduleSelf(what, when);
    }

    @Override
    public void unscheduleDrawable(final Drawable who, final Runnable what) {
        unscheduleSelf(what);
    }

    /**
     * Stop animating the flip and reset to one side.
     */
    public void reset() {
        final float old = mFlipFraction;
        mFlipAnimator.cancel();
        mFlipFraction = mFlipToSide ? 0f : 2f;
        if (mFlipFraction != old) {
            invalidateSelf();
        }
    }

    /**
     * Returns true if the front is shown. Returns false if the back is shown.
     */
    public boolean getSideShown() {
        final float totalDurationMs = mPreFlipDurationMs + mFlipDurationMs + mPostFlipDurationMs;
        final float middleFraction = (mPreFlipDurationMs / totalDurationMs
                + (totalDurationMs - mPostFlipDurationMs) / totalDurationMs) / 2;
        return mFlipFraction / 2 < middleFraction;
    }

    /**
     * Returns true if the front is being flipped towards. Returns false if the back is being
     * flipped towards.
     */
    public boolean getSideFlippingTowards() {
        return mFlipToSide;
    }

    /**
     * Starts an animated flip to the other side. If a flip animation is currently started,
     * it will be reversed.
     */
    public void flip() {
        mFlipToSide = !mFlipToSide;
        if (mFlipAnimator.isStarted()) {
            mFlipAnimator.reverse();
        } else {
            if (!mFlipToSide /* front to back */) {
                mFlipAnimator.start();
            } else /* back to front */ {
                mFlipAnimator.reverse();
            }
        }
    }

    /**
     * Start an animated flip to a side. This works regardless of whether a flip animation is
     * currently started.
     *
     * @param side Pass true if flip to front, false if flip to back.
     */
    public void flipTo(final boolean side) {
        if (mFlipToSide != side) {
            flip();
        }
    }

    /**
     * Returns whether flipping is in progress.
     */
    public boolean isFlipping() {
        return mFlipAnimator.isStarted();
    }

    @Override
    public boolean isStateful() {
        return mFront.isStateful() || mBack.isStateful();
    }

    @Override
    public boolean setState(int[] stateSet) {
        return mFront.setState(stateSet) || mBack.setState(stateSet);
    }

    @Override
    public void setTintMode(PorterDuff.Mode tintMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFront.setTintMode(tintMode);
            mBack.setTintMode(tintMode);
        }
    }

    @Override
    public void setTintList(ColorStateList tint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFront.setTintList(tint);
            mBack.setTintList(tint);
        }
    }
}
