package github.tornaco.android.nitro.framework.plugin;

import android.content.pm.PackageInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public final class PluginFile {
    private String name;

    private String packageName;
    private int versionCode;
    private String versionName;
    private String label;

    private String originFile;
    private String apkFile;
    private String dexFile;
    private String libFile;
    private String sourceDir;

    private PackageInfo packageInfo;
}
