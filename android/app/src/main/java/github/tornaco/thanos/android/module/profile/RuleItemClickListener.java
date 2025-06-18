package github.tornaco.thanos.android.module.profile;

import androidx.annotation.NonNull;

import github.tornaco.android.thanos.core.profile.RuleInfo;

public interface RuleItemClickListener {
    void onItemClick(@NonNull RuleInfo ruleInfo);
}
