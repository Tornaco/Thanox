package github.tornaco.thanos.android.module.profile;

import android.widget.Switch;

import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import github.tornaco.android.thanos.core.profile.GlobalVar;
import github.tornaco.android.thanos.core.profile.RuleInfo;
import github.tornaco.thanos.android.module.profile.RuleItemSwitchChangeListener;
import github.tornaco.thanos.android.module.profile.RuleUiItem;
import util.Consumer;

public class DataBindingAdapters {

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @BindingAdapter("android:ruleInfoList")
    public static void setRuleInfoList(RecyclerView view, List<github.tornaco.thanos.android.module.profile.RuleUiItem> models) {
        Consumer<List<github.tornaco.thanos.android.module.profile.RuleUiItem>> consumer = (Consumer<List<RuleUiItem>>) view.getAdapter();
        consumer.accept(models);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @BindingAdapter("android:globalVars")
    public static void setGlobalVars(RecyclerView view, List<GlobalVar> models) {
        Consumer<List<GlobalVar>> consumer = (Consumer<List<GlobalVar>>) view.getAdapter();
        consumer.accept(models);
    }

    @BindingAdapter({"android:ruleInfo", "android:ruleSwitchListener"})
    public static void setRuleSwitchListener(SwitchCompat component, RuleInfo ruleInfo, RuleItemSwitchChangeListener listener) {
        component.setOnClickListener(v -> listener.onItemSwitchChange(ruleInfo, component.isChecked()));
    }

    @BindingAdapter({"android:ruleInfo", "android:ruleSwitchListener"})
    public static void setRuleSwitchListener(Switch component, RuleInfo ruleInfo, RuleItemSwitchChangeListener listener) {
        component.setOnClickListener(v -> listener.onItemSwitchChange(ruleInfo, component.isChecked()));
    }
}
