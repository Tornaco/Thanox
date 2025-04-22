package github.tornaco.thanos.module.component.manager.model;

import android.content.ComponentName;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import github.tornaco.thanos.module.component.manager.redesign.rule.ComponentRule;
import github.tornaco.thanos.module.component.manager.redesign.rule.BlockerRule;
import util.PinyinComparatorUtils;

public class ComponentModel implements Comparable<ComponentModel> {
    private final String name;
    private final ComponentName componentName;
    private final String label;
    private int enableSetting;
    private final Object componentObject;
    private final boolean isDisabledByThanox;
    private final boolean isRunning;
    private final ComponentRule componentRule;
    @Nullable
    private final BlockerRule blockerRule;

    public ComponentModel(String name,
                          ComponentName componentName,
                          String label,
                          int enableSetting,
                          Object componentObject,
                          boolean isDisabledByThanox,
                          boolean isRunning,
                          ComponentRule componentRule,
                          @Nullable BlockerRule blockerRule) {
        this.name = name;
        this.componentName = componentName;
        this.label = label;
        this.enableSetting = enableSetting;
        this.componentObject = componentObject;
        this.isDisabledByThanox = isDisabledByThanox;
        this.isRunning = isRunning;
        this.componentRule = componentRule;
        this.blockerRule = blockerRule;
    }

    public boolean isDisabled() {
        return enableSetting != PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                && enableSetting != PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
    }

    public boolean isEnabled() {
        return !isDisabled();
    }

    public String getName() {
        return name;
    }

    public ComponentName getComponentName() {
        return componentName;
    }

    public ComponentRule getComponentRule() {
        return componentRule;
    }

    public String getLabel() {
        return label;
    }

    public int getEnableSetting() {
        return enableSetting;
    }

    public Object getComponentObject() {
        return componentObject;
    }

    public boolean isDisabledByThanox() {
        return isDisabledByThanox;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setEnableSetting(int enableSetting) {
        this.enableSetting = enableSetting;
    }

    @Nullable
    public BlockerRule getBlockerRule() {
        return blockerRule;
    }

    @Override
    public int compareTo(@NonNull ComponentModel o) {
        int thisScore = 0;
        int thatScore = 0;

        if (this.isRunning) thisScore += 2;
        if (o.isRunning) thatScore += 2;

        if (this.isDisabled()) thisScore += 1;
        if (o.isDisabled()) thatScore += 1;

        if (thisScore != thatScore) {
            return -Integer.compare(thisScore, thatScore);
        }

        return PinyinComparatorUtils.compare(this.name, o.name);
    }
}
