/*
 * Copyright (c) 2016 Tim Malseed
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.simplecityapps.recyclerview_fastscroll.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;

import com.simplecityapps.recyclerview_fastscroll.R;
import com.simplecityapps.recyclerview_fastscroll.interfaces.OnFastScrollStateChangeListener;
import com.simplecityapps.recyclerview_fastscroll.utils.Utils;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FastScrollRecyclerView extends RecyclerView implements RecyclerView.OnItemTouchListener {

    private static final String TAG = "FastScrollRecyclerView";

    private FastScroller mScrollbar;

    private boolean mFastScrollEnabled = true;

    /**
     * The current scroll state of the recycler view.  We use this in onUpdateScrollbar()
     * and scrollToPositionAtProgress() to determine the scroll position of the recycler view so
     * that we can calculate what the scroll bar looks like, and where to jump to from the fast
     * scroller.
     */
    public static class ScrollPositionState {
        // The index of the first visible row
        int rowIndex;
        // The offset of the first visible row
        int rowTopOffset;
        // The height of a given row (they are currently all the same height)
        int rowHeight;
    }

    private ScrollPositionState mScrollPosState = new ScrollPositionState();

    private int mDownX;
    private int mDownY;
    private int mLastY;

    private SparseIntArray mScrollOffsets;

    private ScrollOffsetInvalidator mScrollOffsetInvalidator;
    private OnFastScrollStateChangeListener mStateChangeListener;

    public FastScrollRecyclerView(Context context) {
        this(context, null);
    }

    public FastScrollRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FastScrollRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.FastScrollRecyclerView, 0, 0);
        try {
            mFastScrollEnabled = typedArray.getBoolean(R.styleable.FastScrollRecyclerView_fastScrollThumbEnabled, true);
        } finally {
            typedArray.recycle();
        }

        mScrollbar = new FastScroller(context, this, attrs);
        mScrollOffsetInvalidator = new ScrollOffsetInvalidator();
        mScrollOffsets = new SparseIntArray();
    }

    public int getScrollBarWidth() {
        return mScrollbar.getWidth();
    }

    public int getScrollBarThumbHeight() {
        return mScrollbar.getThumbHeight();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addOnItemTouchListener(this);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (getAdapter() != null) {
            getAdapter().unregisterAdapterDataObserver(mScrollOffsetInvalidator);
        }

        if (adapter != null) {
            adapter.registerAdapterDataObserver(mScrollOffsetInvalidator);
        }

        super.setAdapter(adapter);
    }

    /**
     * We intercept the touch handling only to support fast scrolling when initiated from the
     * scroll bar.  Otherwise, we fall back to the default RecyclerView touch handling.
     */
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent ev) {
        return handleTouchEvent(ev);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent ev) {
        handleTouchEvent(ev);
    }

    /**
     * Handles the touch event and determines whether to show the fast scroller (or updates it if
     * it is already showing).
     */
    private boolean handleTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // Keep track of the down positions
                mDownX = x;
                mDownY = mLastY = y;
                mScrollbar.handleTouchEvent(ev, mDownX, mDownY, mLastY, mStateChangeListener);
                break;
            case MotionEvent.ACTION_MOVE:
                mLastY = y;
                mScrollbar.handleTouchEvent(ev, mDownX, mDownY, mLastY, mStateChangeListener);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mScrollbar.handleTouchEvent(ev, mDownX, mDownY, mLastY, mStateChangeListener);
                break;
        }
        return mScrollbar.isDragging();
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    /**
     * Returns the available scroll height:
     * AvailableScrollHeight = Total height of the all items - last page height
     *
     * @param yOffset the offset from the top of the recycler view to start tracking.
     */
    protected int getAvailableScrollHeight(int adapterHeight, int yOffset) {
        int visibleHeight = getHeight();
        int scrollHeight = getPaddingTop() + yOffset + adapterHeight + getPaddingBottom();
        return scrollHeight - visibleHeight;
    }

    /**
     * Returns the available scroll bar height:
     * AvailableScrollBarHeight = Total height of the visible view - thumb height
     */
    protected int getAvailableScrollBarHeight() {
        int visibleHeight = getHeight();
        return visibleHeight - mScrollbar.getThumbHeight();
    }

    @Override
    public void draw(Canvas c) {
        super.draw(c);
        if (mFastScrollEnabled) {
            onUpdateScrollbar();
            mScrollbar.draw(c);
        }
    }

    /**
     * Updates the scrollbar thumb offset to match the visible scroll of the recycler view.  It does
     * this by mapping the available scroll area of the recycler view to the available space for the
     * scroll bar.
     *
     * @param scrollPosState the current scroll position
     * @param rowCount       the number of rows, used to calculate the total scroll height (assumes that
     */
    protected void updateThumbPosition(ScrollPositionState scrollPosState, int rowCount) {
        int availableScrollHeight;
        int availableScrollBarHeight;
        int scrolledPastHeight;

        if (getAdapter() instanceof MeasurableAdapter) {
            availableScrollHeight = getAvailableScrollHeight(calculateAdapterHeight(), 0);
            scrolledPastHeight = calculateScrollDistanceToPosition(scrollPosState.rowIndex);
        } else {
            availableScrollHeight = getAvailableScrollHeight(rowCount * scrollPosState.rowHeight, 0);
            scrolledPastHeight = scrollPosState.rowIndex * scrollPosState.rowHeight;
        }

        availableScrollBarHeight = getAvailableScrollBarHeight();

        // Only show the scrollbar if there is height to be scrolled
        if (availableScrollHeight <= 0) {
            mScrollbar.setThumbPosition(-1, -1);
            return;
        }

        // Calculate the current scroll position, the scrollY of the recycler view accounts for the
        // view padding, while the scrollBarY is drawn right up to the background padding (ignoring
        // padding)
        int scrollY = Math.min(availableScrollHeight, getPaddingTop() + scrolledPastHeight - scrollPosState.rowTopOffset);

        int scrollBarY = (int) (((float) scrollY / availableScrollHeight) * availableScrollBarHeight);

        // Calculate the position and size of the scroll bar
        int scrollBarX;
        if (Utils.isRtl(getResources())) {
            scrollBarX = 0;
        } else {
            scrollBarX = getWidth() - mScrollbar.getWidth();
        }
        mScrollbar.setThumbPosition(scrollBarX, scrollBarY);
    }

    /**
     * Maps the touch (from 0..1) to the adapter position that should be visible.
     */
    public String scrollToPositionAtProgress(float touchFraction) {
        int itemCount = getAdapter().getItemCount();
        if (itemCount == 0) {
            return "";
        }
        int spanCount = 1;
        int rowCount = itemCount;
        if (getLayoutManager() instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) getLayoutManager()).getSpanCount();
            rowCount = (int) Math.ceil((double) rowCount / spanCount);
        }

        // Stop the scroller if it is scrolling
        stopScroll();

        getCurScrollState(mScrollPosState);

        float itemPos;
        int availableScrollHeight;

        int scrollPosition;
        int scrollOffset;

        if (getAdapter() instanceof MeasurableAdapter) {
            itemPos = findItemPosition(touchFraction);
            availableScrollHeight = getAvailableScrollHeight(calculateAdapterHeight(), 0);
            int passedHeight = (int) (availableScrollHeight * touchFraction);
            scrollPosition = findMeasureAdapterFirstVisiblePosition(passedHeight);
            scrollOffset = calculateScrollDistanceToPosition(scrollPosition) - passedHeight;
        } else {
            itemPos = findItemPosition(touchFraction);
            availableScrollHeight = getAvailableScrollHeight(calculateAdapterHeight(), 0);

            //The exact position of our desired item
            int exactItemPos = (int) (availableScrollHeight * touchFraction);

            //The offset used here is kind of hard to explain.
            //If the position we wish to scroll to is, say, position 10.5, we scroll to position 10,
            //and then offset by 0.5 * rowHeight. This is how we achieve smooth scrolling.
            scrollPosition = spanCount * exactItemPos / mScrollPosState.rowHeight;
            scrollOffset = -(exactItemPos % mScrollPosState.rowHeight);
        }

        LinearLayoutManager layoutManager = ((LinearLayoutManager) getLayoutManager());
        layoutManager.scrollToPositionWithOffset(scrollPosition, scrollOffset);

        if (!(getAdapter() instanceof SectionedAdapter)) {
            return "";
        }

        int posInt = (int) ((touchFraction == 1) ? getAdapter().getItemCount() - 1 : itemPos);

        SectionedAdapter sectionedAdapter = (SectionedAdapter) getAdapter();
        return sectionedAdapter.getSectionName(posInt);
    }

    @SuppressWarnings("unchecked")
    private int findMeasureAdapterFirstVisiblePosition(int passedHeight) {
        if (getAdapter() instanceof MeasurableAdapter) {
            MeasurableAdapter measurmeasurableAdapterr = (MeasurableAdapter) getAdapter();
            for (int i = 0; i < getAdapter().getItemCount(); i++) {
                int top = calculateScrollDistanceToPosition(i);
                int bottom = top + measurmeasurableAdapterr.getViewTypeHeight(this, findViewHolderForAdapterPosition(i), getAdapter().getItemViewType(i));
                if (i == getAdapter().getItemCount() - 1) {
                    if (passedHeight >= top && passedHeight <= bottom) {
                        return i;
                    }
                } else {
                    if (passedHeight >= top && passedHeight < bottom) {
                        return i;
                    }
                }
            }
            int low = calculateScrollDistanceToPosition(0);
            int height = calculateScrollDistanceToPosition(getAdapter().getItemCount() - 1)
                    + measurmeasurableAdapterr.getViewTypeHeight(this, findViewHolderForAdapterPosition(getAdapter().getItemCount() - 1), getAdapter().getItemViewType(getAdapter().getItemCount() - 1));
            throw new IllegalStateException(String.format("Invalid passed height: %d, [low: %d, height: %d]", passedHeight, low, height));
        } else {
            throw new IllegalStateException("findMeasureAdapterFirstVisiblePosition() should only be called where the RecyclerView.Adapter is an instance of MeasurableAdapter");
        }

    }

    @SuppressWarnings("unchecked")
    private float findItemPosition(float touchFraction) {

        if (getAdapter() instanceof MeasurableAdapter) {
            MeasurableAdapter measurer = (MeasurableAdapter) getAdapter();
            int viewTop = (int) (touchFraction * calculateAdapterHeight());

            for (int i = 0; i < getAdapter().getItemCount(); i++) {
                int top = calculateScrollDistanceToPosition(i);
                int bottom = top + measurer.getViewTypeHeight(this, findViewHolderForAdapterPosition(i), getAdapter().getItemViewType(i));
                if (i == getAdapter().getItemCount() - 1) {
                    if (viewTop >= top && viewTop <= bottom) {
                        return i;
                    }
                } else {
                    if (viewTop >= top && viewTop < bottom) {
                        return i;
                    }
                }
            }

            // Should never happen
            Log.w(TAG, "Failed to find a view at the provided scroll fraction (" + touchFraction + ")");
            return touchFraction * getAdapter().getItemCount();
        } else {
            return getAdapter().getItemCount() * touchFraction;
        }
    }

    /**
     * Updates the bounds for the scrollbar.
     */
    public void onUpdateScrollbar() {

        if (getAdapter() == null) {
            return;
        }

        int rowCount = getAdapter().getItemCount();
        if (getLayoutManager() instanceof GridLayoutManager) {
            int spanCount = ((GridLayoutManager) getLayoutManager()).getSpanCount();
            rowCount = (int) Math.ceil((double) rowCount / spanCount);
        }
        // Skip early if, there are no items.
        if (rowCount == 0) {
            mScrollbar.setThumbPosition(-1, -1);
            return;
        }

        // Skip early if, there no child laid out in the container.
        getCurScrollState(mScrollPosState);
        if (mScrollPosState.rowIndex < 0) {
            mScrollbar.setThumbPosition(-1, -1);
            return;
        }

        updateThumbPosition(mScrollPosState, rowCount);
    }

    /**
     * Returns the current scroll state of the apps rows.
     */
    @SuppressWarnings("unchecked")
    private void getCurScrollState(ScrollPositionState stateOut) {
        stateOut.rowIndex = -1;
        stateOut.rowTopOffset = -1;
        stateOut.rowHeight = -1;

        int itemCount = getAdapter().getItemCount();

        // Return early if there are no items, or no children.
        if (itemCount == 0 || getChildCount() == 0) {
            return;
        }

        View child = getChildAt(0);

        stateOut.rowIndex = getChildAdapterPosition(child);
        if (getLayoutManager() instanceof GridLayoutManager) {
            stateOut.rowIndex = stateOut.rowIndex / ((GridLayoutManager) getLayoutManager()).getSpanCount();
        }
        if (getAdapter() instanceof MeasurableAdapter) {
            stateOut.rowTopOffset = getLayoutManager().getDecoratedTop(child);
            stateOut.rowHeight = ((MeasurableAdapter) getAdapter()).getViewTypeHeight(this, findViewHolderForAdapterPosition(stateOut.rowIndex), getAdapter().getItemViewType(stateOut.rowIndex));
        } else {
            stateOut.rowTopOffset = getLayoutManager().getDecoratedTop(child);
            stateOut.rowHeight = child.getHeight() + getLayoutManager().getTopDecorationHeight(child)
                    + getLayoutManager().getBottomDecorationHeight(child);
        }
    }

    /**
     * Calculates the total height of all views above a position in the recycler view. This method
     * should only be called when the attached adapter implements {@link MeasurableAdapter}.
     *
     * @param adapterIndex The index in the adapter to find the total height above the
     *                     corresponding view
     * @return The total height of all views above {@code adapterIndex} in pixels
     */
    @SuppressWarnings("unchecked")
    private int calculateScrollDistanceToPosition(int adapterIndex) {
        if (!(getAdapter() instanceof MeasurableAdapter)) {
            throw new IllegalStateException("calculateScrollDistanceToPosition() should only be called where the RecyclerView.Adapter is an instance of MeasurableAdapter");
        }

        if (mScrollOffsets.indexOfKey(adapterIndex) >= 0) {
            return mScrollOffsets.get(adapterIndex);
        }

        int totalHeight = 0;
        MeasurableAdapter measurer = (MeasurableAdapter) getAdapter();

        // TODO Take grid layouts into account

        for (int i = 0; i < adapterIndex; i++) {
            mScrollOffsets.put(i, totalHeight);
            int viewType = getAdapter().getItemViewType(i);
            totalHeight += measurer.getViewTypeHeight(this, findViewHolderForAdapterPosition(i), viewType);
        }

        mScrollOffsets.put(adapterIndex, totalHeight);
        return totalHeight;
    }

    /**
     * Calculates the total height of the recycler view. This method should only be called when the
     * attached adapter implements {@link MeasurableAdapter}.
     *
     * @return The total height of all rows in the RecyclerView
     */
    private int calculateAdapterHeight() {
        if (!(getAdapter() instanceof MeasurableAdapter)) {
            throw new IllegalStateException("calculateAdapterHeight() should only be called where the RecyclerView.Adapter is an instance of MeasurableAdapter");
        }
        return calculateScrollDistanceToPosition(getAdapter().getItemCount());
    }

    public void showScrollbar() {
        mScrollbar.show();
    }

    public void setThumbColor(@ColorInt int color) {
        mScrollbar.setThumbColor(color);
    }

    public void setTrackColor(@ColorInt int color) {
        mScrollbar.setTrackColor(color);
    }

    public void setPopupBgColor(@ColorInt int color) {
        mScrollbar.setPopupBgColor(color);
    }

    public void setPopupTextColor(@ColorInt int color) {
        mScrollbar.setPopupTextColor(color);
    }

    public void setPopupTextSize(int textSize) {
        mScrollbar.setPopupTextSize(textSize);
    }

    public void setPopUpTypeface(Typeface typeface) {
        mScrollbar.setPopupTypeface(typeface);
    }

    public void setAutoHideDelay(int hideDelay) {
        mScrollbar.setAutoHideDelay(hideDelay);
    }

    public void setAutoHideEnabled(boolean autoHideEnabled) {
        mScrollbar.setAutoHideEnabled(autoHideEnabled);
    }

    public void setOnFastScrollStateChangeListener(OnFastScrollStateChangeListener stateChangeListener) {
        mStateChangeListener = stateChangeListener;
    }

    @Deprecated
    public void setStateChangeListener(OnFastScrollStateChangeListener stateChangeListener) {
        setOnFastScrollStateChangeListener(stateChangeListener);
    }

    public void setThumbInactiveColor(@ColorInt int color) {
        mScrollbar.setThumbInactiveColor(color);
    }

    public void allowThumbInactiveColor(boolean allowInactiveColor) {
        mScrollbar.enableThumbInactiveColor(allowInactiveColor);
    }

    @Deprecated
    public void setThumbInactiveColor(boolean allowInactiveColor) {
        allowThumbInactiveColor(allowInactiveColor);
    }

    public void setFastScrollEnabled(boolean fastScrollEnabled) {
        mFastScrollEnabled = fastScrollEnabled;
    }

    @Deprecated
    public void setThumbEnabled(boolean thumbEnabled) {
        setFastScrollEnabled(thumbEnabled);
    }

    /**
     * Set the FastScroll Popup position. This is either {@link FastScroller.FastScrollerPopupPosition#ADJACENT},
     * meaning the popup moves adjacent to the FastScroll thumb, or {@link FastScroller.FastScrollerPopupPosition#CENTER},
     * meaning the popup is static and centered within the RecyclerView.
     */
    public void setPopupPosition(@FastScroller.FastScrollerPopupPosition int popupPosition) {
        mScrollbar.setPopupPosition(popupPosition);
    }

    private class ScrollOffsetInvalidator extends AdapterDataObserver {
        private void invalidateAllScrollOffsets() {
            mScrollOffsets.clear();
        }

        @Override
        public void onChanged() {
            invalidateAllScrollOffsets();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            invalidateAllScrollOffsets();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            invalidateAllScrollOffsets();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            invalidateAllScrollOffsets();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            invalidateAllScrollOffsets();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            invalidateAllScrollOffsets();
        }
    }

    public interface SectionedAdapter {
        @NonNull
        String getSectionName(int position);
    }

    /**
     * FastScrollRecyclerView by default assumes that all items in a RecyclerView will have
     * ItemViews with the same heights so that the total height of all views in the RecyclerView
     * can be calculated. If your list uses different view heights, then make your adapter implement
     * this interface.
     */
    public interface MeasurableAdapter<VH extends ViewHolder> {
        /**
         * Gets the height of a specific view type, including item decorations
         *
         * @param recyclerView The recyclerView that this item view will be placed in
         * @param viewHolder   The viewHolder that corresponds to this item view
         * @param viewType     The view type to get the height of
         * @return The height of a single view for the given view type in pixels
         */
        int getViewTypeHeight(RecyclerView recyclerView, @Nullable VH viewHolder, int viewType);
    }
}
