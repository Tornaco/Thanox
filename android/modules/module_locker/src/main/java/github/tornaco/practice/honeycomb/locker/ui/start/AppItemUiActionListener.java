package github.tornaco.practice.honeycomb.locker.ui.start;

import github.tornaco.android.thanos.core.pm.AppInfo;

public interface AppItemUiActionListener {
    void onAppItemClick(AppInfo appInfo);

    void onAppItemSwitchStateChange(AppInfo appInfo, boolean checked);
}
