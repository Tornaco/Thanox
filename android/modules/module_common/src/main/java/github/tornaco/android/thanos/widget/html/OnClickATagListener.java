package github.tornaco.android.thanos.widget.html;

import android.view.View;

import androidx.annotation.Nullable;

/**
 * This listener can define what happens when the a tag is clicked
 */
public interface OnClickATagListener {
    void onClick(View widget, @Nullable String href);

}
