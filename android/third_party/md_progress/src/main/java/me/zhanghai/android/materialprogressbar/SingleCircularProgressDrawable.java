/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

class SingleCircularProgressDrawable extends BaseSingleCircularProgressDrawable
        implements ShowBackgroundDrawable {

    /**
     * Value from {@link Drawable#getLevel()}
     */
    private static final int LEVEL_MAX = 10000;

    private static final float START_ANGLE_MAX_NORMAL = 0;
    private static final float START_ANGLE_MAX_DYNAMIC = 360;
    private static final float SWEEP_ANGLE_MAX = 360;

    private final float mStartAngleMax;

    private boolean mShowBackground;

    SingleCircularProgressDrawable(int style) {
        switch (style) {
            case MaterialProgressBar.DETERMINATE_CIRCULAR_PROGRESS_STYLE_NORMAL:
                mStartAngleMax = START_ANGLE_MAX_NORMAL;
                break;
            case MaterialProgressBar.DETERMINATE_CIRCULAR_PROGRESS_STYLE_DYNAMIC:
                mStartAngleMax = START_ANGLE_MAX_DYNAMIC;
                break;
            default:
                throw new IllegalArgumentException("Invalid value for style");
        }
    }

    @Override
    protected boolean onLevelChange(@IntRange(from = 0, to = LEVEL_MAX) int level) {
        invalidateSelf();
        return true;
    }

    @Override
    public boolean getShowBackground() {
        return mShowBackground;
    }

    @Override
    public void setShowBackground(boolean show) {
        if (mShowBackground != show) {
            mShowBackground = show;
            invalidateSelf();
        }
    }

    @Override
    protected void onDrawRing(@NonNull Canvas canvas, @NonNull Paint paint) {

        int level = getLevel();
        if (level == 0) {
            return;
        }

        float ratio = (float) level / LEVEL_MAX;
        float startAngle = ratio * mStartAngleMax;
        float sweepAngle = ratio * SWEEP_ANGLE_MAX;

        drawRing(canvas, paint, startAngle, sweepAngle);
        if (mShowBackground) {
            // Draw twice to emulate the background for secondary progress.
            drawRing(canvas, paint, startAngle, sweepAngle);
        }
    }
}
