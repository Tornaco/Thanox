package github.tornaco.android.nitro.framework.host.manager.data.model;

import android.content.pm.ApplicationInfo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import static androidx.room.ForeignKey.CASCADE;

@Builder
@ToString
@AllArgsConstructor
@Entity(tableName = "plugin_app",
        indices = {@Index("pluginPackageName")},
        foreignKeys = @ForeignKey(
                entity = InstalledPlugin.class,
                parentColumns = "packageName",
                childColumns = "pluginPackageName",
                onDelete = CASCADE
        ))
public class PluginApplicationInfo {

    @PrimaryKey
    @NonNull
    public String pluginPackageName;

    public ApplicationInfo applicationInfo;

    public PluginApplicationInfo() {
    }

}
