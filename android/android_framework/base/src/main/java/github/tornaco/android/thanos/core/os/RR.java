package github.tornaco.android.thanos.core.os;

import android.os.Parcel;
import android.os.Parcelable;

import github.tornaco.android.thanos.core.annotation.Keep;

@Keep
public class RR implements Parcelable {
    public static final int SUCCESS = 0;

    private final int result;
    private final String msg;
    private final String k;
    private final String i;

    protected RR(Parcel in) {
        result = in.readInt();
        msg = in.readString();
        k = in.readString();
        i = in.readString();
    }

    public RR(int result, String msg, String k, String i) {
        this.result = result;
        this.msg = msg;
        this.k = k;
        this.i = i;
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

    public String getI() {
        return i;
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
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(result);
        parcel.writeString(msg);
        parcel.writeString(k);
        parcel.writeString(i);
    }

    public boolean isSuccess() {
        return result == SUCCESS;
    }
}
