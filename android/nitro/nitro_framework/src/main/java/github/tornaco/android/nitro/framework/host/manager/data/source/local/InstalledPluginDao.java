package github.tornaco.android.nitro.framework.host.manager.data.source.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import github.tornaco.android.nitro.framework.host.manager.data.model.InstalledPlugin;

@Dao
public interface InstalledPluginDao {

    @Query("SELECT * FROM installed_plugins WHERE packageName = :pkg limit 1")
    InstalledPlugin loadByPackageName(String pkg);

    @Query("SELECT * FROM installed_plugins WHERE packageName = :pkg AND versionCode = :versionCode limit 1")
    InstalledPlugin loadByPackageNameAndVersionCode(String pkg, int versionCode);

    @Query("SELECT * FROM installed_plugins")
    List<InstalledPlugin> loadAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(InstalledPlugin plugin);

    @Delete
    void delete(InstalledPlugin plugin);
}
