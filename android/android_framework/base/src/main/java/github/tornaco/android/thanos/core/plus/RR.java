package github.tornaco.android.thanos.core.plus;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Remote res.
 */
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

    public RR(int result, String msg, String k) {
        this.result = result;
        this.msg = msg;
        this.k = k;
    }

    public int getResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }

    public String getK() {
        return k;
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
        parcel.writeString(msg);
        parcel.writeString(k);
    }

    public boolean isSuccess() {
        return result == SUCCESS;
    }
}
