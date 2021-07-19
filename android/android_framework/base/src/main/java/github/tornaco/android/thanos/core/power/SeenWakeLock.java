package github.tornaco.android.thanos.core.power;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public final class SeenWakeLock implements Parcelable {

    private String tag;
    private int flags;
    private String ownerPackageName;
    private long acquireTimeMills;

    private SeenWakeLock(Parcel in) {
        tag = in.readString();
        flags = in.readInt();
        ownerPackageName = in.readString();
        acquireTimeMills = in.readLong();
    }

    public static final Creator<SeenWakeLock> CREATOR = new Creator<SeenWakeLock>() {
        @Override
        public SeenWakeLock createFromParcel(Parcel in) {
            return new SeenWakeLock(in);
        }

        @Override
        public SeenWakeLock[] newArray(int size) {
            return new SeenWakeLock[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tag);
        parcel.writeInt(flags);
        parcel.writeString(ownerPackageName);
        parcel.writeLong(acquireTimeMills);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SeenWakeLock that = (SeenWakeLock) o;
        return tag.equals(that.tag)
                && ownerPackageName.equals(that.ownerPackageName)
                && flags == that.flags;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, ownerPackageName);
    }
}
