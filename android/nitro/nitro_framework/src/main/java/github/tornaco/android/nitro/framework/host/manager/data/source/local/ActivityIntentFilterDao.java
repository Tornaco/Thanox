package github.tornaco.android.nitro.framework.host.manager.data.source.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import github.tornaco.android.nitro.framework.host.manager.data.model.ActivityIntentFilter;

@Dao
public interface ActivityIntentFilterDao {

    @Query("SELECT * FROM activity_intent_filter WHERE pluginPackageName = :pkg AND name = :componentNameString")
    List<ActivityIntentFilter> loadByPackageNameAndComponentName(String pkg, String componentNameString);

    @Query("SELECT * FROM activity_intent_filter WHERE pluginPackageName = :pkg")
    List<ActivityIntentFilter> loadByPackageName(String pkg);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ActivityIntentFilter intentFilter);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<ActivityIntentFilter> intentFilter);

    @Delete
    void delete(ActivityIntentFilter intentFilter);
}
