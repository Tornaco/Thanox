package github.tornaco.android.thanos.common;

import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

public interface StateImageProvider {
    @DrawableRes
    int provideImageRes(@NonNull AppListModel model);

    View.OnClickListener provideOnClickListener(@NonNull AppListModel model, int itemIndex);
}
