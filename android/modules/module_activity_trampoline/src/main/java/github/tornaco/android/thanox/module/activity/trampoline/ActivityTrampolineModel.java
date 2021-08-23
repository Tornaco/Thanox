package github.tornaco.android.thanox.module.activity.trampoline;

import androidx.annotation.Nullable;

import github.tornaco.android.thanos.core.app.component.ComponentReplacement;
import github.tornaco.android.thanos.core.pm.AppInfo;

public class ActivityTrampolineModel {
    private ComponentReplacement replacement;
    @Nullable
    private AppInfo app;

    public ActivityTrampolineModel(ComponentReplacement replacement, AppInfo app) {
        this.replacement = replacement;
        this.app = app;
    }

    public ComponentReplacement getReplacement() {
        return this.replacement;
    }

    @Nullable
    public AppInfo getApp() {
        return this.app;
    }
}
