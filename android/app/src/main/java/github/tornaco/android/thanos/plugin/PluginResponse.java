package github.tornaco.android.thanos.plugin;

import androidx.annotation.Keep;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
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
}
