package github.tornaco.android.thanos.core.pm;

import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

import github.tornaco.android.thanos.core.annotation.DrawableRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import util.PinyinComparatorUtils;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
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
}
