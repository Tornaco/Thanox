/*
 * Copyright (C) 2016 The Android Open Source Project
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

package me.zhanghai.android.materialprogressbar.internal;

import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/*
 * @see com.android.internal.graphics.drawable.AnimationScaleListDrawable
 */
public class AnimationScaleListDrawableCompat extends DrawableContainerCompat
        implements Animatable {
    private static final String TAG = "AnimationScaleListDrawableCompat";
    private AnimationScaleListState mAnimationScaleListState;
    private boolean mMutated;

    public AnimationScaleListDrawableCompat(@NonNull Drawable[] drawables) {
        setConstantState(new AnimationScaleListState(null, this, null));
        for (Drawable drawable : drawables) {
            mAnimationScaleListState.addDrawable(drawable);
        }
        onStateChange(getState());
    }

    private AnimationScaleListDrawableCompat(@Nullable AnimationScaleListState state,
                                             @Nullable Resources res) {
        // Every scale list drawable has its own constant state.
        final AnimationScaleListState newState = new AnimationScaleListState(state, this, res);
        setConstantState(newState);
        onStateChange(getState());
    }

    /**
     * Set the current drawable according to the animation scale. If scale is 0, then pick the
     * static drawable, otherwise, pick the animatable drawable.
     */
    @Override
    protected boolean onStateChange(int[] stateSet) {
        final boolean changed = super.onStateChange(stateSet);
        int idx = mAnimationScaleListState.getCurrentDrawableIndexBasedOnScale();
        return selectDrawable(idx) || changed;
    }


    @Override
    public Drawable mutate() {
        if (!mMutated && super.mutate() == this) {
            mAnimationScaleListState.mutate();
            mMutated = true;
        }
        return this;
    }

    @Override
    public void clearMutated() {
        super.clearMutated();
        mMutated = false;
    }

    @Override
    public void start() {
        Drawable dr = getCurrent();
        if (dr != null && dr instanceof Animatable) {
            ((Animatable) dr).start();
        }
    }

    @Override
    public void stop() {
        Drawable dr = getCurrent();
        if (dr != null && dr instanceof Animatable) {
            ((Animatable) dr).stop();
        }
    }

    @Override
    public boolean isRunning() {
        boolean result = false;
        Drawable dr = getCurrent();
        if (dr != null && dr instanceof Animatable) {
            result = ((Animatable) dr).isRunning();
        }
        return result;
    }

    static class AnimationScaleListState extends DrawableContainerState {
        int[] mThemeAttrs = null;
        // The index of the last static drawable.
        int mStaticDrawableIndex = -1;
        // The index of the last animatable drawable.
        int mAnimatableDrawableIndex = -1;

        AnimationScaleListState(AnimationScaleListState orig,
                                AnimationScaleListDrawableCompat owner, Resources res) {
            super(orig, owner, res);

            if (orig != null) {
                // Perform a shallow copy and rely on mutate() to deep-copy.
                mThemeAttrs = orig.mThemeAttrs;

                mStaticDrawableIndex = orig.mStaticDrawableIndex;
                mAnimatableDrawableIndex = orig.mAnimatableDrawableIndex;
            }

        }

        void mutate() {
            mThemeAttrs = mThemeAttrs != null ? mThemeAttrs.clone() : null;
        }

        /**
         * Add the drawable into the container.
         * This class only keep track one animatable drawable, and one static. If there are multiple
         * defined in the XML, then pick the last one.
         */
        int addDrawable(Drawable drawable) {
            final int pos = addChild(drawable);
            if (drawable instanceof Animatable) {
                mAnimatableDrawableIndex = pos;
            } else {
                mStaticDrawableIndex = pos;
            }
            return pos;
        }

        @Override
        public Drawable newDrawable() {
            return new AnimationScaleListDrawableCompat(this, null);
        }

        @Override
        public Drawable newDrawable(Resources res) {
            return new AnimationScaleListDrawableCompat(this, res);
        }

        @Override
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        public boolean canApplyTheme() {
            return mThemeAttrs != null || super.canApplyTheme();
        }

        public int getCurrentDrawableIndexBasedOnScale() {
            if (!ValueAnimatorCompat.areAnimatorsEnabled()) {
                return mStaticDrawableIndex;
            }
            return mAnimatableDrawableIndex;
        }
    }

    @Override
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public void applyTheme(@NonNull Resources.Theme theme) {
        super.applyTheme(theme);

        onStateChange(getState());
    }

    @Override
    protected void setConstantState(@NonNull DrawableContainerState state) {
        super.setConstantState(state);

        if (state instanceof AnimationScaleListState) {
            mAnimationScaleListState = (AnimationScaleListState) state;
        }
    }
}
