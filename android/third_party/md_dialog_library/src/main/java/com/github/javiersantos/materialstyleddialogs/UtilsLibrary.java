package com.github.javiersantos.materialstyleddialogs;

import android.content.Context;
import android.os.Build;
import android.util.TypedValue;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

class UtilsLibrary {

    static int getPrimaryColor(Context context) {
        TypedValue typedValue = new TypedValue();

        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);

        return typedValue.data;
    }

    static int dpToPixels(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    static int getColor(Context context, @ColorRes int colorId) {
        return ContextCompat.getColor(context, colorId);
    }

    static boolean checkApiGreaterThan(int api) {
        return Build.VERSION.SDK_INT >= api;
    }

}
