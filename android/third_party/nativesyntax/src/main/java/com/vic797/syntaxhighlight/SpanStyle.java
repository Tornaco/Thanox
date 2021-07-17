package com.vic797.syntaxhighlight;

import android.graphics.Typeface;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        Typeface.NORMAL,
        Typeface.BOLD,
        Typeface.ITALIC,
        Typeface.BOLD_ITALIC
})
@Retention(RetentionPolicy.SOURCE)
public @interface SpanStyle {
}