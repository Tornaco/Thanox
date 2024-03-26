package github.tornaco.android.thanos.core.secure;

import android.os.Parcel;
import android.os.Parcelable;

import github.tornaco.android.thanos.core.annotation.Keep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Keep
public final class PrivacyCheatRecord implements Parcelable {
    private String packageName;
    private int op;
    private int mode;
    private long timeMills;

    private PrivacyCheatRecord(Parcel in) {
        packageName = in.readString();
        op = in.readInt();
        mode = in.readInt();
        timeMills = in.readLong();
    }

    public static final Creator<PrivacyCheatRecord> CREATOR = new Creator<PrivacyCheatRecord>() {
        @Override
        public PrivacyCheatRecord createFromParcel(Parcel in) {
            return new PrivacyCheatRecord(in);
        }

        @Override
        public PrivacyCheatRecord[] newArray(int size) {
            return new PrivacyCheatRecord[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(packageName);
        parcel.writeInt(op);
        parcel.writeInt(mode);
        parcel.writeLong(timeMills);
    }
}
