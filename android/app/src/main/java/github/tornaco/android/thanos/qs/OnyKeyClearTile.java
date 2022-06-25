package github.tornaco.android.thanos.qs;

import android.content.Intent;

import github.tornaco.android.thanos.core.T;

public class OnyKeyClearTile extends FeatureOnOffTile {

    @Override
    boolean isOn() {
        return false;
    }

    @Override
    void setOn(boolean on) {
        getApplicationContext().sendBroadcast(new Intent(T.Actions.ACTION_RUNNING_PROCESS_CLEAR));
    }
}
