package github.tornaco.android.thanos.settings;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.Preference;

import com.elvishew.xlog.XLog;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import github.tornaco.android.thanos.BaseWithFabPreferenceFragmentCompat;
import github.tornaco.android.thanos.BuildConfig;
import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.ThanosApp;
import github.tornaco.android.thanos.app.donate.DonateActivity;
import github.tornaco.android.thanos.app.donate.DonateSettings;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.util.ObjectToStringUtils;
import github.tornaco.android.thanos.core.util.OsUtils;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.module.easteregg.paint.PlatLogoActivity;
import github.tornaco.android.thanos.util.ToastUtils;
import github.tornaco.permission.requester.RequiresPermission;
import github.tornaco.permission.requester.RuntimePermissions;
import util.IoUtils;

@RuntimePermissions
public class AboutSettingsFragment extends BaseWithFabPreferenceFragmentCompat {
    private final static int REQUEST_CODE_EXPORT_MAGISK_FILE_PICKED = 0x100;

    private int buildInfoClickTimes = 0;

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
                            PlatLogoActivity.start(getActivity());
                            Toast.makeText(
                                    getActivity(),
                                    "Thanox is build against Android 11, patching against "
                                            + thanos.getPatchingSource(),
                                    Toast.LENGTH_LONG)
                                    .show();
                            return true;
                        });
        if (thanos.isServiceInstalled()) {
            findPreference(getString(R.string.key_build_info_server))
                    .setSummary(thanos.getVersionName() + "\n" + thanos.fingerPrint());
            findPreference(getString(R.string.key_patch_info)).setOnPreferenceClickListener(preference -> {
                if (OsUtils.isROrAbove()) {
                    ExportPatchUi.show(getActivity(), this::exportMagiskZip);
                }
                return true;
            });
            findPreference(getString(R.string.key_patch_info)).setSummary(thanos.getPatchingSource());
        } else {
            findPreference(getString(R.string.key_build_info_server))
                    .setSummary(R.string.status_not_active);
            findPreference(getString(R.string.key_patch_info)).setSummary("N/A");
        }

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

        findPreference(getString(R.string.key_contributors)).setSummary(BuildProp.THANOX_CONTRIBUTORS);
        donatePref.setVisible(ThanosApp.isPrc());

        findPreference(getString(R.string.key_email)).setSummary(BuildProp.THANOX_CONTACT_EMAIL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        XLog.d("onActivityResult: %s %s %s", requestCode, resultCode, ObjectToStringUtils.intentToString(data));
        if (requestCode == REQUEST_CODE_EXPORT_MAGISK_FILE_PICKED && resultCode == Activity.RESULT_OK) {
            onExportMagiskFilePickRequestResultQ(data);
        }
    }

    private void showBuildProp() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            BuildPropActivity.Starter.INSTANCE.start(getActivity());
        }
    }

    private void exportMagiskZip() {
        AboutSettingsFragmentPermissionRequester.exportMagiskZipRequestedChecked(AboutSettingsFragment.this);
    }

    @RequiresPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void exportMagiskZipRequested() {
        exportMagiskZipQAndAbove();
    }

    private void exportMagiskZipQAndAbove() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // you can set file mime-type
        intent.setType("*/*");
        // default file name
        String backupFileNameWithExt = "magisk-riru-thanox-" + BuildProp.THANOS_VERSION_NAME + ".zip";
        intent.putExtra(Intent.EXTRA_TITLE, backupFileNameWithExt);
        try {
            startActivityForResult(intent, REQUEST_CODE_EXPORT_MAGISK_FILE_PICKED);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(requireContext(), "Activity not found, please install Files app", Toast.LENGTH_LONG).show();
        }
    }

    private void onExportMagiskFilePickRequestResultQ(Intent data) {
        if (data == null) {
            XLog.e("No data.");
            return;
        }

        Uri fileUri = data.getData();

        if (fileUri == null) {
            Toast.makeText(getActivity(), "fileUri == null", Toast.LENGTH_LONG).show();
            XLog.e("No fileUri.");
            return;
        }

        XLog.d("fileUri == %s", fileUri);
        OutputStream os = null;
        try {
            os = requireContext().getContentResolver().openOutputStream(fileUri);
            //noinspection UnstableApiUsage
            Files.asByteSource(
                    new File(Objects.requireNonNull(PkgUtils.getApkPath(requireContext(), requireContext().getPackageName()))))
                    .copyTo(os);

            ToastUtils.ok(requireContext());
        } catch (IOException e) {
            XLog.e(e);
            Toast.makeText(getActivity(), Log.getStackTraceString(e), Toast.LENGTH_LONG).show();
        } finally {
            IoUtils.closeQuietly(os);
        }
    }
}
