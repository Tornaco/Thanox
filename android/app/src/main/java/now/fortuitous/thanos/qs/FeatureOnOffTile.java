/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package now.fortuitous.thanos.qs;

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
