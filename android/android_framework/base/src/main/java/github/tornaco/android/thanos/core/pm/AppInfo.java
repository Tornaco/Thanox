package github.tornaco.android.thanos.core.pm;

import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

import github.tornaco.android.thanos.core.annotation.DrawableRes;
import util.PinyinComparatorUtils;

public class AppInfo implements Parcelable, Comparable<AppInfo> {
    public static final int STATE_ENABLED = 100;
    public static final int STATE_DISABLED_OR_HIDDEN = 200;

    public static final int FB_STATE_F = 0x1;
    public static final int FB_STATE_B = 0x2;

    public static final int FLAGS_NONE = 0;
    public static final int FLAGS_USER = 1 << 0;
    public static final int FLAGS_SYSTEM = 1 << 1;
    public static final int FLAGS_SYSTEM_UID = 1 << 2;
    public static final int FLAGS_SYSTEM_MEDIA = 1 << 3;
    public static final int FLAGS_SYSTEM_PHONE = 1 << 4;
    public static final int FLAGS_WHITE_LISTED = 1 << 5;
    public static final int FLAGS_WEB_VIEW_PROVIDER = 1 << 6;
    public static final int FLAGS_SHORTCUT_PROXY = 1 << 7;

    public static final int FLAGS_ALL =
            FLAGS_USER
                    | FLAGS_SYSTEM
                    | FLAGS_SYSTEM_UID
                    | FLAGS_SYSTEM_MEDIA
                    | FLAGS_SYSTEM_PHONE
                    | FLAGS_WEB_VIEW_PROVIDER
                    | FLAGS_SHORTCUT_PROXY
                    | FLAGS_WHITE_LISTED;

    private String pkgName;
    private String iconUrl;
    private String appLabel;
    private int versionCode;
    private String versionName;
    private String apkPath;
    private String dataDir;
    private int flags;
    private int uid;

    private int userId;

    public long firstInstallTime;
    public long lastUpdateTime;

    private boolean debuggable;
    private int minSdkVersion;
    private int targetSdkVersion;

    // Enabled or disabled?
    // STATE_ENABLED : STATE_DISABLED_OR_HIDDEN
    private int state;
    // Idle or not?
    private boolean idle;
    private boolean isDummy;
    // Ignore Parcelable
    private boolean isSelected;
    // You can store something here.
    private transient String str;
    // You can store something here.
    private transient Object obj;
    // You can store something here.
    private transient int arg1;
    // You can store something here.
    private transient int arg2;
    // You can store something here.
    private transient long arg3;
    // You can store something here.
    private transient long arg4;
    @DrawableRes
    private transient int iconDrawable;

    private AppInfo(Parcel in) {
        pkgName = in.readString();
        iconUrl = in.readString();
        appLabel = in.readString();
        versionCode = in.readInt();
        versionName = in.readString();
        apkPath = in.readString();
        dataDir = in.readString();
        flags = in.readInt();
        uid = in.readInt();
        userId = in.readInt();
        firstInstallTime = in.readLong();
        lastUpdateTime = in.readLong();
        debuggable = in.readInt() == 1;
        minSdkVersion = in.readInt();
        targetSdkVersion = in.readInt();
        state = in.readInt();
        idle = in.readInt() == 1;
        isDummy = in.readInt() == 1;
    }

    public static final Creator<AppInfo> CREATOR =
            new Creator<AppInfo>() {
                @Override
                public AppInfo createFromParcel(Parcel in) {
                    return new AppInfo(in);
                }

                @Override
                public AppInfo[] newArray(int size) {
                    return new AppInfo[size];
                }
            };

    public AppInfo(String pkgName, String iconUrl, String appLabel,
                   int versionCode, String versionName,
                   String apkPath, String dataDir,
                   int flags,
                   int uid, int userId,
                   long firstInstallTime, long lastUpdateTime,
                   boolean debuggable,
                   int minSdkVersion, int targetSdkVersion,
                   int state, boolean idle,
                   boolean isDummy,
                   boolean isSelected,
                   String str, Object obj, int arg1, int arg2, long arg3, long arg4,
                   int iconDrawable) {
        this.pkgName = pkgName;
        this.iconUrl = iconUrl;
        this.appLabel = appLabel;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.apkPath = apkPath;
        this.dataDir = dataDir;
        this.flags = flags;
        this.uid = uid;
        this.userId = userId;
        this.firstInstallTime = firstInstallTime;
        this.lastUpdateTime = lastUpdateTime;
        this.debuggable = debuggable;
        this.minSdkVersion = minSdkVersion;
        this.targetSdkVersion = targetSdkVersion;
        this.state = state;
        this.idle = idle;
        this.isDummy = isDummy;
        this.isSelected = isSelected;
        this.str = str;
        this.obj = obj;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.arg4 = arg4;
        this.iconDrawable = iconDrawable;
    }

    public AppInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pkgName);
        dest.writeString(this.iconUrl);
        dest.writeString(this.appLabel);
        dest.writeInt(this.versionCode);
        dest.writeString(this.versionName);
        dest.writeString(this.apkPath);
        dest.writeString(this.dataDir);
        dest.writeInt(this.flags);
        dest.writeInt(this.uid);
        dest.writeInt(this.userId);
        dest.writeLong(this.firstInstallTime);
        dest.writeLong(this.lastUpdateTime);
        dest.writeInt(this.debuggable ? 1 : 0);
        dest.writeInt(this.minSdkVersion);
        dest.writeInt(this.targetSdkVersion);
        dest.writeInt(this.state);
        dest.writeInt(this.idle ? 1 : 0);
        dest.writeInt(this.isDummy ? 1 : 0);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(AppInfo appInfo) {
        if (appInfo == null) {
            return -1;
        }
        int thisScore = 0;
        int thatScore = 0;

        if (this.isSelected) {
            thisScore += 2;
        }
        if (appInfo.isSelected) {
            thatScore += 2;
        }
        if (this.disabled()) {
            thisScore += 1;
        }
        if (appInfo.disabled()) {
            thatScore += 1;
        }
        if (thisScore != thatScore) {
            return -Integer.compare(thisScore, thatScore);
        }
        return PinyinComparatorUtils.compare(this.appLabel, appInfo.appLabel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppInfo appInfo = (AppInfo) o;
        return pkgName.equals(appInfo.pkgName) && userId == appInfo.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkgName);
    }

    public boolean disabled() {
        return state == STATE_DISABLED_OR_HIDDEN;
    }

    public boolean isSystemUid() {
        return (FLAGS_SYSTEM_UID & flags) != 0;
    }

    public boolean isMediaUid() {
        return (FLAGS_SYSTEM_MEDIA & flags) != 0;
    }

    public boolean isPhoneUid() {
        return (FLAGS_SYSTEM_PHONE & flags) != 0;
    }

    public static AppInfo dummy() {
        return new AppInfo(
                "com.android.settings",
                null,
                "Dummy",
                0,
                "0",
                "apk path",
                "data dir",
                FLAGS_USER,
                Integer.MAX_VALUE,
                0, // UserId
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                false,
                0,
                0,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                false,
                false,
                true,
                null,
                null,
                0,
                0,
                0L,
                0L,
                -1);
    }

    public String getPkgName() {
        return this.pkgName;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public String getAppLabel() {
        return this.appLabel;
    }

    public int getVersionCode() {
        return this.versionCode;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public String getApkPath() {
        return this.apkPath;
    }

    public String getDataDir() {
        return this.dataDir;
    }

    public int getFlags() {
        return this.flags;
    }

    public int getUid() {
        return this.uid;
    }

    public int getUserId() {
        return this.userId;
    }

    public long getFirstInstallTime() {
        return this.firstInstallTime;
    }

    public long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public boolean isDebuggable() {
        return this.debuggable;
    }

    public int getMinSdkVersion() {
        return this.minSdkVersion;
    }

    public int getTargetSdkVersion() {
        return this.targetSdkVersion;
    }

    public int getState() {
        return this.state;
    }

    public boolean isIdle() {
        return this.idle;
    }

    public boolean isDummy() {
        return this.isDummy;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public String getStr() {
        return this.str;
    }

    public Object getObj() {
        return this.obj;
    }

    public int getArg1() {
        return this.arg1;
    }

    public int getArg2() {
        return this.arg2;
    }

    public long getArg3() {
        return this.arg3;
    }

    public long getArg4() {
        return this.arg4;
    }

    public int getIconDrawable() {
        return this.iconDrawable;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFirstInstallTime(long firstInstallTime) {
        this.firstInstallTime = firstInstallTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setDebuggable(boolean debuggable) {
        this.debuggable = debuggable;
    }

    public void setMinSdkVersion(int minSdkVersion) {
        this.minSdkVersion = minSdkVersion;
    }

    public void setTargetSdkVersion(int targetSdkVersion) {
        this.targetSdkVersion = targetSdkVersion;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setIdle(boolean idle) {
        this.idle = idle;
    }

    public void setDummy(boolean isDummy) {
        this.isDummy = isDummy;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public void setArg1(int arg1) {
        this.arg1 = arg1;
    }

    public void setArg2(int arg2) {
        this.arg2 = arg2;
    }

    public void setArg3(long arg3) {
        this.arg3 = arg3;
    }

    public void setArg4(long arg4) {
        this.arg4 = arg4;
    }

    public void setIconDrawable(int iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public String toString() {
        return "AppInfo(pkgName=" + this.getPkgName() + ", iconUrl=" + this.getIconUrl() + ", appLabel=" + this.getAppLabel()
                + ", versionCode=" + this.getVersionCode() + ", versionName=" + this.getVersionName() + ", apkPath=" + this.getApkPath()
                + ", dataDir=" + this.getDataDir() + ", flags=" + this.getFlags() + ", uid=" + this.getUid() + ", userId=" + this.getUserId()
                + ", firstInstallTime=" + this.getFirstInstallTime() + ", lastUpdateTime=" + this.getLastUpdateTime()
                + ", debuggable=" + this.isDebuggable() + ", minSdkVersion=" + this.getMinSdkVersion() + ", targetSdkVersion="
                + this.getTargetSdkVersion() + ", state=" + this.getState() + ", idle=" + this.isIdle() + ", isDummy="
                + this.isDummy() + ", isSelected=" + this.isSelected() + ", str=" + this.getStr() + ", obj="
                + this.getObj() + ", arg1=" + this.getArg1() + ", arg2=" + this.getArg2() + ", arg3="
                + this.getArg3() + ", arg4=" + this.getArg4() + ", iconDrawable=" + this.getIconDrawable() + ")";
    }
}
