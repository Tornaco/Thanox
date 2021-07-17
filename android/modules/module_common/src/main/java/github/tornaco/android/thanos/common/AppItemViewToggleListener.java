package github.tornaco.android.thanos.common;

import github.tornaco.android.thanos.core.pm.AppInfo;

public interface AppItemViewToggleListener {
    void onAppItemSwitchStateChange(AppInfo appInfo, boolean checked);
}
