package github.tornaco.android.thanos.core.profile;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public final class ConfigTemplate implements Parcelable {

    private String title;
    private String id;
    private String dummyPackageName;
    private long createAt;

    private ConfigTemplate(Parcel in) {
        title = in.readString();
        id = in.readString();
        dummyPackageName = in.readString();
        createAt = in.readLong();
    }

    public static final Creator<ConfigTemplate> CREATOR = new Creator<ConfigTemplate>() {
        @Override
        public ConfigTemplate createFromParcel(Parcel in) {
            return new ConfigTemplate(in);
        }

        @Override
        public ConfigTemplate[] newArray(int size) {
            return new ConfigTemplate[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(id);
        parcel.writeString(dummyPackageName);
        parcel.writeLong(createAt);
    }

    public boolean validate() {
        return !TextUtils.isEmpty(id)
                && !TextUtils.isEmpty(dummyPackageName)
                && !TextUtils.isEmpty(title);
    }
}
