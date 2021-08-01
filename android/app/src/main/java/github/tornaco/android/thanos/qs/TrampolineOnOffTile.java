package github.tornaco.android.thanos.qs;

import github.tornaco.android.thanos.core.app.ThanosManager;

public class TrampolineOnOffTile extends FeatureOnOffTile {

    @Override
    boolean isOn() {
        return ThanosManager.from(getApplicationContext())
                .isServiceInstalled()
                && ThanosManager.from(getApplicationContext())
                .getActivityStackSupervisor().isActivityTrampolineEnabled();
    }

    @Override
    void setOn(boolean on) {
        ThanosManager.from(getApplicationContext())
                .ifServiceInstalled(thanosManager -> thanosManager.getActivityStackSupervisor().setActivityTrampolineEnabled(on));
    }
}
