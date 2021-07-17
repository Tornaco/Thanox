package com.vic797.syntaxhighlight;

import android.graphics.Color;
import android.graphics.Typeface;

import androidx.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Syntax rules for {@link SyntaxHighlighter}
 */
public final class Syntax {

    private int color;
    private int backColor;
    private String formatFlags;

    private Pattern pattern;

    /**
     * Creates a new syntax rule
     *
     * @param color color of the span
     * @param regex regular expression
     */
    public Syntax(int color, String regex) {
        this.color = color;
        backColor = -1;
        pattern = Pattern.compile(regex);
        formatFlags = "normal";
    }

    /**
     * <b>Optional:</b> set a background color for the span (default is {@link Color#WHITE})
     *
     * @param color background color
     */
    public Syntax setBackgroundColor(int color) {
        this.backColor = color;
        return this;
    }

    /**
     * Returns the background color
     */
    public int getBackgroundColor() {
        return this.backColor;
    }

    /**
     * Returns the foreground color
     */
    public int getColor() {
        return color;
    }

    /**
     * Returns a matcher for a string
     *
     * @param text string to match
     * @return a matcher from a {@link Pattern}
     */
    @NonNull
    public Matcher getMatcher(String text) {
        return pattern.matcher(text);
    }

    /**
     * Set format flag
     *
     * @param formatFlags a string flags divided by
     */
    public Syntax setFormatFlag(String formatFlags) {
        this.formatFlags = ((formatFlags == null) || formatFlags.equals("")) ? "normal" : formatFlags;
        return this;
    }

    /**
     * Set format flag
     */
    public Syntax setFormatFlag(@SpanStyle int style) {
        switch (style) {
            case Typeface.NORMAL:
                return setFormatFlag("normal");
            case Typeface.BOLD:
                return setFormatFlag("bold");
            case Typeface.ITALIC:
                return setFormatFlag("italic");
            case Typeface.BOLD_ITALIC:
                return setFormatFlag("bold|italic");
            default:
                return setFormatFlag("normal");
        }
    }

    /**
     * Get the current format fla
     *
     * @return any of {@link SpanStyle}
     */
    @SpanStyle
    public int getFormatFlags() {
        switch (formatFlags) {
            case "normal":
                return Typeface.NORMAL;
            case "bold":
                return Typeface.BOLD;
            case "italic":
                return Typeface.ITALIC;
            case "bold|italic":
            case "italic|bold":
                return Typeface.BOLD_ITALIC;
            default:
                return Typeface.NORMAL;
        }
    }
}