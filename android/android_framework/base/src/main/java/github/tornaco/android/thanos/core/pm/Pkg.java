package github.tornaco.android.thanos.core.pm;

import android.os.Binder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.UserHandle;

import java.util.Objects;

import github.tornaco.android.thanos.core.annotation.Keep;

@Keep
public class Pkg implements Parcelable {
    private final String pkgName;
    private final int userId;

    public Pkg(String pkgName, int userId) {
        this.pkgName = pkgName;
        this.userId = userId;
    }

    protected Pkg(Parcel in) {
        pkgName = in.readString();
        userId = in.readInt();
    }

    public static final Creator<Pkg> CREATOR = new Creator<Pkg>() {
        @Override
        public Pkg createFromParcel(Parcel in) {
            return new Pkg(in);
        }

        @Override
        public Pkg[] newArray(int size) {
            return new Pkg[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pkgName);
        parcel.writeInt(userId);
    }

    public String getPkgName() {
        return pkgName;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Pkg{" +
                "pkgName='" + pkgName + '\'' +
                ", userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pkg pkg = (Pkg) o;
        return userId == pkg.userId && pkgName.equals(pkg.pkgName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkgName, userId);
    }

    public static Pkg newPkg(String pkgName, int userId) {
        return new Pkg(pkgName, userId);
    }

    public static Pkg from(String pkgName, int uid) {
        return new Pkg(pkgName, UserHandle.getUserId(uid));
    }

    public static Pkg fromAppInfo(AppInfo appInfo) {
        return new Pkg(appInfo.getPkgName(), appInfo.getUserId());
    }

    public static Pkg systemUserPkg(String pkgName) {
        return new Pkg(pkgName, UserHandle.USER_SYSTEM);
    }

    public static Pkg currentUserPkg(String pkgName) {
        return new Pkg(pkgName, UserHandle.getUserId(Binder.getCallingUid()));
    }
}
