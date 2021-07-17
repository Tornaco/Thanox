package github.tornaco.android.thanos.widget.checkable;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;

import github.tornaco.android.thanos.module.common.R;
import github.tornaco.android.thanos.util.BitmapUtil;

public class CheckableFlipDrawable extends FlipDrawable implements
        ValueAnimator.AnimatorUpdateListener {

    private final FrontDrawable mFrontDrawable;
    private final CheckMarkDrawable mCheckMarkDrawable;

    private final ValueAnimator mCheckmarkScaleAnimator;
    private final ValueAnimator mCheckmarkAlphaAnimator;

    private static final int POST_FLIP_DURATION_MS = 150;

    private static final float CHECKMARK_SCALE_BEGIN_VALUE = 0.2f;
    private static final float CHECKMARK_ALPHA_BEGIN_VALUE = 0f;

    /**
     * Must be <= 1f since the animation value is used as a percentage.
     */
    private static final float END_VALUE = 1f;

    public CheckableFlipDrawable(Context context,
                                 Drawable front,
                                 final int checkBackgroundColor,
                                 final int flipDurationMs) {
        super(new FrontDrawable(front), new CheckMarkDrawable(context, checkBackgroundColor),
                flipDurationMs, 0 /* preFlipDurationMs */, POST_FLIP_DURATION_MS);

        mFrontDrawable = (FrontDrawable) mFront;
        mCheckMarkDrawable = (CheckMarkDrawable) mBack;

        // We will ok checkmark animations that are synchronized with the
        // flipping animation. The entire delay + duration of the checkmark animation
        // needs to equal the entire duration of the flip animation (where delay is 0).

        // The checkmark animation is in effect only when the back drawable is being shown.
        // For the flip animation duration    <pre>[_][]|[][_]<post>
        // The checkmark animation will be    |--delay--|-duration-|

        // Need delay to skip the first half of the flip duration.
        final long animationDelay = mPreFlipDurationMs + mFlipDurationMs / 2;
        // Actual duration is the second half of the flip duration.
        final long animationDuration = mFlipDurationMs / 2 + mPostFlipDurationMs;

        mCheckmarkScaleAnimator = ValueAnimator.ofFloat(CHECKMARK_SCALE_BEGIN_VALUE, END_VALUE)
                .setDuration(animationDuration);
        mCheckmarkScaleAnimator.setStartDelay(animationDelay);
        mCheckmarkScaleAnimator.addUpdateListener(this);

        mCheckmarkAlphaAnimator = ValueAnimator.ofFloat(CHECKMARK_ALPHA_BEGIN_VALUE, END_VALUE)
                .setDuration(animationDuration);
        mCheckmarkAlphaAnimator.setStartDelay(animationDelay);
        mCheckmarkAlphaAnimator.addUpdateListener(this);
    }

    public void setFront(Drawable front) {
        mFrontDrawable.setInnerDrawable(front);
        invalidateSelf();
    }

    public void setCheckMarkBackgroundColor(int color) {
        mCheckMarkDrawable.setBackgroundColor(color);
        invalidateSelf();
    }

    public Drawable getFrontDrawable() {
        return mFrontDrawable.getInnerDrawable();
    }

    @Override
    public void reset() {
        super.reset();
        if (mCheckmarkScaleAnimator == null) {
            // Call from super's constructor. Not yet initialized.
            return;
        }
        mCheckmarkScaleAnimator.cancel();
        mCheckmarkAlphaAnimator.cancel();
        boolean side = getSideFlippingTowards();
        mCheckMarkDrawable.setScaleAnimatorValue(side ? CHECKMARK_SCALE_BEGIN_VALUE : END_VALUE);
        mCheckMarkDrawable.setAlphaAnimatorValue(side ? CHECKMARK_ALPHA_BEGIN_VALUE : END_VALUE);
    }

    @Override
    public void flip() {
        super.flip();
        // Keep the checkmark animators in sync with the flip animator.
        if (mCheckmarkScaleAnimator.isStarted()) {
            mCheckmarkScaleAnimator.reverse();
            mCheckmarkAlphaAnimator.reverse();
        } else {
            if (!getSideFlippingTowards() /* front to back */) {
                mCheckmarkScaleAnimator.start();
                mCheckmarkAlphaAnimator.start();
            } else /* back to front */ {
                mCheckmarkScaleAnimator.reverse();
                mCheckmarkAlphaAnimator.reverse();
            }
        }
    }

    @Override
    public void onAnimationUpdate(final ValueAnimator animation) {
        //noinspection ConstantConditions
        final float value = (Float) animation.getAnimatedValue();

        if (animation == mCheckmarkScaleAnimator) {
            mCheckMarkDrawable.setScaleAnimatorValue(value);
        } else if (animation == mCheckmarkAlphaAnimator) {
            mCheckMarkDrawable.setAlphaAnimatorValue(value);
        }
    }

    private static class FrontDrawable extends Drawable implements Drawable.Callback {
        private Drawable mDrawable;

        public FrontDrawable(Drawable d) {
            mDrawable = d;
            mDrawable.setCallback(this);
        }

        public Drawable getInnerDrawable() {
            return mDrawable;
        }

        public void setInnerDrawable(Drawable d) {
            mDrawable.setCallback(null);
            mDrawable = d;
            mDrawable.setCallback(this);
            assignDrawableBounds(getBounds());
            invalidateSelf();
        }

        @Override
        public void setTintMode(PorterDuff.Mode tintMode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mDrawable.setTintMode(tintMode);
            }
        }

        @Override
        public void setTintList(ColorStateList tint) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mDrawable.setTintList(tint);
            }
        }

        @Override
        public boolean setState(int[] stateSet) {
            return mDrawable.setState(stateSet);
        }

        @Override
        protected void onBoundsChange(final Rect bounds) {
            super.onBoundsChange(bounds);
            assignDrawableBounds(bounds);
        }

        private void assignDrawableBounds(Rect bounds) {
            int w = mDrawable.getIntrinsicWidth();
            int h = mDrawable.getIntrinsicHeight();
            if (w > 0 && h > 0) {
                mDrawable.setBounds(0, 0, w, h);
            } else {
                mDrawable.setBounds(bounds);
            }
        }

        @Override
        public void draw(final Canvas canvas) {
            final Rect bounds = getBounds();
            if (!isVisible() || bounds.isEmpty()) {
                return;
            }

            int w = mDrawable.getIntrinsicWidth();
            int h = mDrawable.getIntrinsicHeight();

            if (w <= 0 || h <= 0) {
                mDrawable.draw(canvas);
            } else {
                final float widthScale = (float) bounds.width() / (float) w;
                final float heightScale = (float) bounds.height() / (float) h;
                final float scale = Math.max(widthScale, heightScale);
                canvas.save();
                canvas.scale(scale, scale);
                canvas.translate(bounds.left, bounds.top);
                mDrawable.draw(canvas);
                canvas.restore();
            }
        }

        @Override
        public void setAlpha(final int alpha) {
            mDrawable.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(final ColorFilter cf) {
            mDrawable.setColorFilter(cf);
        }

        @Override
        public int getOpacity() {
            return mDrawable.getOpacity();
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
    }

    private static class CheckMarkDrawable extends Drawable {
        private static Bitmap sCheckMark;

        private final Paint mPaint;

        private float mScaleFraction;
        private float mAlphaFraction;

        private static final Matrix sMatrix = new Matrix();

        public CheckMarkDrawable(Context context, int backgroundColor) {
            if (sCheckMark == null) {
                sCheckMark = BitmapUtil.getBitmap(context, R.drawable.module_common_ic_check_fill_white);
            }
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setFilterBitmap(true);
            mPaint.setColor(backgroundColor);
        }

        public void setBackgroundColor(int color) {
            mPaint.setColor(color);
        }

        @Override
        public void draw(final Canvas canvas) {
            final Rect bounds = getBounds();
            if (!isVisible() || bounds.isEmpty()) {
                return;
            }

            canvas.drawCircle(bounds.centerX(), bounds.centerY(), bounds.width() / 2, mPaint);

            // Scale the checkmark.
            sMatrix.reset();
            sMatrix.setScale(mScaleFraction, mScaleFraction, sCheckMark.getWidth() / 2,
                    sCheckMark.getHeight() / 2);
            sMatrix.postTranslate(bounds.centerX() - sCheckMark.getWidth() / 2,
                    bounds.centerY() - sCheckMark.getHeight() / 2);

            // Fade the checkmark.
            final int oldAlpha = mPaint.getAlpha();
            // Interpolate the alpha.
            mPaint.setAlpha((int) (oldAlpha * mAlphaFraction));
            canvas.drawBitmap(sCheckMark, sMatrix, mPaint);
            // Restore the alpha.
            mPaint.setAlpha(oldAlpha);
        }

        @Override
        public void setAlpha(final int alpha) {
            mPaint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(final ColorFilter cf) {
            mPaint.setColorFilter(cf);
        }

        @Override
        public int getOpacity() {
            // Always a gray background.
            return PixelFormat.OPAQUE;
        }

        /**
         * Set value as a fraction from 0f to 1f.
         */
        public void setScaleAnimatorValue(final float value) {
            final float old = mScaleFraction;
            mScaleFraction = value;
            if (old != mScaleFraction) {
                invalidateSelf();
            }
        }

        /**
         * Set value as a fraction from 0f to 1f.
         */
        public void setAlphaAnimatorValue(final float value) {
            final float old = mAlphaFraction;
            mAlphaFraction = value;
            if (old != mAlphaFraction) {
                invalidateSelf();
            }
        }
    }
}
