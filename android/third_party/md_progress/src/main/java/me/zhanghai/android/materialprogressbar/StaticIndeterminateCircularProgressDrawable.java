package me.zhanghai.android.materialprogressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Px;
import me.zhanghai.android.materialprogressbar.internal.ThemeUtils;

public class StaticIndeterminateCircularProgressDrawable extends BaseProgressDrawable {

    /**
     * Backported {@code Path} for {@code @android:drawable/progress_static_material}.
     */
    private static final Path PATH_PROGRESS;
    static {
        // M17.65,6.35
        // C16.2,4.9 14.21,4.0 12.0,4.0
        // c-4.42,0.0 -7.99,3.58 -7.99,8.0
        // s3.57,8.0 7.99,8.0
        // c3.73,0.0 6.84,-2.55 7.73,-6.0
        // l-2.08,0.0
        // c-0.82,2.33 -3.04,4.0 -5.65,4.0 -3.31,0.0 -6.0,-2.69 -6.0,-6.0
        // s2.69,-6.0 6.0,-6.0
        // c1.66,0.0 3.1,0.69 4.22,1.78
        // L13.0,11.0
        // l7.0,0.0
        // L20.0,4.0
        // l-2.35,2.35
        // z
        //
        // M 17.65,6.35
        // C 16.2,4.9 14.21,4 12,4
        // C 7.58,4 4.01,7.58 4.01,12
        // c 0,4.42 3.57,8 7.99,8
        // c 3.73,0 6.84,-2.55 7.73,-6
        // H 17.65
        // C 16.83,16.33 14.61,18 12,18
        // C 8.69,18 6,15.31 6,12
        // C 6,8.69 8.69,6 12,6
        // c 1.66,0 3.1,0.69 4.22,1.78
        // L 13,11
        // h 7
        // V 4
        // Z
        PATH_PROGRESS = new Path();
        PATH_PROGRESS.moveTo(17.65f, 6.35f);
        PATH_PROGRESS.cubicTo(16.2f, 4.9f, 14.21f, 4f, 12f, 4f);
        PATH_PROGRESS.cubicTo(7.58f, 4f, 4.01f, 7.58f, 4.01f, 12f);
        PATH_PROGRESS.rCubicTo(0f, 4.42f, 3.57f, 8f, 7.99f, 8f);
        PATH_PROGRESS.rCubicTo(3.73f, 0f, 6.84f, -2.55f, 7.73f, -6f);
        PATH_PROGRESS.rLineTo(-2.08f, 0f);
        PATH_PROGRESS.cubicTo(16.83f, 16.33f, 14.61f, 18f, 12f, 18f);
        PATH_PROGRESS.cubicTo(8.69f, 18f, 6f, 15.31f, 6f, 12f);
        PATH_PROGRESS.cubicTo(6f, 8.69f, 8.69f, 6f, 12f, 6f);
        PATH_PROGRESS.rCubicTo(1.66f, 0f, 3.1f, 0.69f, 4.22f, 1.78f);
        PATH_PROGRESS.lineTo(13f, 11f);
        PATH_PROGRESS.rLineTo(7f, 0f);
        PATH_PROGRESS.lineTo(20f, 4f);
        PATH_PROGRESS.close();
    }

    @Dimension(unit = Dimension.DP)
    private static final int PROGRESS_INTRINSIC_SIZE_DP = 42;
    @Dimension(unit = Dimension.DP)
    private static final int PADDED_INTRINSIC_SIZE_DP = 48;
    // To keep the same ratio as indeterminate circular progress drawable.
    private static final RectF RECT_PROGRESS_BOUND = new RectF(3, 3, 21, 21);
    private static final RectF RECT_PADDED_BOUND = new RectF(0, 0, 24, 24);

    @Px
    private final int mProgressIntrinsicSize;
    @Px
    private final int mPaddedIntrinsicSize;

    @NonNull
    private final Path mPath = new Path();
    @NonNull
    private final Matrix mMatrix = new Matrix();

    /**
     * Create a new {@code StaticIndeterminateCircularProgressDrawable}.
     *
     * @param context the {@code Context} for retrieving style information.
     */
    public StaticIndeterminateCircularProgressDrawable(@NonNull Context context) {

        float density = context.getResources().getDisplayMetrics().density;
        mProgressIntrinsicSize = Math.round(PROGRESS_INTRINSIC_SIZE_DP * density);
        mPaddedIntrinsicSize = Math.round(PADDED_INTRINSIC_SIZE_DP * density);

        int controlActivatedColor = ThemeUtils.getColorFromAttrRes(R.attr.colorControlActivated,
                Color.BLACK, context);
        // setTint() has been overridden for compatibility.
        setTint(controlActivatedColor);
    }

    @Px
    private int getIntrinsicSize() {
        return mUseIntrinsicPadding ? mPaddedIntrinsicSize : mProgressIntrinsicSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Px
    public int getIntrinsicWidth() {
        return getIntrinsicSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Px
    public int getIntrinsicHeight() {
        return getIntrinsicSize();
    }

    @Override
    protected void onPreparePaint(@NonNull Paint paint) {
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas, int width, int height, @NonNull Paint paint) {
        // Drawing the transformed path makes rendering much less blurry than using canvas transform
        // directly. See https://stackoverflow.com/a/16091390 .
        RectF bound = mUseIntrinsicPadding ? RECT_PADDED_BOUND : RECT_PROGRESS_BOUND;
        mMatrix.setScale(width / bound.width(), height / bound.height());
        mMatrix.preTranslate(-bound.left, -bound.top);
        PATH_PROGRESS.transform(mMatrix, mPath);
        canvas.drawPath(mPath, paint);
    }
}
