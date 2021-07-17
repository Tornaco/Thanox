/*
 * Copyright (C) 2014 The Android Open Source Project
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

package github.tornaco.android.thanos.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import java.util.ArrayList;

import github.tornaco.android.thanos.module.common.R;
import lombok.Getter;
import lombok.Setter;

public class SwitchBar extends LinearLayout
    implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

  private static int[] MARGIN_ATTRIBUTES = {
    R.attr.switchBarMarginStart,
    R.attr.switchBarMarginEnd,
    R.attr.switchBarTextColor,
    R.attr.switchBarBackgroundColor,
    R.attr.switchBarBackgroundColorChecked
  };
  private final TextAppearanceSpan mSummarySpan;

  private ToggleSwitch mSwitch;
  private TextView mTextView;
  private String mLabel;
  private String mSummary;

  private ArrayList<OnSwitchChangeListener> mSwitchChangeListeners = new ArrayList<>();

  public SwitchBar(Context context) {
    this(context, null);
  }

  public SwitchBar(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SwitchBar(Context context, AttributeSet attrs, int defStyleAttr) {
    this(context, attrs, defStyleAttr, 0);
  }

  @Setter @Getter private int onRes, offRes;

  @ColorInt private int bgColorChecked;
  @ColorInt private int bgColorUnChecked;

  @SuppressWarnings("ResourceType")
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public SwitchBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);

    LayoutInflater.from(context).inflate(R.layout.switch_bar, this);

    final TypedArray a = context.obtainStyledAttributes(attrs, MARGIN_ATTRIBUTES);
    int switchBarMarginStart = (int) a.getDimension(0, 0);
    int switchBarMarginEnd = (int) a.getDimension(1, 0);
    int textColor = a.getColor(2, Color.WHITE);
    bgColorUnChecked = a.getColor(3, Color.WHITE);
    bgColorChecked = a.getColor(4, Color.WHITE);
    a.recycle();

    mTextView = findViewById(R.id.switch_text);
    mTextView.setTextColor(textColor);
    mLabel = getResources().getString(R.string.switch_off_text);
    mSummarySpan = new TextAppearanceSpan(getContext(), R.style.TextAppearance_Small_SwitchBar);
    updateText();
    MarginLayoutParams lp = (MarginLayoutParams) mTextView.getLayoutParams();
    lp.setMarginStart(switchBarMarginStart);

    mSwitch = findViewById(R.id.switch_widget);
    // Prevent onSaveInstanceState() to be called as we are managing the state of the Switch
    // on our own
    mSwitch.setSaveEnabled(false);
    lp = (MarginLayoutParams) mSwitch.getLayoutParams();
    lp.setMarginEnd(switchBarMarginEnd);

    setBackgroundColor(bgColorUnChecked);

    addOnSwitchChangeListener((switchView, isChecked) -> setTextViewLabel(isChecked));

    setOnClickListener(this);

    setVisibility(INVISIBLE);
    show();
  }

  public void setTextViewLabel(boolean isChecked) {
    mLabel =
        getResources()
            .getString(
                isChecked
                    ? (onRes > 0 ? onRes : R.string.switch_on_text)
                    : (offRes > 0 ? offRes : R.string.switch_off_text));
    updateText();
  }

  public void setSummary(String summary) {
    mSummary = summary;
    updateText();
  }

  private void updateText() {
    if (TextUtils.isEmpty(mSummary)) {
      mTextView.setText(mLabel);
      return;
    }
    final SpannableStringBuilder ssb = new SpannableStringBuilder(mLabel).append('\n');
    final int start = ssb.length();
    ssb.append(mSummary);
    ssb.setSpan(mSummarySpan, start, ssb.length(), 0);
    mTextView.setText(ssb);
  }

  public void setCheckedInternal(boolean checked) {
    setTextViewLabel(checked);
    mSwitch.setCheckedInternal(checked);
  }

  public boolean isChecked() {
    return mSwitch.isChecked();
  }

  public void setChecked(boolean checked) {
    setTextViewLabel(checked);
    mSwitch.setChecked(checked);
    setBackgroundColor(checked ? bgColorChecked : bgColorUnChecked);
  }

  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    mTextView.setEnabled(enabled);
    mSwitch.setEnabled(enabled);
  }

  public final ToggleSwitch getSwitch() {
    return mSwitch;
  }

  public void show() {
    if (!isShowing()) {
      setVisibility(View.VISIBLE);
      mSwitch.setOnCheckedChangeListener(this);
    }
  }

  public void hide() {
    if (isShowing()) {
      setVisibility(View.GONE);
      mSwitch.setOnCheckedChangeListener(null);
    }
  }

  public boolean isShowing() {
    return (getVisibility() == View.VISIBLE);
  }

  @Override
  public void onClick(View v) {
    final boolean isChecked = !mSwitch.isChecked();
    setChecked(isChecked);
  }

  public void propagateChecked(boolean isChecked) {
    final int count = mSwitchChangeListeners.size();
    for (int n = 0; n < count; n++) {
      mSwitchChangeListeners.get(n).onSwitchChanged(mSwitch, isChecked);
    }
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    propagateChecked(isChecked);
  }

  public void addOnSwitchChangeListener(OnSwitchChangeListener listener) {
    if (mSwitchChangeListeners.contains(listener)) {
      throw new IllegalStateException("Cannot add twice the same OnSwitchChangeListener");
    }
    mSwitchChangeListeners.add(listener);
  }

  public void removeOnSwitchChangeListener(OnSwitchChangeListener listener) {
    if (!mSwitchChangeListeners.contains(listener)) {
      throw new IllegalStateException("Cannot remove OnSwitchChangeListener");
    }
    mSwitchChangeListeners.remove(listener);
  }

  @Override
  public Parcelable onSaveInstanceState() {
    Parcelable superState = super.onSaveInstanceState();

    SavedState ss = new SavedState(superState);
    ss.checked = mSwitch.isChecked();
    ss.visible = isShowing();
    return ss;
  }

  @Override
  public void onRestoreInstanceState(Parcelable state) {
    SavedState ss = (SavedState) state;

    super.onRestoreInstanceState(ss.getSuperState());

    mSwitch.setCheckedInternal(ss.checked);
    setTextViewLabel(ss.checked);
    setVisibility(ss.visible ? View.VISIBLE : View.GONE);
    mSwitch.setOnCheckedChangeListener(ss.visible ? this : null);

    requestLayout();
  }

  public interface OnSwitchChangeListener {
    /**
     * Called when the checked state of the Switch has changed.
     *
     * @param switchView The Switch view whose state has changed.
     * @param isChecked The new checked state of switchView.
     */
    void onSwitchChanged(Switch switchView, boolean isChecked);
  }

  static class SavedState extends BaseSavedState {
    public static final Creator<SavedState> CREATOR =
        new Creator<SavedState>() {
          public SavedState createFromParcel(Parcel in) {
            return new SavedState(in);
          }

          public SavedState[] newArray(int size) {
            return new SavedState[size];
          }
        };
    boolean checked;
    boolean visible;

    SavedState(Parcelable superState) {
      super(superState);
    }

    /** Constructor called get {@link #CREATOR} */
    private SavedState(Parcel in) {
      super(in);
      checked = (Boolean) in.readValue(null);
      visible = (Boolean) in.readValue(null);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
      super.writeToParcel(out, flags);
      out.writeValue(checked);
      out.writeValue(visible);
    }

    @Override
    public String toString() {
      return "SwitchBar.SavedState{"
          + Integer.toHexString(System.identityHashCode(this))
          + " checked="
          + checked
          + " visible="
          + visible
          + "}";
    }
  }
}
