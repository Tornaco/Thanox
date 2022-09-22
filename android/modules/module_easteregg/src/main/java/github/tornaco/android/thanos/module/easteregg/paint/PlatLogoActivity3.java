/*
 * Copyright (C) 2020 The Android Open Source Project
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

package github.tornaco.android.thanos.module.easteregg.paint;

import static android.graphics.PixelFormat.TRANSLUCENT;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.module.easteregg.R;
import github.tornaco.android.thanos.util.ActivityUtils;
import lang3.ArrayUtils;


public class PlatLogoActivity3 extends Activity {
    private static final String TAG = "PlatLogoActivity";

    private static final String[][] EMOJI_SETS = {
            {"🍇", "🍈", "🍉", "🍊", "🍋", "🍌", "🍍", "🥭", "🍎", "🍏", "🍐", "🍑",
                    "🍒", "🍓", "🫐", "🥝"},
            {"😺", "😸", "😹", "😻", "😼", "😽", "🙀", "😿", "😾"},
            {"😀", "😃", "😄", "😁", "😆", "😅", "🤣", "😂", "🙂", "🙃", "🫠", "😉", "😊",
                    "😇", "🥰", "😍", "🤩", "😘", "😗", "☺️", "😚", "😙", "🥲", "😋", "😛", "😜",
                    "🤪", "😝", "🤑", "🤗", "🤭", "🫢", "🫣", "🤫", "🤔", "🫡", "🤐", "🤨", "😐",
                    "😑", "😶", "🫥", "😏", "😒", "🙄", "😬", "🤥", "😌", "😔", "😪", "🤤", "😴",
                    "😷"},
            {"🤩", "😍", "🥰", "😘", "🥳", "🥲", "🥹"},
            {"🫠"},
            {"💘", "💝", "💖", "💗", "💓", "💞", "💕", "❣", "💔", "❤", "🧡", "💛",
                    "💚", "💙", "💜", "🤎", "🖤", "🤍"},
            // {"👁", "️🫦", "👁️"}, // this one is too much
            {"👽", "🛸", "✨", "🌟", "💫", "🚀", "🪐", "🌙", "⭐", "🌍"},
            {"🌑", "🌒", "🌓", "🌔", "🌕", "🌖", "🌗", "🌘"},
            {"🐙", "🪸", "🦑", "🦀", "🦐", "🐡", "🦞", "🐠", "🐟", "🐳", "🐋", "🐬", "🫧", "🌊",
                    "🦈"},
            {"🙈", "🙉", "🙊", "🐵", "🐒"},
            {"🕛", "🕧", "🕐", "🕜", "🕑", "🕝", "🕒", "🕞", "🕓", "🕟", "🕔", "🕠", "🕕", "🕡",
                    "🕖", "🕢", "🕗", "🕣", "🕘", "🕤", "🕙", "🕥", "🕚", "🕦"},
            {"🌺", "🌸", "💮", "🏵️", "🌼", "🌿"},
            {"🐢", "✨", "🌟", "👑"}
    };

    private ImageView mLogo;
    private BubblesDrawable mBg;

    public static void start(Context context) {
        ActivityUtils.startActivity(context, PlatLogoActivity3.class);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setNavigationBarColor(0);
        getWindow().setStatusBarColor(0);

        final ActionBar ab = getActionBar();
        if (ab != null) ab.hide();

        final FrameLayout layout = new FrameLayout(this);

        final DisplayMetrics dm = getResources().getDisplayMetrics();
        final float dp = dm.density;
        final int minSide = Math.min(dm.widthPixels, dm.heightPixels);
        final int widgetSize = (int) (minSide * 0.75);
        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(widgetSize, widgetSize);
        lp.gravity = Gravity.CENTER;

        mLogo = new ImageView(this);
        mLogo.setVisibility(View.GONE);
        mLogo.setImageResource(R.drawable.module_easteregg_platlogo33);
        layout.addView(mLogo, lp);

        mBg = new BubblesDrawable();
        mBg.setLevel(0);
        mBg.avoid = widgetSize / 2;
        mBg.padding = 0.5f * dp;
        mBg.minR = 1 * dp;
        layout.setBackground(mBg);

        setContentView(layout);
        launchNextStage();

        for (String[] emojiSets : EMOJI_SETS) {
            ThanosManager.from(this).getProfileManager().executeAction(String.format("ui.showDanmu(\"%s\")", ArrayUtils.toString(emojiSets, "")));
        }
    }

    private void launchNextStage() {
        mLogo.setAlpha(0f);
        mLogo.setScaleX(0.5f);
        mLogo.setScaleY(0.5f);
        mLogo.setVisibility(View.VISIBLE);
        mLogo.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setInterpolator(new OvershootInterpolator())
                .start();

        mLogo.postDelayed(() -> {
                    final ObjectAnimator anim = ObjectAnimator.ofInt(mBg, "level", 0, 10000);
                    anim.setInterpolator(new DecelerateInterpolator(1f));
                    anim.start();
                },
                500
        );
    }

    static class Bubble {
        public float x, y, r;
        public int color;
    }

    class BubblesDrawable extends Drawable {
        private static final int MAX_BUBBS = 2000;

        private final int[] mColorIds = {
                android.R.color.system_accent1_400,
                android.R.color.system_accent1_500,
                android.R.color.system_accent1_600,

                android.R.color.system_accent2_400,
                android.R.color.system_accent2_500,
                android.R.color.system_accent2_600,
        };

        private int[] mColors = new int[mColorIds.length];

        private final Bubble[] mBubbs = new Bubble[MAX_BUBBS];
        private int mNumBubbs;

        private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        public float avoid = 0f;
        public float padding = 0f;
        public float minR = 0f;

        BubblesDrawable() {
            for (int i = 0; i < mColorIds.length; i++) {
                mColors[i] = getColor(mColorIds[i]);
            }
            for (int j = 0; j < mBubbs.length; j++) {
                mBubbs[j] = new Bubble();
            }
        }

        @Override
        public void draw(Canvas canvas) {
            final float f = getLevel() / 10000f;
            mPaint.setStyle(Paint.Style.FILL);
            int drawn = 0;
            for (int j = 0; j < mNumBubbs; j++) {
                if (mBubbs[j].color == 0 || mBubbs[j].r == 0) continue;
                mPaint.setColor(mBubbs[j].color);
                canvas.drawCircle(mBubbs[j].x, mBubbs[j].y, mBubbs[j].r * f, mPaint);
                drawn++;
            }
        }

        @Override
        protected boolean onLevelChange(int level) {
            invalidateSelf();
            return true;
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            randomize();
        }

        private void randomize() {
            final float w = getBounds().width();
            final float h = getBounds().height();
            final float maxR = Math.min(w, h) / 3f;
            mNumBubbs = 0;
            if (avoid > 0f) {
                mBubbs[mNumBubbs].x = w / 2f;
                mBubbs[mNumBubbs].y = h / 2f;
                mBubbs[mNumBubbs].r = avoid;
                mBubbs[mNumBubbs].color = 0;
                mNumBubbs++;
            }
            for (int j = 0; j < MAX_BUBBS; j++) {
                // a simple but time-tested bubble-packing algorithm:
                // 1. pick a spot
                // 2. shrink the bubble until it is no longer overlapping any other bubble
                // 3. if the bubble hasn't popped, keep it
                int tries = 5;
                while (tries-- > 0) {
                    float x = (float) Math.random() * w;
                    float y = (float) Math.random() * h;
                    float r = Math.min(Math.min(x, w - x), Math.min(y, h - y));

                    // shrink radius to fit other bubbs
                    for (int i = 0; i < mNumBubbs; i++) {
                        r = (float) Math.min(r,
                                Math.hypot(x - mBubbs[i].x, y - mBubbs[i].y) - mBubbs[i].r
                                        - padding);
                        if (r < minR) break;
                    }

                    if (r >= minR) {
                        // we have found a spot for this bubble to live, let's save it and move on
                        r = Math.min(maxR, r);

                        mBubbs[mNumBubbs].x = x;
                        mBubbs[mNumBubbs].y = y;
                        mBubbs[mNumBubbs].r = r;
                        mBubbs[mNumBubbs].color = mColors[(int) (Math.random() * mColors.length)];
                        mNumBubbs++;
                        break;
                    }
                }
            }
            Log.v(TAG, String.format("successfully placed %d bubbles (%d%%)",
                    mNumBubbs, (int) (100f * mNumBubbs / MAX_BUBBS)));
        }

        @Override
        public void setAlpha(int alpha) {
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
        }

        @Override
        public int getOpacity() {
            return TRANSLUCENT;
        }
    }

}