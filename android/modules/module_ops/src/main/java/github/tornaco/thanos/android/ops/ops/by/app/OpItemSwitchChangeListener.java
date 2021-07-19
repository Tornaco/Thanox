package github.tornaco.thanos.android.ops.ops.by.app;

import androidx.annotation.NonNull;

import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.thanos.android.ops.model.Op;

public interface OpItemSwitchChangeListener {
    void onOpItemSwitchChanged(@NonNull Op op, AppInfo appInfo, int selectedMode);
}
