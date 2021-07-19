package github.tornaco.android.thanos.core.su;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
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
}
