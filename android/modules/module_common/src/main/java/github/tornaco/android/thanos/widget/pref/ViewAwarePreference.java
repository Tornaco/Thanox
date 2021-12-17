package github.tornaco.android.thanos.widget.pref;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

public class ViewAwarePreference extends Preference {
  private View view;

  public ViewAwarePreference(
      Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  public ViewAwarePreference(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public ViewAwarePreference(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ViewAwarePreference(Context context) {
    super(context);
  }

  @Override
  public void onBindViewHolder(PreferenceViewHolder holder) {
    super.onBindViewHolder(holder);
    this.view = holder.itemView;
  }

  public View getView() {
    return this.view;
  }
}
