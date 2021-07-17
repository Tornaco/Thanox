/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar;

/**
 * A {@code Drawable} that has a background.
 */
public interface ShowBackgroundDrawable {

    /**
     * Get whether this drawable is showing a background. The default is {@code true}.
     *
     * @return Whether this drawable is showing a background.
     */
    boolean getShowBackground();

    /**
     * Set whether this drawable should show a background. The default is {@code true}.
     *
     * @param show Whether background should be shown.
     */
    void setShowBackground(boolean show);
}
