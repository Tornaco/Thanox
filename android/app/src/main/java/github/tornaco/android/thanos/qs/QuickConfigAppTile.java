package github.tornaco.android.thanos.qs;

import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.annotation.RequiresApi;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;

@RequiresApi(api = Build.VERSION_CODES.N)
public class QuickConfigAppTile extends TileService {
    @Override
    public void onTileAdded() {
        super.onTileAdded();
        XLog.v("onTileAdded");
        updateState();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        XLog.v("onTileRemoved");
    }

    @Override
    public void onClick() {
        super.onClick();
        XLog.d("onClick");
        if (isLocked()) {
            updateState();
            return;
        }
        launchAppDetailsActivity(ThanosManager.from(getApplicationContext()).getActivityStackSupervisor().getCurrentFrontApp());
        updateState();
    }

    public void launchAppDetailsActivity(String pkgName) {
        XLog.d("launchAppDetailsActivity: %s", pkgName);
        Intent viewer = new Intent();
        viewer.setPackage(BuildProp.THANOS_APP_PKG_NAME);
        viewer.setClassName(BuildProp.THANOS_APP_PKG_NAME, BuildProp.ACTIVITY_APP_DETAILS);
        AppInfo appInfo = ThanosManager.from(getApplicationContext()).getPkgManager().getAppInfo(pkgName);
        viewer.putExtra("app", appInfo);
        viewer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityAndCollapse(viewer);
    }

    @Override
    public void onStartListening() {
        updateState();
        super.onStartListening();
        XLog.v("onStartListening");
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        updateState();
        XLog.v("onStopListening");
    }

    private void updateState() {
        if (getQsTile() == null) return;
        getQsTile().setState(Tile.STATE_INACTIVE);
        getQsTile().updateTile();
    }
}
