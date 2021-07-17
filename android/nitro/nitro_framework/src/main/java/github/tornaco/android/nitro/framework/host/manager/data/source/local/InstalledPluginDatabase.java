package github.tornaco.android.nitro.framework.host.manager.data.source.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import github.tornaco.android.nitro.framework.host.manager.data.converter.ActivityInfoConverter;
import github.tornaco.android.nitro.framework.host.manager.data.converter.ApplicationInfoConverter;
import github.tornaco.android.nitro.framework.host.manager.data.converter.IntentFilterConverter;
import github.tornaco.android.nitro.framework.host.manager.data.model.ActivityIntentFilter;
import github.tornaco.android.nitro.framework.host.manager.data.model.InstalledPlugin;
import github.tornaco.android.nitro.framework.host.manager.data.model.PluginActivityInfo;
import github.tornaco.android.nitro.framework.host.manager.data.model.PluginApplicationInfo;
import util.Singleton2;

@Database(entities = {
        InstalledPlugin.class,
        PluginApplicationInfo.class,
        PluginActivityInfo.class,
        ActivityIntentFilter.class},
        version = 3,
        exportSchema = false)
@TypeConverters({ActivityInfoConverter.class,
        ApplicationInfoConverter.class,
        IntentFilterConverter.class})
public abstract class InstalledPluginDatabase extends RoomDatabase {

    private static final Singleton2<Context, InstalledPluginDatabase> SINGLETON
            = new Singleton2<Context, InstalledPluginDatabase>() {
        @Override
        protected InstalledPluginDatabase create(Context context) {
            return Room
                    .databaseBuilder(context, InstalledPluginDatabase.class, "plugins.db")
                    // Add stable and withHooks
                    .addMigrations(new Migration(1, 2) {
                        @Override
                        public void migrate(@NonNull SupportSQLiteDatabase database) {
                            database.execSQL("ALTER TABLE `installed_plugins`  ADD COLUMN `stable` INTEGER NOT NULL DEFAULT 0");
                            database.execSQL("ALTER TABLE `installed_plugins`  ADD COLUMN `withHooks` INTEGER NOT NULL DEFAULT 1");
                        }
                    })
                    .addMigrations(new Migration(2, 3) {
                        @Override
                        public void migrate(@NonNull SupportSQLiteDatabase database) {
                            database.execSQL("ALTER TABLE `installed_plugins`  ADD COLUMN `statusCallableClass` TEXT DEFAULT ''");
                        }
                    })
                    .allowMainThreadQueries()
                    .build();
        }
    };

    public static InstalledPluginDatabase getInstance(Context context) {
        return SINGLETON.get(context);
    }

    public abstract InstalledPluginDao installedPluginDao();

    public abstract PluginActivityDao pluginActivityDao();

    public abstract PluginAppDao pluginAppDao();

    public abstract ActivityIntentFilterDao intentFilterDao();
}
