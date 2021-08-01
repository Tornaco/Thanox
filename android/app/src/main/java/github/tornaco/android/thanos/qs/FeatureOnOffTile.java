package github.tornaco.android.thanos.qs;

import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.annotation.RequiresApi;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pref.PrefChangeListener;

@RequiresApi(api = Build.VERSION_CODES.N)
public abstract class FeatureOnOffTile extends TileService {

    @Override
    public void onClick() {
        super.onClick();
        setOn(!isOn());
        updateTile();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        ThanosManager.from(getApplicationContext())
                .getPrefManager()
                .registerSettingsChangeListener(new PrefChangeListener() {
                    @Override
                    public void onPrefChanged(String key) {
                        super.onPrefChanged(key);
                        updateTile();
                    }
                });
    }

    private void updateTile() {
        boolean on = isOn();
        getQsTile().setState(on ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        getQsTile().updateTile();
    }

    abstract boolean isOn();

    abstract void setOn(boolean on);
}
