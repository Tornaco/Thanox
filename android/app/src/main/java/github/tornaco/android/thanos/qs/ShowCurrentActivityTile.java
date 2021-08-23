package github.tornaco.android.thanos.qs;

import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.annotation.RequiresApi;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pref.PrefChangeListener;
import util.Consumer;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ShowCurrentActivityTile extends TileService {
    @Override
    public void onClick() {
        super.onClick();
        toggle();
        updateState();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        updateState();

        ThanosManager.from(getApplicationContext())
                .ifServiceInstalled(new Consumer<ThanosManager>() {
                    @Override
                    public void accept(ThanosManager thanosManager) {
                        thanosManager.getPrefManager().registerSettingsChangeListener(new PrefChangeListener() {
                            @Override
                            public void onPrefChanged(String key) {
                                super.onPrefChanged(key);
                                XLog.v("ShowCurrentActivityTile.onPrefChanged: " + key);
                                updateState();
                            }
                        });
                    }
                });
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        updateState();
    }

    private void toggle() {
        ThanosManager.from(getApplicationContext())
                .ifServiceInstalled(thanosManager ->
                        thanosManager.getActivityStackSupervisor()
                                .setShowCurrentComponentViewEnabled(
                                        !thanosManager.getActivityStackSupervisor()
                                                .isShowCurrentComponentViewEnabled()));
    }

    private void updateState() {
        ThanosManager thanosManager = ThanosManager.from(getApplicationContext());
        if (!thanosManager.isServiceInstalled()) return;
        boolean enabled = thanosManager.getActivityStackSupervisor().isShowCurrentComponentViewEnabled();
        getQsTile().setState(enabled ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        getQsTile().updateTile();
    }
}
