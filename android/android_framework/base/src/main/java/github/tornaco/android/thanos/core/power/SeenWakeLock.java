package github.tornaco.android.thanos.core.power;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

import github.tornaco.android.thanos.core.annotation.Keep;

@Keep
public class SeenWakeLock implements Parcelable {

    private String tag;
    private int flags;

    private String ownerPackageName;
    private int ownerUserId;

    private long acquireTimeMills;

    private boolean isHeld;
    private boolean isBlock;

    protected SeenWakeLock(Parcel in) {
        tag = in.readString();
        flags = in.readInt();
        ownerPackageName = in.readString();
        ownerUserId = in.readInt();
        acquireTimeMills = in.readLong();
        isHeld = in.readByte() != 0;
        isBlock = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tag);
        dest.writeInt(flags);
        dest.writeString(ownerPackageName);
        dest.writeInt(ownerUserId);
        dest.writeLong(acquireTimeMills);
        dest.writeByte((byte) (isHeld ? 1 : 0));
        dest.writeByte((byte) (isBlock ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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
                && ownerUserId == that.ownerUserId
                && flags == that.flags;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, ownerPackageName);
    }

    @Override
    public String toString() {
        return "SeenWakeLock{" +
                "tag='" + tag + '\'' +
                ", flags=" + flags +
                ", ownerPackageName='" + ownerPackageName + '\'' +
                ", ownerUserId=" + ownerUserId +
                '}';
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public String getOwnerPackageName() {
        return ownerPackageName;
    }

    public void setOwnerPackageName(String ownerPackageName) {
        this.ownerPackageName = ownerPackageName;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public long getAcquireTimeMills() {
        return acquireTimeMills;
    }

    public void setAcquireTimeMills(long acquireTimeMills) {
        this.acquireTimeMills = acquireTimeMills;
    }

    public boolean isHeld() {
        return isHeld;
    }

    public void setHeld(boolean held) {
        isHeld = held;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public void setBlock(boolean block) {
        isBlock = block;
    }

    public SeenWakeLock(String tag, int flags, String ownerPackageName, int ownerUserId, long acquireTimeMills, boolean isHeld, boolean isBlock) {
        this.tag = tag;
        this.flags = flags;
        this.ownerPackageName = ownerPackageName;
        this.ownerUserId = ownerUserId;
        this.acquireTimeMills = acquireTimeMills;
        this.isHeld = isHeld;
        this.isBlock = isBlock;
    }

    public SeenWakeLock() {
    }
}
