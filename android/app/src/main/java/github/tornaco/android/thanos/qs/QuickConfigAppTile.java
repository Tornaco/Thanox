package github.tornaco.android.thanos.qs;

import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.annotation.RequiresApi;

import github.tornaco.android.thanos.core.app.ThanosManager;
import com.elvishew.xlog.XLog;

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
        ThanosManager.from(getApplicationContext())
                .ifServiceInstalled(thanosManager -> thanosManager.getActivityManager()
                        .launchAppDetailsActivity(
                                thanosManager.getActivityStackSupervisor().getCurrentFrontApp()));

        QsHelper.collp(this);
        updateState();
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
        getQsTile().setState(Tile.STATE_ACTIVE);
        getQsTile().updateTile();
    }
}
