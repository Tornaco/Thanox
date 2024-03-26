package github.tornaco.android.thanos.core.pm;

import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;

import github.tornaco.android.thanos.core.annotation.Keep;

@Keep
public class ComponentInfo implements Parcelable {
    private final String name;
    private final ComponentName componentName;
    private final String label;
    private final int enableSetting;
    private final boolean isDisabledByThanox;

    public ComponentInfo(String name, ComponentName componentName, String label, int enableSetting, boolean isDisabledByThanox) {
        this.name = name;
        this.componentName = componentName;
        this.label = label;
        this.enableSetting = enableSetting;
        this.isDisabledByThanox = isDisabledByThanox;
    }

    protected ComponentInfo(Parcel in) {
        name = in.readString();
        componentName = in.readParcelable(ComponentName.class.getClassLoader());
        label = in.readString();
        enableSetting = in.readInt();
        isDisabledByThanox = in.readByte() != 0;
    }

    public static final Creator<ComponentInfo> CREATOR = new Creator<ComponentInfo>() {
        @Override
        public ComponentInfo createFromParcel(Parcel in) {
            return new ComponentInfo(in);
        }

        @Override
        public ComponentInfo[] newArray(int size) {
            return new ComponentInfo[size];
        }
    };

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

    public boolean isDisabledByThanox() {
        return isDisabledByThanox;
    }


    @Override
    public String toString() {
        return "ComponentInfo{" +
                "name='" + name + '\'' +
                ", componentName=" + componentName +
                ", label='" + label + '\'' +
                ", enableSetting=" + enableSetting +
                ", isDisabledByThanox=" + isDisabledByThanox +
                '}';
    }

    public static ComponentInfoBuilder builder() {
        return new ComponentInfoBuilder();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeParcelable(componentName, i);
        parcel.writeString(label);
        parcel.writeInt(enableSetting);
        parcel.writeByte((byte) (isDisabledByThanox ? 1 : 0));
    }

    public static class ComponentInfoBuilder {
        private String name;
        private ComponentName componentName;
        private String label;
        private int enableSetting;
        private boolean isDisabledByThanox;

        ComponentInfoBuilder() {
        }

        public ComponentInfo.ComponentInfoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ComponentInfo.ComponentInfoBuilder componentName(ComponentName componentName) {
            this.componentName = componentName;
            return this;
        }

        public ComponentInfo.ComponentInfoBuilder label(String label) {
            this.label = label;
            return this;
        }

        public ComponentInfo.ComponentInfoBuilder enableSetting(int enableSetting) {
            this.enableSetting = enableSetting;
            return this;
        }

        public ComponentInfo.ComponentInfoBuilder isDisabledByThanox(boolean isDisabledByThanox) {
            this.isDisabledByThanox = isDisabledByThanox;
            return this;
        }

        public ComponentInfo build() {
            return new ComponentInfo(name, componentName, label, enableSetting, isDisabledByThanox);
        }
    }
}
