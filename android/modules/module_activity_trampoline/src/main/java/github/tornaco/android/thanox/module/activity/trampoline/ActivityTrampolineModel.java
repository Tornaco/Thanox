package github.tornaco.android.thanox.module.activity.trampoline;

import androidx.annotation.Nullable;

import github.tornaco.android.thanos.core.app.component.ComponentReplacement;
import github.tornaco.android.thanos.core.pm.AppInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActivityTrampolineModel {
    private ComponentReplacement replacement;
    @Nullable
    private AppInfo app;
}
