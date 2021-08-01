package github.tornaco.android.thanos.qs;

import github.tornaco.android.thanos.core.app.ThanosManager;

public class ProfileOnOffTile extends FeatureOnOffTile {

    @Override
    boolean isOn() {
        return ThanosManager.from(getApplicationContext())
                .isServiceInstalled()
                && ThanosManager.from(getApplicationContext())
                .getProfileManager().isProfileEnabled();
    }

    @Override
    void setOn(boolean on) {
        ThanosManager.from(getApplicationContext())
                .ifServiceInstalled(thanosManager -> thanosManager.getProfileManager().setProfileEnabled(on));
    }
}
