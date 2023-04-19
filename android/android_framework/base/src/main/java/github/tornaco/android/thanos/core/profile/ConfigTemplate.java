package github.tornaco.android.thanos.core.profile;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class ConfigTemplate implements Parcelable {

    private String title;
    private String id;
    private String dummyPackageName;
    private long createAt;

    public ConfigTemplate(String title, String id, String dummyPackageName, long createAt) {
        this.title = title;
        this.id = id;
        this.dummyPackageName = dummyPackageName;
        this.createAt = createAt;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getDummyPackageName() {
        return dummyPackageName;
    }

    public long getCreateAt() {
        return createAt;
    }

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
