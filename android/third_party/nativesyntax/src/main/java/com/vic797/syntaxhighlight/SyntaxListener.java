package com.vic797.syntaxhighlight;

import android.text.Editable;

/**
 * Main interface to listen the {@link SyntaxHighlighter} events
 */
public interface SyntaxListener {

    void onLineClick(Editable editable, String text, int line);

    void onHighlightStart(Editable editable);

    void onHighlightEnd(Editable editable);

    void onError(Exception e);

}