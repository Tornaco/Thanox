package github.tornaco.android.thanos.core.os;

import android.os.Binder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.UserHandle;

import java.util.Objects;

public class ProcessName implements Parcelable {
    private final String name;
    private final int userId;

    public ProcessName(String name, int userId) {
        this.name = name;
        this.userId = userId;
    }

    protected ProcessName(Parcel in) {
        name = in.readString();
        userId = in.readInt();
    }

    public static final Creator<ProcessName> CREATOR = new Creator<ProcessName>() {
        @Override
        public ProcessName createFromParcel(Parcel in) {
            return new ProcessName(in);
        }

        @Override
        public ProcessName[] newArray(int size) {
            return new ProcessName[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(userId);
    }

    public String getName() {
        return name;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "ProcessName{" + "name='" + name + '\'' + ", userId=" + userId + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessName pn = (ProcessName) o;
        return userId == pn.userId && name.equals(pn.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, userId);
    }

    public static ProcessName from(String name, int uid) {
        return new ProcessName(name, UserHandle.getUserId(uid));
    }

    public static ProcessName systemUserProcess(String name) {
        return new ProcessName(name, UserHandle.USER_SYSTEM);
    }

    public static ProcessName currentUserProcess(String name) {
        return new ProcessName(name, UserHandle.getUserId(Binder.getCallingUid()));
    }
}
