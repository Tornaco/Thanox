/**
 *
 */
package github.tornaco.android.thanos.process;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class LinearColorBar extends LinearLayout {

    static final int RIGHT_COLOR = 0xffced7db;
    static final int GRAY_COLOR = 0xff555555;
    static final int WHITE_COLOR = 0xffffffff;

    private float mRedRatio;
    private float mYellowRatio;
    private float mGreenRatio;

    private int mLeftColor;
    private int mMiddleColor;
    private int mRightColor = RIGHT_COLOR;

    private boolean mShowIndicator = true;
    private boolean mShowingGreen;

    private OnRegionTappedListener mOnRegionTappedListener;
    private int mColoredRegions = REGION_RED | REGION_YELLOW | REGION_GREEN;

    final Rect mRect = new Rect();
    final Paint mPaint = new Paint();

    int mLastInterestingLeft, mLastInterestingRight;
    int mLineWidth;

    int mLastLeftDiv, mLastRightDiv;
    int mLastRegion;

    final Path mColorPath = new Path();
    final Path mEdgePath = new Path();
    final Paint mColorGradientPaint = new Paint();
    final Paint mEdgeGradientPaint = new Paint();

    public static final int REGION_RED = 1 << 0;
    public static final int REGION_YELLOW = 1 << 1;
    public static final int REGION_GREEN = 1 << 2;
    public static final int REGION_ALL = REGION_RED | REGION_YELLOW | REGION_GREEN;

    public interface OnRegionTappedListener {
        public void onRegionTapped(int region);
    }

    public LinearColorBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        mPaint.setStyle(Paint.Style.FILL);
        mColorGradientPaint.setStyle(Paint.Style.FILL);
        mColorGradientPaint.setAntiAlias(true);
        mEdgeGradientPaint.setStyle(Paint.Style.STROKE);
        mLineWidth = getResources().getDisplayMetrics().densityDpi >= DisplayMetrics.DENSITY_HIGH
                ? 2 : 1;
        mEdgeGradientPaint.setStrokeWidth(mLineWidth);
        mEdgeGradientPaint.setAntiAlias(true);
        mLeftColor = mMiddleColor = Utils.getColorAccent(context);
    }

    public void setOnRegionTappedListener(OnRegionTappedListener listener) {
        if (listener != mOnRegionTappedListener) {
            mOnRegionTappedListener = listener;
            setClickable(listener != null);
        }
    }

    public void setColoredRegions(int regions) {
        mColoredRegions = regions;
        invalidate();
    }

    public void setRatios(float red, float yellow, float green) {
        mRedRatio = red;
        mYellowRatio = yellow;
        mGreenRatio = green;
        invalidate();
    }

    public void setColors(int red, int yellow, int green) {
        mLeftColor = red;
        mMiddleColor = yellow;
        mRightColor = green;
        updateIndicator();
        invalidate();
    }

    public void setShowIndicator(boolean showIndicator) {
        mShowIndicator = showIndicator;
        updateIndicator();
        invalidate();
    }

    public void setShowingGreen(boolean showingGreen) {
        if (mShowingGreen != showingGreen) {
            mShowingGreen = showingGreen;
            updateIndicator();
            invalidate();
        }
    }

    private void updateIndicator() {
        int off = getPaddingTop() - getPaddingBottom();
        if (off < 0) off = 0;
        mRect.top = off;
        mRect.bottom = getHeight();
        if (!mShowIndicator) {
            return;
        }
        if (mShowingGreen) {
            mColorGradientPaint.setShader(new LinearGradient(
                    0, 0, 0, off - 2, mRightColor & 0xffffff, mRightColor, Shader.TileMode.CLAMP));
        } else {
            mColorGradientPaint.setShader(new LinearGradient(
                    0, 0, 0, off - 2, mMiddleColor & 0xffffff, mMiddleColor, Shader.TileMode.CLAMP));
        }
        mEdgeGradientPaint.setShader(new LinearGradient(
                0, 0, 0, off / 2, 0x00a0a0a0, 0xffa0a0a0, Shader.TileMode.CLAMP));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateIndicator();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mOnRegionTappedListener != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    final int x = (int) event.getX();
                    if (x < mLastLeftDiv) {
                        mLastRegion = REGION_RED;
                    } else if (x < mLastRightDiv) {
                        mLastRegion = REGION_YELLOW;
                    } else {
                        mLastRegion = REGION_GREEN;
                    }
                    invalidate();
                }
                break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void dispatchSetPressed(boolean pressed) {
        invalidate();
    }

    @Override
    public boolean performClick() {
        if (mOnRegionTappedListener != null && mLastRegion != 0) {
            mOnRegionTappedListener.onRegionTapped(mLastRegion);
            mLastRegion = 0;
        }
        return super.performClick();
    }

    private int pickColor(int color, int region) {
        if (isPressed() && (mLastRegion & region) != 0) {
            return WHITE_COLOR;
        }
        if ((mColoredRegions & region) == 0) {
            return GRAY_COLOR;
        }
        return color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();

        int left = 0;

        int right = left + (int) (width * mRedRatio);
        int right2 = right + (int) (width * mYellowRatio);
        int right3 = right2 + (int) (width * mGreenRatio);

        int indicatorLeft, indicatorRight;
        if (mShowingGreen) {
            indicatorLeft = right2;
            indicatorRight = right3;
        } else {
            indicatorLeft = right;
            indicatorRight = right2;
        }

        if (mLastInterestingLeft != indicatorLeft || mLastInterestingRight != indicatorRight) {
            mColorPath.reset();
            mEdgePath.reset();
            if (mShowIndicator && indicatorLeft < indicatorRight) {
                final int midTopY = mRect.top;
                final int midBottomY = 0;
                final int xoff = 2;
                mColorPath.moveTo(indicatorLeft, mRect.top);
                mColorPath.cubicTo(indicatorLeft, midBottomY,
                        -xoff, midTopY,
                        -xoff, 0);
                mColorPath.lineTo(width + xoff - 1, 0);
                mColorPath.cubicTo(width + xoff - 1, midTopY,
                        indicatorRight, midBottomY,
                        indicatorRight, mRect.top);
                mColorPath.close();
                final float lineOffset = mLineWidth + .5f;
                mEdgePath.moveTo(-xoff + lineOffset, 0);
                mEdgePath.cubicTo(-xoff + lineOffset, midTopY,
                        indicatorLeft + lineOffset, midBottomY,
                        indicatorLeft + lineOffset, mRect.top);
                mEdgePath.moveTo(width + xoff - 1 - lineOffset, 0);
                mEdgePath.cubicTo(width + xoff - 1 - lineOffset, midTopY,
                        indicatorRight - lineOffset, midBottomY,
                        indicatorRight - lineOffset, mRect.top);
            }
            mLastInterestingLeft = indicatorLeft;
            mLastInterestingRight = indicatorRight;
        }

        if (!mEdgePath.isEmpty()) {
            canvas.drawPath(mEdgePath, mEdgeGradientPaint);
            canvas.drawPath(mColorPath, mColorGradientPaint);
        }

        if (left < right) {
            mRect.left = left;
            mRect.right = right;
            mPaint.setColor(pickColor(mLeftColor, REGION_RED));
            canvas.drawRect(mRect, mPaint);
            width -= (right - left);
            left = right;
        }

        mLastLeftDiv = right;
        mLastRightDiv = right2;

        right = right2;

        if (left < right) {
            mRect.left = left;
            mRect.right = right;
            mPaint.setColor(pickColor(mMiddleColor, REGION_YELLOW));
            canvas.drawRect(mRect, mPaint);
            width -= (right - left);
            left = right;
        }


        right = left + width;
        if (left < right) {
            mRect.left = left;
            mRect.right = right;
            mPaint.setColor(pickColor(mRightColor, REGION_GREEN));
            canvas.drawRect(mRect, mPaint);
        }
    }
}