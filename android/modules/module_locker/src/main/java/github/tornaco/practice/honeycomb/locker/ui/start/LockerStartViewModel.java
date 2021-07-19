package github.tornaco.practice.honeycomb.locker.ui.start;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import github.tornaco.android.rhino.annotations.Verify;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.practice.honeycomb.locker.ui.setup.SetupActivity;

public class LockerStartViewModel extends AndroidViewModel {

    public LockerStartViewModel(@NonNull Application application) {
        super(application);
    }

    public boolean isCurrentLockMethodKeySet() {
        return ThanosManager.from(getApplication())
                .getActivityStackSupervisor()
                .isLockerKeySet(getLockerMethod());
    }

    public int getLockerMethod() {
        return ThanosManager.from(getApplication())
                .getActivityStackSupervisor()
                .getLockerMethod();
    }

    @Verify
    public void startSetupActivity(int method) {
        SetupActivity.start(getApplication(), method);
    }
}
