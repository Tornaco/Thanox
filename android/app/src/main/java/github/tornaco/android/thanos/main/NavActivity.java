package github.tornaco.android.thanos.main;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.elvishew.xlog.XLog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Arrays;
import java.util.List;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.app.BaseTrustedActivity;
import github.tornaco.android.thanos.app.donate.DonateActivity;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.databinding.ActivityNavBinding;
import github.tornaco.android.thanos.onboarding.OnBoardingActivity;
import github.tornaco.android.thanos.pref.AppPreference;
import github.tornaco.android.thanos.settings.ExportPatchUi;
import github.tornaco.android.thanos.settings.PowerSettingsActivity;
import github.tornaco.android.thanos.settings.SettingsDashboardActivity;
import github.tornaco.android.thanos.util.BrowserUtils;
import github.tornaco.android.thanos.widget.ModernAlertDialog;
import github.tornaco.permission.requester.RequiresPermission;
import github.tornaco.permission.requester.RuntimePermissions;

@RuntimePermissions
public class NavActivity extends BaseTrustedActivity implements NavFragment.FragmentAttachListener {

    private ActivityNavBinding binding;
    private NavViewModel navViewModel;
    private boolean isFirstRunDialogShown;

    private ExportPatchUi exportPatchUi;

    public static void start(Context context) {
        Intent starter = new Intent(context, NavActivity.class);
        context.startActivity(starter);
    }

    @Override
    @Verify
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (NavActivityPlugin.onCreate(this)) {
            finish();
            return;
        }
        binding = ActivityNavBinding.inflate(
                LayoutInflater.from(this), null, false);
        setContentView(binding.getRoot());
        setupView();
        setupPagers(savedInstanceState);
        setupViewModel();
        checkOnBoarding();
        checkPatchSource();
    }

    private void checkOnBoarding() {
        if (!AppPreference.hasOnBoarding(thisActivity())) {
            OnBoardingActivity.Starter.INSTANCE.start(thisActivity());
            finish();
        }
    }

    private void checkPatchSource() {
        ThanosManager.from(thisActivity()).ifServiceInstalled(thanosManager -> {
            List<String> patches = thanosManager.getPatchingSource();
            XLog.d("checkPatchSource: " + Arrays.toString(patches.toArray()));
            if (patches.size() > 1) {
                showMultiplePatchAppliedDialog(patches);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFirstRun();
        navViewModel.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.settings) {
            SettingsDashboardActivity.start(this);
            return true;
        } else if (itemId == R.id.reboot) {
            showRebootDialog();
            return true;
        }
        return false;
    }

    private void showFeedbackDialog() {
        new MaterialAlertDialogBuilder(thisActivity())
                .setTitle(R.string.nav_title_feedback)
                .setMessage(R.string.dialog_message_feedback)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void showRebootDialog() {
        ModernAlertDialog dialog = new ModernAlertDialog(NavActivity.this);
        dialog.setDialogTitle(getString(R.string.reboot_now));
        dialog.setDialogMessage(getString(R.string.common_dialog_message_are_you_sure));
        dialog.setCancelable(true);
        dialog.setPositive(getString(android.R.string.ok));
        dialog.setNegative(getString(android.R.string.cancel));
        dialog.setOnPositive(() -> {
            ThanosManager.from(thisActivity()).getPowerManager().reboot();
        });
        dialog.show();
    }

    private void initFirstRun() {
        if (AppPreference.isFirstRun(getApplication()) && !isFirstRunDialogShown) {
            showAppNoticeDialog();
        }
    }

    private void showAppNoticeDialog() {
        isFirstRunDialogShown = true;
        ModernAlertDialog dialog = new ModernAlertDialog(NavActivity.this);
        dialog.setDialogTitle(getString(R.string.title_app_notice));
        dialog.setDialogMessage(getString(R.string.message_app_notice));
        dialog.setCancelable(false);
        dialog.setPositive(getString(android.R.string.ok));
        dialog.setNegative(getString(android.R.string.cancel));
        dialog.setNeutral(getString(R.string.title_remember));
        dialog.setOnNegative(this::finishAffinity);
        dialog.setOnNeutral(() -> AppPreference.setFirstRun(getApplication(), false));
        dialog.show();
    }

    @Verify
    private void setupViewModel() {
        navViewModel = obtainViewModel(this);
        binding.setViewmodel(navViewModel);
        binding.setStateBadgeClickListener(view -> {
            if (navViewModel.getState().get() == State.RebootNeeded) {
                showRebootPage();
            }
            if (navViewModel.getState().get() == State.InActive) {
                showActiveDialog();
            }
        });
        navViewModel.getState().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (navViewModel.getState().get() == State.RebootNeeded) {
                    showRebootPage();
                }
            }
        });
        binding.setTryingBadgeClickListener(view -> DonateActivity.start(NavActivity.this));
        binding.setFrameworkErrorClickListener(v -> showFrameworkErrorDialog());
        binding.setPowerSaveClickListener(v -> showPowerSaveDialog());
        binding.executePendingBindings();
    }

    private void setupView() {
        setSupportActionBar(binding.toolbar);
        exportPatchUi = ExportPatchUi.from(this);
    }

    @Verify
    private void setupPagers(Bundle savedInstanceState) {
        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new PrebuiltFeatureFragment())
                    .commitAllowingStateLoss();
        } catch (Throwable e) {
            Toast.makeText(this, Log.getStackTraceString(e), Toast.LENGTH_LONG).show();
            recreate();
        }
    }

    private void showRebootPage() {
        // Now navigate to reboot activity.
        NeedToRestartActivity.Starter.INSTANCE.start(thisActivity());
    }

    private void showActiveDialog() {
        new MaterialAlertDialogBuilder(NavActivity.this)
                .setTitle(R.string.status_not_active)
                .setMessage(R.string.message_active_needed)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    // Noop.
                })
                .create()
                .show();
    }

    private void showFrameworkErrorDialog() {
        new MaterialAlertDialogBuilder(NavActivity.this)
                .setTitle(R.string.title_framework_error)
                .setMessage(R.string.message_framework_error)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    // Noop.
                })
                .create()
                .show();
    }

    private void showPowerSaveDialog() {
        new MaterialAlertDialogBuilder(NavActivity.this)
                .setTitle(R.string.dialog_title_battery_drain_fast_by_thanox)
                .setMessage(R.string.dialog_message_battery_drain_fast_by_thanox)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PowerSettingsActivity.start(thisActivity());
                    }
                })
                .create()
                .show();
    }

    private void showMultiplePatchAppliedDialog(List<String> patches) {
        new MaterialAlertDialogBuilder(NavActivity.this)
                .setTitle(R.string.title_multiple_patch_applied_error)
                .setMessage(getString(R.string.message_multiple_patch_applied_error, String.join(", ", patches)))
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    BrowserUtils.launch(thisActivity(), BuildProp.THANOX_URL_DOCS_HOME);
                })
                .create()
                .show();
    }

    @Verify
    public static NavViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(NavViewModel.class);
    }

    @Override
    public void onFragmentAttach(NavFragment fragment) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        exportPatchUi.handleActivityResult(requestCode, resultCode, data);
    }

    @RequiresPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void exportMagiskZipRequested() {
        // Noop, just request perm.
    }
}
