package github.tornaco.android.thanos.core.su;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public final class SuRes implements Parcelable {
    /**
     * Shell result output.
     */
    private List<String> out;
    /**
     * Shell error output.
     */
    private List<String> err;
    /**
     * Return code of the cmd.
     */
    private int code;

    private SuRes(Parcel in) {
        out = in.createStringArrayList();
        err = in.createStringArrayList();
        code = in.readInt();
    }

    public SuRes(List<String> out, List<String> err, int code) {
        this.out = out;
        this.err = err;
        this.code = code;
    }

    public SuRes() {
    }

    public List<String> getOut() {
        return out;
    }

    public void setOut(List<String> out) {
        this.out = out;
    }

    public List<String> getErr() {
        return err;
    }

    public void setErr(List<String> err) {
        this.err = err;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static final Creator<SuRes> CREATOR = new Creator<SuRes>() {
        @Override
        public SuRes createFromParcel(Parcel in) {
            return new SuRes(in);
        }

        @Override
        public SuRes[] newArray(int size) {
            return new SuRes[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(out);
        parcel.writeStringList(err);
        parcel.writeInt(code);
    }

    @Override
    public String toString() {
        return "SuRes{" +
                "out=" + out +
                ", err=" + err +
                ", code=" + code +
                '}';
    }
}
