package github.tornaco.thanos.android.ops.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.DrawableRes;
import lombok.*;

@Builder
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Op implements Parcelable {
    private String title;
    private String summary;
    @DrawableRes
    private int iconRes;

    private int code;
    @Setter
    private int mode;

    private boolean remind;

    protected Op(Parcel in) {
        title = in.readString();
        summary = in.readString();
        iconRes = in.readInt();
        code = in.readInt();
        mode = in.readInt();
        remind = in.readInt() == 1;
    }

    public static final Creator<Op> CREATOR = new Creator<Op>() {
        @Override
        public Op createFromParcel(Parcel in) {
            return new Op(in);
        }

        @Override
        public Op[] newArray(int size) {
            return new Op[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(summary);
        dest.writeInt(iconRes);
        dest.writeInt(code);
        dest.writeInt(mode);
        dest.writeInt(remind ? 1 : 0);
    }
}
