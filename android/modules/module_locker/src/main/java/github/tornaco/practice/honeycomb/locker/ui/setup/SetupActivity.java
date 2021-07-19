package github.tornaco.practice.honeycomb.locker.ui.setup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.practice.honeycomb.locker.R;
import github.tornaco.practice.honeycomb.locker.ViewModelFactory;

public class SetupActivity extends ThemeActivity {

    public static void start(Context context, int method) {
        Intent starter = new Intent(context, SetupActivity.class);
        starter.putExtra("method", method);
        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_locker_verify_activity);

        int method = getIntent().getIntExtra("method", ActivityStackSupervisor.LockerMethod.PIN);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,
                            method == ActivityStackSupervisor.LockerMethod.PATTERN
                                    ? PatternLockSetupFragment.newInstance()
                                    : PinLockSetupFragment.newInstance())
                    .commitNow();
        }
    }

    public static SetupViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(SetupViewModel.class);
    }
}
