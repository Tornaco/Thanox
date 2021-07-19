package github.tornaco.android.thanos.core.plus;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Remote res.
 */
@AllArgsConstructor
@Getter
@ToString
public final class RR implements Parcelable {
    public static final int SUCCESS = 0;

    private final int result;
    private final String msg;
    private final String k;

    protected RR(Parcel in) {
        result = in.readInt();
        msg = in.readString();
        k = in.readString();
    }

    public static final Creator<RR> CREATOR = new Creator<RR>() {
        @Override
        public RR createFromParcel(Parcel in) {
            return new RR(in);
        }

        @Override
        public RR[] newArray(int size) {
            return new RR[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(result);
        parcel.writeStringNoHelper(msg);
        parcel.writeStringNoHelper(k);
    }

    public boolean isSuccess() {
        return result == SUCCESS;
    }
}
