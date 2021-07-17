package github.tornaco.android.nitro.framework.host.manager.data.source.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import github.tornaco.android.nitro.framework.host.manager.data.model.PluginApplicationInfo;

@Dao
public interface PluginAppDao {

    @Query("SELECT * FROM plugin_app WHERE pluginPackageName = :pkg")
    PluginApplicationInfo loadByPackageName(String pkg);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PluginApplicationInfo app);

    @Delete
    void delete(PluginApplicationInfo app);
}
