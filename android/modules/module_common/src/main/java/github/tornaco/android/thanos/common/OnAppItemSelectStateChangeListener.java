package github.tornaco.android.thanos.common;

import github.tornaco.android.thanos.core.pm.AppInfo;

public interface OnAppItemSelectStateChangeListener {
    void onAppItemSelectionChanged(AppInfo appInfo, boolean selected);
}
