package github.tornaco.thanos.android.module.profile;

import androidx.annotation.NonNull;

import github.tornaco.android.thanos.core.profile.RuleInfo;

public interface RuleItemSwitchChangeListener {
    void onItemSwitchChange(@NonNull RuleInfo ruleInfo, boolean checked);
}
