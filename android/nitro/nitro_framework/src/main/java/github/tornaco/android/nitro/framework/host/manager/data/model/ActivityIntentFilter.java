package github.tornaco.android.nitro.framework.host.manager.data.model;

import android.content.IntentFilter;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import static androidx.room.ForeignKey.CASCADE;

@AllArgsConstructor
@Builder
@ToString
@Entity(tableName = "activity_intent_filter",
        indices = {@Index("name")},
        foreignKeys = @ForeignKey(
                entity = PluginActivityInfo.class,
                parentColumns = "name",
                childColumns = "name",
                onDelete = CASCADE
        ))
public class ActivityIntentFilter {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    // component name.
    @NonNull
    public String name;

    @NonNull
    public String pluginPackageName;

    public IntentFilter intentFilter;

    public ActivityIntentFilter() {
    }
}
