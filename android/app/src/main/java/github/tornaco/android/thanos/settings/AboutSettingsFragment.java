package github.tornaco.android.thanos.settings;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import github.tornaco.android.thanos.BasePreferenceFragmentCompat;
import github.tornaco.android.thanos.BuildConfig;
import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.ThanosApp;
import github.tornaco.android.thanos.app.donate.DonateActivity;
import github.tornaco.android.thanos.app.donate.DonateSettings;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.util.ClipboardUtils;
import github.tornaco.android.thanos.core.util.OsUtils;
import github.tornaco.android.thanos.module.easteregg.paint.PlatLogoActivity3;
import github.tornaco.android.thanos.util.BrowserUtils;
import github.tornaco.permission.requester.RequiresPermission;
import github.tornaco.permission.requester.RuntimePermissions;

@RuntimePermissions
public class AboutSettingsFragment extends BasePreferenceFragmentCompat {

    private int buildInfoClickTimes = 0;
    private ExportPatchUi exportPatchUi;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        exportPatchUi = ExportPatchUi.from(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.about_settings_pref, rootKey);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onBindPreferences() {
        super.onBindPreferences();
        ThanosManager thanos = ThanosManager.from(getContext());

        // Build.
        findPreference(getString(R.string.key_build_info_app))
                .setSummary(
                        BuildConfig.VERSION_NAME
                                + "\n"
                                + BuildProp.THANOS_BUILD_FINGERPRINT
                                + "\n"
                                + BuildProp.THANOS_BUILD_DATE
                                + "\n"
                                + BuildProp.THANOS_BUILD_HOST);

        findPreference(getString(R.string.key_build_info_app))
                .setOnPreferenceClickListener(
                        preference -> {
                            PlatLogoActivity3.start(getActivity());
                            Toast.makeText(
                                    getActivity(),
                                    "Thanox is build against Android 13", Toast.LENGTH_LONG)
                                    .show();
                            return true;
                        });
        if (thanos.isServiceInstalled()) {
            findPreference(getString(R.string.key_build_info_server))
                    .setSummary(thanos.getVersionName() + "\n" + thanos.fingerPrint());
            findPreference(getString(R.string.key_patch_info)).setSummary(String.join("\n", thanos.getPatchingSource()));
        } else {
            findPreference(getString(R.string.key_build_info_server))
                    .setSummary(R.string.status_not_active);
            findPreference(getString(R.string.key_patch_info)).setSummary("N/A");
        }

        findPreference(getString(R.string.key_patch_info)).setOnPreferenceClickListener(preference -> {
            if (OsUtils.isROrAbove()) {
                exportPatchUi.show(() -> AboutSettingsFragmentPermissionRequester.exportMagiskZipRequestedChecked(AboutSettingsFragment.this));
            }
            return true;
        });

        findPreference(getString(R.string.key_build_info_server))
                .setOnPreferenceClickListener(preference -> {
                    buildInfoClickTimes += 1;
                    if (buildInfoClickTimes >= 10) {
                        buildInfoClickTimes = 0;
                        showBuildProp();
                    }
                    return false;
                });

        // About.
        Preference donatePref = findPreference(getString(R.string.key_donate));
        donatePref.setOnPreferenceClickListener(
                preference -> {
                    DonateActivity.start(getActivity());
                    return true;
                });

        if (DonateSettings.isActivated(getContext())) {
            donatePref.setSummary(R.string.module_donate_donated);
        }

        Preference licensePref = findPreference(getString(R.string.key_open_source_license));
        licensePref.setOnPreferenceClickListener(
                preference12 -> {
                    LicenseHelper.showLicenseDialog(getActivity());
                    return true;
                });

        donatePref.setVisible(ThanosApp.isPrc());

        findPreference(getString(R.string.key_email)).setSummary(BuildProp.THANOX_CONTACT_EMAIL);

        findPreference(getString(R.string.key_rss_e)).setOnPreferenceClickListener(preference -> {
            showTgAndQQDialog();
            return true;
        });
    }

    private void showTgAndQQDialog() {
        new MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.pref_title_rss_e)
                .setMessage(R.string.pref_summary_rss_e)
                .setPositiveButton("QQ", (dialog, which) -> {
                    ClipboardUtils.copyToClipboard(requireActivity(), "thanox QQ", BuildProp.THANOX_QQ_PRIMARY);
                    Toast.makeText(requireContext(), R.string.common_toast_copied_to_clipboard, Toast.LENGTH_LONG).show();
                }).setNegativeButton("TG", (dialog, which) -> BrowserUtils.launch(getActivity(), BuildProp.THANOX_TG_CHANNEL)).show();
    }

    private void showBuildProp() {
        BuildPropActivity.Starter.INSTANCE.start(getActivity());
    }


    @RequiresPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void exportMagiskZipRequested() {
        // Noop, just request perm.
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        exportPatchUi.handleActivityResult(requestCode, resultCode, data);
    }
}
