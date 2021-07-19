package github.tornaco.thanos.module.component.manager.model;

import android.content.ComponentName;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import util.PinyinComparatorUtils;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComponentModel implements Comparable<ComponentModel> {
    private String name;
    private ComponentName componentName;
    private String label;
    private int enableSetting;
    private Object componentObject;
    private boolean isDisabledByThanox;
    private boolean isRunning;

    public boolean isDisabled() {
        return enableSetting != PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                && enableSetting != PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
    }

    public String getName() {
        return name;
    }

    public ComponentName getComponentName() {
        return componentName;
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
