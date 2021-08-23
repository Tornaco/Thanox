package github.tornaco.practice.honeycomb.locker.ui.verify;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import github.tornaco.android.thanos.core.T;
import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor;
import github.tornaco.android.thanos.core.util.Preconditions;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.practice.honeycomb.locker.R;
import github.tornaco.practice.honeycomb.locker.ViewModelFactory;

public class VerifyActivity extends ThemeActivity {
    private int requestCode;
    private String pkg;
    private int method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_locker_verify_activity);

        Intent intent = getIntent();
        Preconditions.checkNotNull(intent, "Intent is null");
        Preconditions.checkState(intent.hasExtra(T.Actions.ACTION_LOCKER_VERIFY_EXTRA_REQUEST_CODE), "No request code");
        Preconditions.checkState(intent.hasExtra(T.Actions.ACTION_LOCKER_VERIFY_EXTRA_PACKAGE), "No pkg");
        requestCode = intent.getIntExtra(T.Actions.ACTION_LOCKER_VERIFY_EXTRA_REQUEST_CODE, Integer.MIN_VALUE);
        pkg = intent.getStringExtra(T.Actions.ACTION_LOCKER_VERIFY_EXTRA_PACKAGE);
        method = intent.getIntExtra(T.Actions.ACTION_LOCKER_VERIFY_EXTRA_METHOD, ActivityStackSupervisor.LockerMethod.NONE);
       // setTitle(ApkUtil.loadNameByPkgName(getApplicationContext(), pkg));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,
                            method == ActivityStackSupervisor.LockerMethod.PATTERN
                                    ? PatternLockVerifyFragment.newInstance()
                                    : PinLockVerifyFragment.newInstance())
                    .commitNow();
        }
    }

    public static VerifyViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
        VerifyViewModel verifyViewModel = ViewModelProviders.of(activity, factory).get(VerifyViewModel.class);
        injectVerifyArgs(activity, verifyViewModel);
        return verifyViewModel;
    }

    @VisibleForTesting
    static void injectVerifyArgs(FragmentActivity activity, VerifyViewModel verifyViewModel) {
        VerifyActivity verifyActivity = (VerifyActivity) activity;
        verifyViewModel.setPkg(verifyActivity.pkg);
        verifyViewModel.setRequestCode(verifyActivity.requestCode);
    }

}
