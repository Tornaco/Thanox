/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

abstract class BasePaintDrawable extends BaseDrawable {

    @Nullable
    private Paint mPaint;

    @Override
    protected final void onDraw(@NonNull Canvas canvas, int width, int height) {

        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.BLACK);
            onPreparePaint(mPaint);
        }
        mPaint.setAlpha(mAlpha);
        mPaint.setColorFilter(getColorFilterForDrawing());

        onDraw(canvas, width, height, mPaint);
    }

    protected abstract void onPreparePaint(@NonNull Paint paint);

    protected abstract void onDraw(@NonNull Canvas canvas, int width, int height,
                                   @NonNull Paint paint);
}
