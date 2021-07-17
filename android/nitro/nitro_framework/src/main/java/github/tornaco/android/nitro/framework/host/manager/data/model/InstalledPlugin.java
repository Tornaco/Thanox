package github.tornaco.android.nitro.framework.host.manager.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@ToString
@Entity(tableName = "installed_plugins", primaryKeys = {"packageName"})
public final class InstalledPlugin implements Parcelable {

    private String name;
    @SuppressWarnings("NullableProblems")
    @NonNull
    private String packageName;
    private int versionCode;
    private String versionName;
    private String label;
    private String description;
    private int minRequiredVersion;

    @ColumnInfo(defaultValue = "0")
    private boolean stable;
    @ColumnInfo(defaultValue = "1")
    private boolean withHooks;

    private String mainActivityName;

    private String originFile;
    private String apkFile;
    private String dexFile;
    private String libFile;
    private String sourceDir;

    @ColumnInfo(defaultValue = "")
    private String statusCallableClass;

    @Ignore
    @Getter
    @Setter
    private List<PluginActivityInfo> pluginActivityInfoList;
    @Getter
    @Ignore
    @Setter
    private PluginApplicationInfo pluginApplicationInfo;
    @Getter
    @Ignore
    @Setter
    private List<ActivityIntentFilter> activityIntentFilterList;

    public InstalledPlugin() {
    }

    @Ignore
    private InstalledPlugin(Parcel in) {
        name = in.readString();
        packageName = in.readString();
        versionCode = in.readInt();
        versionName = in.readString();
        label = in.readString();
        description = in.readString();
        minRequiredVersion = in.readInt();
        stable = in.readInt() == 1;
        withHooks = in.readInt() == 1;
        mainActivityName = in.readString();
        originFile = in.readString();
        apkFile = in.readString();
        dexFile = in.readString();
        libFile = in.readString();
        sourceDir = in.readString();
        statusCallableClass = in.readString();
    }

    public static final Creator<InstalledPlugin> CREATOR = new Creator<InstalledPlugin>() {
        @Override
        public InstalledPlugin createFromParcel(Parcel in) {
            return new InstalledPlugin(in);
        }

        @Override
        public InstalledPlugin[] newArray(int size) {
            return new InstalledPlugin[size];
        }
    };

    public int getMinRequiredVersion() {
        return minRequiredVersion;
    }

    public void setMinRequiredVersion(int minRequiredVersion) {
        this.minRequiredVersion = minRequiredVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(@NonNull String packageName) {
        this.packageName = packageName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMainActivityName() {
        return mainActivityName;
    }

    public void setMainActivityName(String mainActivityName) {
        this.mainActivityName = mainActivityName;
    }

    public String getOriginFile() {
        return originFile;
    }

    public void setOriginFile(String originFile) {
        this.originFile = originFile;
    }

    public String getApkFile() {
        return apkFile;
    }

    public void setApkFile(String apkFile) {
        this.apkFile = apkFile;
    }

    public String getDexFile() {
        return dexFile;
    }

    public void setDexFile(String dexFile) {
        this.dexFile = dexFile;
    }

    public String getLibFile() {
        return libFile;
    }

    public void setLibFile(String libFile) {
        this.libFile = libFile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    public boolean isStable() {
        return stable;
    }

    public void setStable(boolean stable) {
        this.stable = stable;
    }

    public boolean isWithHooks() {
        return withHooks;
    }

    public void setWithHooks(boolean withHooks) {
        this.withHooks = withHooks;
    }

    public String getStatusCallableClass() {
        return statusCallableClass;
    }

    public void setStatusCallableClass(String statusCallableClass) {
        this.statusCallableClass = statusCallableClass;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(packageName);
        dest.writeInt(versionCode);
        dest.writeString(versionName);
        dest.writeString(label);
        dest.writeString(description);
        dest.writeInt(minRequiredVersion);
        dest.writeInt(stable ? 1 : 0);
        dest.writeInt(withHooks ? 1 : 0);
        dest.writeString(mainActivityName);
        dest.writeString(originFile);
        dest.writeString(apkFile);
        dest.writeString(dexFile);
        dest.writeString(libFile);
        dest.writeString(sourceDir);
        dest.writeString(statusCallableClass);
    }
}
