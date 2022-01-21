package github.tornaco.android.thanos.core.pm;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ComponentNameBrief implements Parcelable {
    @SerializedName("mPackage")
    private final String pkg;
    @SerializedName("mClass")
    private final String cls;

    public ComponentNameBrief(String pkg, String cls) {
        this.pkg = pkg;
        this.cls = cls;
    }

    protected ComponentNameBrief(Parcel in) {
        pkg = in.readString();
        cls = in.readString();
    }

    public static final Creator<ComponentNameBrief> CREATOR = new Creator<ComponentNameBrief>() {
        @Override
        public ComponentNameBrief createFromParcel(Parcel in) {
            return new ComponentNameBrief(in);
        }

        @Override
        public ComponentNameBrief[] newArray(int size) {
            return new ComponentNameBrief[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pkg);
        parcel.writeString(cls);
    }

    public String flattenToString() {
        return pkg + "/" + cls;
    }

    public static @Nullable
    ComponentNameBrief unflattenFromString(@NonNull String str) {
        int sep = str.indexOf('/');
        if (sep < 0 || (sep + 1) >= str.length()) {
            return null;
        }
        String pkg = str.substring(0, sep);
        String cls = str.substring(sep + 1);
        if (cls.length() > 0 && cls.charAt(0) == '.') {
            cls = pkg + cls;
        }
        return new ComponentNameBrief(pkg, cls);
    }

    public String getPackageName() {
        return this.pkg;
    }
}
