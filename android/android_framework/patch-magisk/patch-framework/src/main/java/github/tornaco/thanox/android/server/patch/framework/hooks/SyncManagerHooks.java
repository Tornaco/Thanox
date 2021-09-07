package github.tornaco.thanox.android.server.patch.framework.hooks;

import android.content.Context;

import com.android.server.content.ContentService;
import com.android.server.content.SyncManager;
import com.elvishew.xlog.XLog;

import github.tornaco.thanox.android.server.patch.framework.LocalServices;
import util.XposedHelpers;

public class SyncManagerHooks {
    public static void install() {
        try {
            installContextForSyncManager();
        } catch (Throwable e) {
            XLog.e("SyncManagerHooks error install", e);
        }
    }

    private static void installContextForSyncManager() {
        XLog.i("SyncManagerHooks installContextForSyncManager");
        LocalServices.getService(ContentService.Lifecycle.class).ifPresent(lifecycle -> {
            XLog.i("SyncManagerHooks ContentService.Lifecycle: %s", lifecycle);
            ContentService contentService = (ContentService) XposedHelpers.getObjectField(lifecycle, "mService");
            XLog.i("SyncManagerHooks contentService: %s", contentService);
            // Find mSyncManager
            SyncManager syncManager = (SyncManager) XposedHelpers.getObjectField(contentService, "mSyncManager");
            XLog.i("SyncManagerHooks syncManager: %s", syncManager);
            // Update mContext.
            Context context = (Context) XposedHelpers.getObjectField(syncManager, "mContext");
            XLog.i("SyncManagerHooks syncManager.context: %s", context);
            XposedHelpers.setObjectField(syncManager, "mContext", new ContextProxy(context, "SyncManager"));
        });

    }
}
