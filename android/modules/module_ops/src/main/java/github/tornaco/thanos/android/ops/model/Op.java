package github.tornaco.thanos.android.ops.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.DrawableRes;

import github.tornaco.android.thanos.core.annotation.Keep;

@Keep
public class Op implements Parcelable {
    private String title;
    private String summary;
    @DrawableRes
    private int iconRes;

    private int code;
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

    public Op(String title, String summary, int iconRes, int code, int mode, boolean remind) {
        this.title = title;
        this.summary = summary;
        this.iconRes = iconRes;
        this.code = code;
        this.mode = mode;
        this.remind = remind;
    }

    public Op() {
    }

    public static OpBuilder builder() {
        return new OpBuilder();
    }

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

    public String getTitle() {
        return this.title;
    }

    public String getSummary() {
        return this.summary;
    }

    public int getIconRes() {
        return this.iconRes;
    }

    public int getCode() {
        return this.code;
    }

    public int getMode() {
        return this.mode;
    }

    public boolean isRemind() {
        return this.remind;
    }

    public String toString() {
        return "Op(title=" + this.getTitle() + ", summary=" + this.getSummary() + ", iconRes=" + this.getIconRes() + ", code=" + this.getCode() + ", mode=" + this.getMode() + ", remind=" + this.isRemind() + ")";
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public static class OpBuilder {
        private String title;
        private String summary;
        private int iconRes;
        private int code;
        private int mode;
        private boolean remind;

        OpBuilder() {
        }

        public Op.OpBuilder title(String title) {
            this.title = title;
            return this;
        }

        public Op.OpBuilder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public Op.OpBuilder iconRes(int iconRes) {
            this.iconRes = iconRes;
            return this;
        }

        public Op.OpBuilder code(int code) {
            this.code = code;
            return this;
        }

        public Op.OpBuilder mode(int mode) {
            this.mode = mode;
            return this;
        }

        public Op.OpBuilder remind(boolean remind) {
            this.remind = remind;
            return this;
        }

        public Op build() {
            return new Op(title, summary, iconRes, code, mode, remind);
        }

        public String toString() {
            return "Op.OpBuilder(title=" + this.title + ", summary=" + this.summary + ", iconRes=" + this.iconRes + ", code=" + this.code + ", mode=" + this.mode + ", remind=" + this.remind + ")";
        }
    }
}
