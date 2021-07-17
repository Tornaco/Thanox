package github.tornaco.android.nitro.framework.host.manager.data.source.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import github.tornaco.android.nitro.framework.host.manager.data.model.PluginActivityInfo;

@Dao
public interface PluginActivityDao {

    @Query("SELECT * FROM plugin_activity WHERE pluginPackageName = :pkg")
    List<PluginActivityInfo> loadByPackageName(String pkg);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PluginActivityInfo activityInfo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<PluginActivityInfo> activityInfo);

    @Delete
    void delete(PluginActivityInfo activityInfo);
}
