package github.tornaco.android.nitro.framework.host.manager.data.model;

import android.content.pm.ActivityInfo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
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
@Entity(tableName = "plugin_activity",
        indices = {@Index(value = "name", unique = true), @Index("pluginPackageName")},
        foreignKeys = @ForeignKey(
                entity = PluginApplicationInfo.class,
                parentColumns = "pluginPackageName",
                childColumns = "pluginPackageName",
                onDelete = CASCADE
        ))
public class PluginActivityInfo {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    public int id;

    @NonNull
    public String pluginPackageName;

    @NonNull
    public String name;

    public ActivityInfo activityInfo;

    public PluginActivityInfo() {
    }
}
