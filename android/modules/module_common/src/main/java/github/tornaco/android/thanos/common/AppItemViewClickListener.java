package github.tornaco.android.thanos.common;

import android.view.View;

import github.tornaco.android.thanos.core.pm.AppInfo;

public interface AppItemViewClickListener {
    void onAppItemClick(AppInfo appInfo, View itemView);
}
