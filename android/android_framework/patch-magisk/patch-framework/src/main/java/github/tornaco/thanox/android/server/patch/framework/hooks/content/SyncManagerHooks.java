package github.tornaco.thanox.android.server.patch.framework.hooks.content;

import android.content.Context;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.services.patch.common.content.ContentServiceLifeCycleHelper;
import github.tornaco.thanox.android.server.patch.framework.LocalServices;
import github.tornaco.thanox.android.server.patch.framework.hooks.ContextProxy;
import util.XposedHelpers;

public class SyncManagerHooks {
    public static void install(ClassLoader classLoader) {
        try {
            installContextForSyncManager(classLoader);
        } catch (Throwable e) {
            XLog.e("SyncManagerHooks error install", e);
        }
    }

    private static void installContextForSyncManager(ClassLoader classLoader) {
        XLog.i("SyncManagerHooks installContextForSyncManager");
        new LocalServices(classLoader).getService(ContentServiceLifeCycleHelper.INSTANCE.lifeCycleClass(classLoader)).ifPresent(lifecycle -> {
            XLog.i("SyncManagerHooks ContentService.Lifecycle: %s", lifecycle);
            Object contentService = XposedHelpers.getObjectField(lifecycle, "mService");
            XLog.i("SyncManagerHooks contentService: %s", contentService);
            // Find mSyncManager
            Object syncManager = XposedHelpers.getObjectField(contentService, "mSyncManager");
            XLog.i("SyncManagerHooks syncManager: %s", syncManager);
            // Update mContext.
            Context context = (Context) XposedHelpers.getObjectField(syncManager, "mContext");
            XLog.i("SyncManagerHooks syncManager.context: %s", context);
            XposedHelpers.setObjectField(syncManager, "mContext", new ContextProxy(context, "SyncManager"));
        });

    }
}
