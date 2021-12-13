package github.tornaco.thanos.module.component.manager.model;

import android.content.ComponentName;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import util.PinyinComparatorUtils;

public class ComponentModel implements Comparable<ComponentModel> {
    private String name;
    private ComponentName componentName;
    private String label;
    private int enableSetting;
    private Object componentObject;
    private boolean isDisabledByThanox;
    private boolean isRunning;

    public ComponentModel(String name, ComponentName componentName, String label, int enableSetting, Object componentObject, boolean isDisabledByThanox, boolean isRunning) {
        this.name = name;
        this.componentName = componentName;
        this.label = label;
        this.enableSetting = enableSetting;
        this.componentObject = componentObject;
        this.isDisabledByThanox = isDisabledByThanox;
        this.isRunning = isRunning;
    }

    public ComponentModel() {
    }

    public static ComponentModelBuilder builder() {
        return new ComponentModelBuilder();
    }

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

    public void setEnableSetting(int enableSetting) {
        this.enableSetting = enableSetting;
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

    public static class ComponentModelBuilder {
        private String name;
        private ComponentName componentName;
        private String label;
        private int enableSetting;
        private Object componentObject;
        private boolean isDisabledByThanox;
        private boolean isRunning;

        ComponentModelBuilder() {
        }

        public ComponentModel.ComponentModelBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ComponentModel.ComponentModelBuilder componentName(ComponentName componentName) {
            this.componentName = componentName;
            return this;
        }

        public ComponentModel.ComponentModelBuilder label(String label) {
            this.label = label;
            return this;
        }

        public ComponentModel.ComponentModelBuilder enableSetting(int enableSetting) {
            this.enableSetting = enableSetting;
            return this;
        }

        public ComponentModel.ComponentModelBuilder componentObject(Object componentObject) {
            this.componentObject = componentObject;
            return this;
        }

        public ComponentModel.ComponentModelBuilder isDisabledByThanox(boolean isDisabledByThanox) {
            this.isDisabledByThanox = isDisabledByThanox;
            return this;
        }

        public ComponentModel.ComponentModelBuilder isRunning(boolean isRunning) {
            this.isRunning = isRunning;
            return this;
        }

        public ComponentModel build() {
            return new ComponentModel(name, componentName, label, enableSetting, componentObject, isDisabledByThanox, isRunning);
        }

        public String toString() {
            return "ComponentModel.ComponentModelBuilder(name=" + this.name + ", componentName=" + this.componentName + ", label=" + this.label + ", enableSetting=" + this.enableSetting + ", componentObject=" + this.componentObject + ", isDisabledByThanox=" + this.isDisabledByThanox + ", isRunning=" + this.isRunning + ")";
        }
    }
}
