package com.vic797.syntaxhighlight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.Selection;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

/**
 * This class allows you to highlight syntax in an Android Native way using {@link android.text.Spannable}.
 * <p>
 * Features:
 * - Optimized: set the spans only in the current line, in a region or in the entire view.
 * - Personalization: Add your own rules using JSON. They can be loaded from a JSON string or a JSON file in assets or in the external storage.
 * - Read Only: use as a {@link android.widget.TextView}
 * - Any number of rules: add all the rules that you want (could affect the performance)
 * <p>
 * By default this library does not include any syntax; you can create your own syntax files using JSON or by
 * adding the {@link Syntax} rules by code.
 *
 * <b>Known bug</b>
 * - Lag while editing with a lot of visible spans
 */

@SuppressWarnings({"unused", "SameParameterValue"})
public class SyntaxHighlighter extends AppCompatEditText {

    private static final String JSON_RULES = "rules";
    private static final String JSON_SYNTAX = "syntax_";
    private static final String JSON_REGEX = "regex";
    private static final String JSON_COLOR = "color";
    private static final String JSON_BACKCOLOR = "backcolor";
    private static final String JSON_BEGIN = "begin";
    private static final String JSON_END = "end";
    private static final String JSON_COMMENT_SINGLE = "comment_single";
    private static final String JSON_COMMENT_MULTI = "comment_multiple";
    private static final String JSON_STYLE = "style";

    private static final String TAG = "SyntaxHighlighter";

    private boolean updateCalledFromInstance = false;
    private ArrayList<Syntax> syntaxList;
    private SyntaxListener listener;
    private View.OnTouchListener onTouchListener;
    private boolean links;
    private boolean highlighting;
    @ColorInt
    private int stripColor;
    private boolean stripLinesInReadOnly;
    private boolean highlightCurrentLine;
    private Paint paint;
    private boolean readOnly = false;

    /*private boolean disableEvents = false;
    private Handler updateHandler = new Handler();
    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            //updateVisibleRegion();
            highlightLine(getText(), getLine());
            disableEvents = false;
        }
    };*/

    /*private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            //onSelectionChanged(start, count);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!disableEvents) {
                disableEvents = true;
                updateHandler.postDelayed(updateRunnable, 1500);
            }
        }
    };*/

    private DelayedTextWatcher textWatcher = new DelayedTextWatcher(1000) {

        @Override
        public void textChanged(Editable editable) {
            highlightLine(getText(), getLine());
        }
    };

    private ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            updateZone();
            updateCalledFromInstance = true;
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    };

    public SyntaxHighlighter(Context context) {
        this(context, null);
    }

    public SyntaxHighlighter(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SyntaxHighlighter(Context context, @Nullable AttributeSet attrs, int defStyleRes) {
        super(context, attrs, defStyleRes);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SyntaxHighlighter);
        try {
            highlightLinks(array.getBoolean(R.styleable.SyntaxHighlighter_highlightLinks, false));
            setReadonly(array.getBoolean(R.styleable.SyntaxHighlighter_readonly, false));
            setStripLinesInReadOnly(array.getBoolean(R.styleable.SyntaxHighlighter_stripLinesInReadOnlyMode, true));
            setStripColor(array.getColor(R.styleable.SyntaxHighlighter_stripLineColor, Color.parseColor("#e0e0e0")));
            setHighlightCurrentLine(array.getBoolean(R.styleable.SyntaxHighlighter_highlightCurrentLine, true));
        } finally {
            array.recycle();
        }
        highlighting = false;
        syntaxList = new ArrayList<>();
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    public void setOnTouchListener(@Nullable View.OnTouchListener listener) {
        this.onTouchListener = listener;
    }

    private int lastLine = -1;

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        if (getLayout() != null) {
            int currentLine = getLayout().getLineForOffset(selStart);
            if (lastLine != currentLine) {
                lastLine = currentLine;
                highlightLine(getText(), new int[]{getLayout().getLineStart(currentLine), getLayout().getLineEnd(currentLine)});
            }
        }
        super.onSelectionChanged(selStart, selEnd);
    }

    private Rect rect = new Rect();

    @Override
    protected void onDraw(Canvas canvas) {
        if (paint != null) {
            if (readOnly) {
                int line = getBaseline();
                int lineCount = getLineCount();
                for (int i = 0; i < lineCount; i++) {
                    if (i % 2 == 0) {
                        getLineBounds(i, rect);
                        rect.left -= getPaddingLeft();
                        rect.right += getPaddingRight();
                        canvas.drawRect(rect, paint);
                    }
                    line += getLineHeight();
                }
            } else {
                getLineBounds(getCurrentLine(), rect);
                canvas.drawRect(rect, paint);
            }
        }
        super.onDraw(canvas);
    }

    //Main calls

    /**
     * Set a strip or highlight color
     */
    public void setStripColor(@ColorInt int color) {
        this.stripColor = color;
        if (paint == null)
            paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
    }

    /**
     * The lines will appear in a horizontal strip pattern
     *
     * @param sliro should do the pattern
     */
    public void setStripLinesInReadOnly(boolean sliro) {
        this.stripLinesInReadOnly = sliro;
    }

    /**
     * Should set a background indocator on the current line?
     */
    public void setHighlightCurrentLine(boolean highlighting) {
        this.highlightCurrentLine = highlighting;
    }

    /**
     * Get the current highlight color
     */
    @ColorInt
    public int getStripColor() {
        return stripColor;
    }

    /**
     * Check if the strip pattern is enabled
     */
    public boolean isStripLinesInReadOnly() {
        return stripLinesInReadOnly;
    }

    public boolean isHighlightCurrentLine() {
        return highlightCurrentLine;
    }

    /**
     * Adds a new syntax rule
     *
     * @param syntaxList Syntax objects
     */
    public void addSyntax(Syntax... syntaxList) {
        if (syntaxList.length == 0)
            return;
        this.syntaxList.addAll(Arrays.asList(syntaxList));
    }

    /**
     * Adds a new syntax rule
     *
     * @param color color of the span
     * @param regex regex for the span
     * @see #addSyntax(Syntax...)
     */
    public void addSyntax(@ColorInt int color, String regex) {
        addSyntax(new Syntax(color, regex));
    }

    /**
     * Generates a list of syntax rules from a JSON string
     *
     * @param json JSON string
     */
    public void addSyntax(String json) {
        try {
            JSONObject object = new JSONObject(json);
            if (object.has(JSON_RULES)) {
                int size = object.getInt(JSON_RULES);
                if (size >= 1) {
                    for (int i = 1; i <= size; i++) {
                        String name = JSON_SYNTAX + i;
                        if (!object.has(name))
                            if (listener != null) {
                                listener.onError(new Exception("Count mismatch; the amount of \"syntax_\" tags must start in 1 and end in " + size));
                                return;
                            }
                        JSONObject syntax = object.getJSONObject(name);
                        String regex = syntax.getString(JSON_REGEX);
                        String color = syntax.getString(JSON_COLOR);
                        Syntax s = new Syntax(Color.parseColor(color), regex);
                        if (syntax.has(JSON_BACKCOLOR)) {
                            String backcolor = syntax.getString(JSON_BACKCOLOR);
                            s.setBackgroundColor(Color.parseColor(backcolor));
                        }
                        if (syntax.has(JSON_STYLE))
                            s.setFormatFlag(syntax.getString(JSON_STYLE));
                        syntaxList.add(s);
                    }
                }
            }

            if (object.has(JSON_COMMENT_SINGLE)) {
                String singleStart = object.getJSONObject(JSON_COMMENT_SINGLE).getString(JSON_BEGIN);
                String singleColor = object.getJSONObject(JSON_COMMENT_SINGLE).getString(JSON_COLOR);
                addSingleLineComment(singleStart, Color.parseColor(singleColor));
            }

            if (object.has(JSON_COMMENT_MULTI)) {
                String multiStart = object.getJSONObject(JSON_COMMENT_MULTI).getString(JSON_BEGIN);
                String multiEnd = object.getJSONObject(JSON_COMMENT_MULTI).getString(JSON_END);
                String multiColor = object.getJSONObject(JSON_COMMENT_MULTI).getString(JSON_COLOR);
                addMultiLineComment(multiStart, multiEnd, Color.parseColor(multiColor));
            }
        } catch (Exception e) {
            if (listener != null)
                listener.onError(e);
        }
    }

    /**
     * Generates a list of syntax rules from a JSON file
     *
     * @param file JSON file
     * @see #addSyntax(String)
     */
    public void addSyntax(File file) {
        if (file.isDirectory())
            return;
        if (!file.getName().endsWith("json"))
            return;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            addSyntax(builder.toString());
        } catch (IOException e) {
            if (listener != null)
                listener.onError(e);
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                if (listener != null)
                    listener.onError(e);
            }
        }
    }

    /**
     * Generates a list of syntax rules from a JSON file in the assets folder
     *
     * @param manager {@link AssetManager} assets
     * @param file    name of the file
     * @see #addSyntax(File)
     * @see #addSyntax(String)
     */
    public void addSyntax(AssetManager manager, String file) {
        if (!file.endsWith("json"))
            return;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(manager.open(file)));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            addSyntax(builder.toString());
        } catch (IOException e) {
            if (listener != null)
                listener.onError(e);
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                if (listener != null)
                    listener.onError(e);
            }
        }
    }

    /**
     * Remove all the rules
     *
     * @param clearSpans if true clears all the spans
     */
    public void removeAllRules(boolean clearSpans) {
        syntaxList.clear();
        if (clearSpans)
            clear();
    }

    /**
     * Generates a rule for single line comments
     *
     * @param sl    how must start a line of a comment
     * @param color color of the comment
     */
    public void addSingleLineComment(@NonNull String sl, @ColorInt int color) {
        if (!sl.equals("")) {
            StringBuilder t = new StringBuilder();
            for (char c : sl.toCharArray()) {
                t.append("\\").append(String.valueOf(c));
            }
            t.append(".*\\n");
            addSyntax(new Syntax(color, t.toString()));
        }
    }

    /**
     * Generates a rule for single line comments
     *
     * @param start how must start a line of a comment
     * @param end   how must end a line of a comment
     * @param color color of the comment
     */
    public void addMultiLineComment(@NonNull String start, @NonNull String end, @ColorInt int color) {
        if ((!start.equals("")) && (!end.equals(""))) {
            StringBuilder t = new StringBuilder();
            for (char c : start.toCharArray()) {
                t.append("\\").append(String.valueOf(c));
            }
            t.append("\\s*\\S[\\s\\S]*");
            for (char c : end.toCharArray()) {
                t.append("\\").append(String.valueOf(c));
            }
            addSyntax(new Syntax(color, t.toString()));
        }
    }

    /**
     * Set the horizontal scroll active or inactive
     * <b>Important:</b> may cause lag
     *
     * @param ehs enabled or not
     */
    public void enableHorizontalScroll(boolean ehs) {
        setHorizontalFadingEdgeEnabled(ehs);
        setHorizontallyScrolling(ehs);
        setHorizontalScrollBarEnabled(ehs);
    }

    /**
     * Enable or disable links
     *
     * @param b enabled or disabled
     */
    public void highlightLinks(boolean b) {
        this.links = b;
    }

    /**
     * Removes a syntax rule
     *
     * @param syntax syntax rule to remove
     */
    public void removeSyntax(Syntax syntax) {
        syntaxList.remove(syntax);
    }

    /**
     * Removes a syntax rule
     *
     * @param index index of syntax rule to remove
     */
    public void removeSyntax(int index) {
        syntaxList.remove(index);
    }

    /**
     * Sets the {@link EditText} to read only
     * <b>Important:</b> can not be undone
     */
    public void setReadonly(boolean readonly) {
        setFocusable(!readonly);
        this.readOnly = readonly;
    }

    /**
     * Set a {@link SyntaxListener} for the syntax highlight process
     *
     * @param listener a listener
     */
    public void setListener(@Nullable SyntaxListener listener) {
        this.listener = listener;
    }

    //Voids

    /**
     * Activates the highlighter and this method <b>must be called</b>
     *
     * @param all if true highlights all the text
     * @see #stopHighlight(boolean, boolean)
     */
    public void startHighlight(boolean all) {
        if (highlighting) {
            if (listener != null)
                listener.onError(new Exception("Unnecessary call, highlighting already stated!"));
            return;
        }
        if ((getInputType() & InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS) != InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
            setInputType(getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        if (syntaxList.isEmpty()) {
            if (listener != null)
                listener.onError(new IndexOutOfBoundsException("To start the highlighter there must be at least one rule!"));
        } else {
            if (all)
                highlightLine(getText(), new int[]{0, getText().length()});
            else
                updateVisibleRegion();
            addTextChangedListener(textWatcher);
            super.setOnTouchListener(getTouchListener());
            updateCalledFromInstance = true;
            highlighting = true;
        }
    }

    /**
     * Stops the highlighting process
     *
     * @param clear      should clear the spans?
     * @param resetRules should reset the highlight rules?
     * @see #startHighlight(boolean)
     */
    public void stopHighlight(boolean clear, boolean resetRules) {
        if (!highlighting) {
            if (listener != null)
                listener.onError(new Exception("Unnecessary call, please call #startHighlight(boolean) before"));
            return;
        }
        removeTextChangedListener(textWatcher);
        super.setOnTouchListener(onTouchListener);
        if (clear) clear();
        if (resetRules) removeAllRules(false);
        highlighting = false;
    }

    /**
     * Clears all the spans
     */
    public void clear() {
        getText().clearSpans();
    }

    /**
     * Updates the spans in a region of lines
     *
     * @param l1 fist line
     * @param l2 last line
     */
    public void updateRegion(int l1, int l2) {
        if (getLayout() == null) {
            if (listener != null)
                listener.onError(new NullPointerException("Layout not initialized!"));
            return;
        }
        for (int i = l1; i <= l2; i++) {
            int start = getLayout().getLineStart(i);
            int end = getLayout().getLineEnd(i);
            int[] se = new int[]{start, end};
            highlightLine(getText(), se);
        }
    }

    /**
     * Updates the visible region
     * <b>Must be scrollable or inside a {@link ScrollView}</b>
     */
    public void updateVisibleRegion() {
        if (!updateCalledFromInstance) {
            getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
        } else {
            if (getLayout() != null)
                updateZone();
        }
    }

    /**
     * Returns the current line
     */
    public int getCurrentLine() {
        int current = 0;
        int selectionStart = Selection.getSelectionStart(getText());
        Layout layout = getLayout();
        if (!(selectionStart == -1))
            current = layout.getLineForOffset(selectionStart);
        return current;
    }

    /**
     * Check if the highlighting process has stated
     *
     * @return true if the highlighting process has stated
     */
    public boolean isHighlighting() {
        return highlighting;
    }

    //Private methods

    @NonNull
    private View.OnTouchListener getTouchListener() {
        return new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                EditText editText = SyntaxHighlighter.this;
                if ((listener != null) && (motionEvent.getAction() == MotionEvent.ACTION_DOWN)) {
                    int l = editText.getLayout().getLineForVertical((int) motionEvent.getY());
                    int s = editText.getLayout().getLineStart(l);
                    int e = editText.getLayout().getLineEnd(l);
                    String text = editText.getText().subSequence(s, e).toString();
                    listener.onLineClick(editText.getText(), text, l);
                }
                if ((editText.getParent() instanceof ScrollView) && (motionEvent.getAction() == MotionEvent.ACTION_UP))
                    updateVisibleRegion();
                return onTouchListener != null && onTouchListener.onTouch(view, motionEvent);
            }
        };
    }

    private void updateZone() {
        if (getLayout() == null)
            return;
        if (getParent() instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) getParent();
            Rect rect = new Rect();
            scrollView.getDrawingRect(rect);
            Layout layout = getLayout();
            int firstVisibleLineNumber = layout.getLineForVertical(rect.top);
            int lastVisibleLineNumber = layout.getLineForVertical(rect.bottom);
            updateRegion(firstVisibleLineNumber, lastVisibleLineNumber);
        } else {
            Layout layout = getLayout();
            int firstVisibleLineNumber = layout.getLineForVertical(getTop());
            int lastVisibleLineNumber = layout.getLineForVertical(getHeight());
            updateRegion(firstVisibleLineNumber, lastVisibleLineNumber);
        }
    }

    private void highlightLine(Editable editable, int[] line) {
        try {
            if (syntaxList == null)
                return;
            if (line.length == 0)
                return;
            if (listener != null)
                listener.onHighlightStart(editable);
            String text = editable.subSequence(line[0], line[1]).toString();
            for (Syntax syntax : syntaxList) {
                Matcher matcher = syntax.getMatcher(text);
                while (matcher.find()) {
                    int start = matcher.start() + ((matcher.start() == 0) && (line[0] == 0) ? 0 : line[0]);
                    int end = matcher.end() + (matcher.end() == line[1] ? 0 : line[0]);
                    editable.setSpan(new ForegroundColorSpan(syntax.getColor()), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    if (syntax.getBackgroundColor() != -1)
                        editable.setSpan(new BackgroundColorSpan(syntax.getBackgroundColor()), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    if (syntax.getFormatFlags() != Typeface.NORMAL)
                        editable.setSpan(new StyleSpan(syntax.getFormatFlags()), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            if (links)
                Linkify.addLinks(this, Linkify.ALL);
            if (listener != null)
                listener.onHighlightEnd(editable);
        } catch (Exception e) {
            if (listener != null)
                listener.onError(e);
        }
    }

    @NonNull
    private int[] getLine() {
        int current = getCurrentLine();
        if (current == -1) {
            return new int[]{};
        }
        int start = getLayout().getLineStart(current);
        int end = getLayout().getLineEnd(current);
        return new int[]{start, end};
    }

}
