package github.tornaco.android.thanos.plugin;

import androidx.annotation.Keep;

@Keep
public class PluginResponse {
    private String name;
    private String packageName;
    private long versionCode;
    private String versionName;
    private String description;
    private long minThanoxVersion;
    private long updateTimeMills;
    private String downloadUrl;
    private String iconUrl;

    public PluginResponse() {
    }

    public String getName() {
        return this.name;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public long getVersionCode() {
        return this.versionCode;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public String getDescription() {
        return this.description;
    }

    public long getMinThanoxVersion() {
        return this.minThanoxVersion;
    }

    public long getUpdateTimeMills() {
        return this.updateTimeMills;
    }

    public String getDownloadUrl() {
        return this.downloadUrl;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setVersionCode(long versionCode) {
        this.versionCode = versionCode;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMinThanoxVersion(long minThanoxVersion) {
        this.minThanoxVersion = minThanoxVersion;
    }

    public void setUpdateTimeMills(long updateTimeMills) {
        this.updateTimeMills = updateTimeMills;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PluginResponse)) return false;
        final PluginResponse other = (PluginResponse) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$packageName = this.getPackageName();
        final Object other$packageName = other.getPackageName();
        if (this$packageName == null ? other$packageName != null : !this$packageName.equals(other$packageName))
            return false;
        if (this.getVersionCode() != other.getVersionCode()) return false;
        final Object this$versionName = this.getVersionName();
        final Object other$versionName = other.getVersionName();
        if (this$versionName == null ? other$versionName != null : !this$versionName.equals(other$versionName))
            return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description))
            return false;
        if (this.getMinThanoxVersion() != other.getMinThanoxVersion()) return false;
        if (this.getUpdateTimeMills() != other.getUpdateTimeMills()) return false;
        final Object this$downloadUrl = this.getDownloadUrl();
        final Object other$downloadUrl = other.getDownloadUrl();
        if (this$downloadUrl == null ? other$downloadUrl != null : !this$downloadUrl.equals(other$downloadUrl))
            return false;
        final Object this$iconUrl = this.getIconUrl();
        final Object other$iconUrl = other.getIconUrl();
        if (this$iconUrl == null ? other$iconUrl != null : !this$iconUrl.equals(other$iconUrl))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PluginResponse;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $packageName = this.getPackageName();
        result = result * PRIME + ($packageName == null ? 43 : $packageName.hashCode());
        final long $versionCode = this.getVersionCode();
        result = result * PRIME + (int) ($versionCode >>> 32 ^ $versionCode);
        final Object $versionName = this.getVersionName();
        result = result * PRIME + ($versionName == null ? 43 : $versionName.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final long $minThanoxVersion = this.getMinThanoxVersion();
        result = result * PRIME + (int) ($minThanoxVersion >>> 32 ^ $minThanoxVersion);
        final long $updateTimeMills = this.getUpdateTimeMills();
        result = result * PRIME + (int) ($updateTimeMills >>> 32 ^ $updateTimeMills);
        final Object $downloadUrl = this.getDownloadUrl();
        result = result * PRIME + ($downloadUrl == null ? 43 : $downloadUrl.hashCode());
        final Object $iconUrl = this.getIconUrl();
        result = result * PRIME + ($iconUrl == null ? 43 : $iconUrl.hashCode());
        return result;
    }

    public String toString() {
        return "PluginResponse(name=" + this.getName() + ", packageName=" + this.getPackageName() + ", versionCode=" + this.getVersionCode() + ", versionName=" + this.getVersionName() + ", description=" + this.getDescription() + ", minThanoxVersion=" + this.getMinThanoxVersion() + ", updateTimeMills=" + this.getUpdateTimeMills() + ", downloadUrl=" + this.getDownloadUrl() + ", iconUrl=" + this.getIconUrl() + ")";
    }
}
