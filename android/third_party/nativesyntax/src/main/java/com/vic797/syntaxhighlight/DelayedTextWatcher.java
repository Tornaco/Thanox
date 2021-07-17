package com.vic797.syntaxhighlight;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class helps to optimize the highlighting process of {@link SyntaxHighlighter}
 * by delaying the {@link TextWatcher#afterTextChanged(Editable)} by a specific time
 * (by default 1 second (1000 milliseconds)) and raising an event. This class uses a {@link Timer} that
 * is restarted every time that {@link TextWatcher#afterTextChanged(Editable)} is called
 * to prevent lag.
 */
public abstract class DelayedTextWatcher implements TextWatcher {

    private long millisAfter;
    private Timer timer;

    /**
     * This method is called after the {@link Timer} ends
     *
     * @see TextWatcher#afterTextChanged(Editable)
     */
    public abstract void textChanged(Editable editable);

    /**
     * Creates a new instance setting the delay to 1 second (1000 milliseconds)
     */
    public DelayedTextWatcher() {
        this(1000);
    }

    /**
     * Creates a new instance with a specific delay
     *
     * @param after a delay
     */
    public DelayedTextWatcher(long after) {
        this.millisAfter = after;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(final Editable editable) {
        if (timer != null)
            timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                textChanged(editable);
            }
        }, millisAfter);
    }
}
