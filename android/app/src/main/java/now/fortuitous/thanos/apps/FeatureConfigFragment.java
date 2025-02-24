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

package now.fortuitous.thanos.apps;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.preference.DropDownPreference;
import androidx.preference.Preference;
import androidx.preference.SwitchPreferenceCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;
import java.util.Objects;

import github.tornaco.android.thanos.BasePreferenceFragmentCompat;
import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.AppListItemDescriptionComposer;
import github.tornaco.android.thanos.core.app.ActivityManager;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.core.secure.PrivacyManager.PrivacyOp;
import github.tornaco.android.thanos.core.secure.field.Fields;
import github.tornaco.android.thanos.core.util.ClipboardUtils;
import github.tornaco.android.thanos.support.AppFeatureManager;
import github.tornaco.android.thanos.support.ContextExtKt;
import github.tornaco.android.thanos.util.AppIconLoaderUtil;
import github.tornaco.android.thanos.widget.EditTextDialog;
import github.tornaco.android.thanos.widget.ModernProgressDialog;
import github.tornaco.android.thanos.widget.QuickDropdown;
import github.tornaco.android.thanos.widget.pref.ViewAwarePreference;
import github.tornaco.thanos.android.ops.ops.by.app.AppOpsListActivity;
import github.tornaco.thanos.module.component.manager.redesign.ComponentsActivity;
import now.fortuitous.thanos.XposedScope;
import now.fortuitous.thanos.launchother.AllowListActivity;
import now.fortuitous.thanos.pref.AppPreference;

public class FeatureConfigFragment extends BasePreferenceFragmentCompat {

    private AppInfo appInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appInfo = requireArguments().getParcelable("app");
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.app_feature_config, rootKey);
    }

    @Override
    protected void onBindPreferences() {
        super.onBindPreferences();
        bindAppInfoPref();
        bindFeatureConfigPref();
        bindAppStatePref();
        bindOpsPref();
        bindRecentTaskExcludePref();
        bindPrivDataFieldsPref();
        bindManagePref();
        bindProtectPrefs();
        bindNotificationPrefs();
        bindMiscPrefs();
        bindLaunchOtherAppPref();
        bindSensorOffPref();
    }

    private void bindAppInfoPref() {
        Preference preference = findPreference(getString(R.string.key_app_feature_config_app_info_detailed));
        if (appInfo.isDummy()) {
            Objects.requireNonNull(Objects.requireNonNull(preference).getParent()).setVisible(false);
            return;
        }
        Objects.requireNonNull(preference).setTitle(appInfo.getAppLabel());
        preference.setSummary(new AppListItemDescriptionComposer(requireContext()).getAppItemDescription(appInfo));

        Bitmap iconBitmap = AppIconLoaderUtil.loadAppIconBitmapWithIconPack(getContext(), appInfo.getPkgName(), appInfo.getUid());
        preference.setIcon(new BitmapDrawable(getResources(), iconBitmap));
        preference.setOnPreferenceClickListener(_it -> {
            showAppInfoPopMenu(getListView().getChildAt(2));
            return true;
        });
    }

    private void showAppInfoPopMenu(@NonNull View anchor) {
        PopupMenu popupMenu = new PopupMenu(requireActivity(), anchor);
        popupMenu.inflate(R.menu.feature_config_app_info_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_copy_pkg_name) {
                ClipboardUtils.copyToClipboard(requireContext(), appInfo.getAppLabel(), appInfo.getPkgName());
                Toast.makeText(getContext(), github.tornaco.android.thanos.res.R.string.common_toast_copied_to_clipboard, Toast.LENGTH_SHORT).show();
                return true;
            }
            if (item.getItemId() == R.id.action_launch_app) {
                ThanosManager.from(getActivity()).getPkgManager().launchSmartFreezePkg(Pkg.fromAppInfo(appInfo));
                return true;
            }
            if (item.getItemId() == R.id.action_system_settings) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", appInfo.getPkgName(), null);
                intent.setData(uri);
                getActivity().startActivity(intent);
                return true;
            }
            if (item.getItemId() == R.id.action_system_details) {
                showAppDetailsDialog();
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void showAppDetailsDialog() {
        String details = String.format("%s\n%s\n%s\nUID: %s\nUser: %s\nMin sdk: %s\nTarget sdk: %s\nDebuggable: %s\n\nApk path:%s\n\nData dir:%s\n", appInfo.getPkgName(), appInfo.getVersionName(), appInfo.getVersionCode(), appInfo.getUid(), appInfo.getUserId(), appInfo.getMinSdkVersion(), appInfo.getTargetSdkVersion(), appInfo.isDebuggable(), appInfo.getApkPath(), appInfo.getDataDir());

        new MaterialAlertDialogBuilder(requireActivity()).setTitle(appInfo.getAppLabel()).setMessage(details).show();
    }

    private void bindProtectPrefs() {
        ContextExtKt.withThanos(requireContext(), thanos -> {
            SwitchPreferenceCompat blockUnInstall = findPreference(getString(R.string.key_app_feature_config_block_uninstall));
            Objects.requireNonNull(blockUnInstall).setVisible(ThanosManager.from(getContext()).hasFeature(BuildProp.THANOX_FEATURE_PREVENT_UNINSTALL));
            blockUnInstall.setChecked(thanos.getPkgManager().isPackageBlockUninstallEnabled(appInfo.getPkgName()));
            blockUnInstall.setOnPreferenceChangeListener((preference12, newValue) -> {
                AppFeatureManager.INSTANCE.withSubscriptionStatus(requireContext(), isSubscribed -> {
                    if (isSubscribed) {
                        thanos.getPkgManager().setPackageBlockUninstallEnabled(appInfo.getPkgName(), (Boolean) newValue);
                    } else {
                        AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                    }
                    return null;
                });
                return true;
            });

            SwitchPreferenceCompat blockClearData = findPreference(getString(R.string.key_app_feature_config_block_clear_data));
            Objects.requireNonNull(blockClearData).setVisible(ThanosManager.from(getContext()).hasFeature(BuildProp.THANOX_FEATURE_PREVENT_CLEAR_DATA));
            blockClearData.setChecked(thanos.getPkgManager().isPackageBlockClearDataEnabled(appInfo.getPkgName()));
            blockClearData.setOnPreferenceChangeListener((preference1, newValue) -> {
                AppFeatureManager.INSTANCE.withSubscriptionStatus(requireActivity(), isSubscribed -> {
                    if (isSubscribed) {
                        thanos.getPkgManager().setPackageBlockClearDataEnabled(appInfo.getPkgName(), (Boolean) newValue);
                    } else {
                        AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                    }
                    return null;
                });
                return true;
            });

            SwitchPreferenceCompat blockUpdate = findPreference(getString(R.string.key_app_feature_config_block_update));
            Objects.requireNonNull(blockUpdate).setVisible(ThanosManager.from(getContext()).hasFeature(BuildProp.THANOX_FEATURE_PREVENT_CLEAR_DATA));
            blockUpdate.setChecked(thanos.getPkgManager().isPackageBlockUpdateEnabled(appInfo.getPkgName()));
            blockUpdate.setOnPreferenceChangeListener((preference1, newValue) -> {
                AppFeatureManager.INSTANCE.withSubscriptionStatus(requireActivity(), isSubscribed -> {
                    if (isSubscribed) {
                        thanos.getPkgManager().setPackageBlockUpdateEnabled(appInfo.getPkgName(), (Boolean) newValue);
                    } else {
                        AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                    }
                    return null;
                });
                return true;
            });
            return null;
        });
    }

    private void bindManagePref() {
        ContextExtKt.withThanos(requireContext(), thanos -> {
            Preference preference = findPreference(getString(R.string.key_app_feature_config_a_manage));
            Objects.requireNonNull(preference).setOnPreferenceClickListener(preference1 -> {
                AppFeatureManager.INSTANCE.withSubscriptionStatus(requireActivity(), isSubscribed -> {
                    if (isSubscribed) {
                        ComponentsActivity.startActivity(requireActivity(), appInfo);
                    } else {
                        AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                    }
                    return null;
                });

                return true;
            });
            int ac = thanos.getPkgManager().getActivitiesCount(appInfo.getPkgName());
            preference.setSummary(ac == 0 ? null : String.valueOf(ac));

            preference = findPreference(getString(R.string.key_app_feature_config_r_manage));
            Objects.requireNonNull(preference).setOnPreferenceClickListener(preference1 -> {
                AppFeatureManager.INSTANCE.withSubscriptionStatus(requireActivity(), isSubscribed -> {
                    if (isSubscribed) {
                        ComponentsActivity.startBroadcastReceiver(requireActivity(), appInfo);
                    } else {
                        AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                    }
                    return null;
                });

                return true;
            });

            preference = findPreference(getString(R.string.key_app_feature_config_p_manage));
            Objects.requireNonNull(preference).setOnPreferenceClickListener(preference1 -> {
                AppFeatureManager.INSTANCE.withSubscriptionStatus(requireActivity(), isSubscribed -> {
                    if (isSubscribed) {
                        ComponentsActivity.startProvider(requireActivity(), appInfo);
                    } else {
                        AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                    }
                    return null;
                });
                return true;
            });

            int rc = thanos.getPkgManager().getReceiverCount(appInfo.getPkgName());
            preference.setSummary(rc == 0 ? null : String.valueOf(rc));

            preference = findPreference(getString(R.string.key_app_feature_config_s_manage));
            Objects.requireNonNull(preference).setOnPreferenceClickListener(preference1 -> {
                AppFeatureManager.INSTANCE.withSubscriptionStatus(requireActivity(), isSubscribed -> {
                    if (isSubscribed) {
                        ComponentsActivity.startService(requireActivity(), appInfo);
                    } else {
                        AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                    }
                    return null;
                });
                return true;
            });
            int sc = thanos.getPkgManager().getServiceCount(appInfo.getPkgName());
            preference.setSummary(sc == 0 ? null : String.valueOf(sc));

            if (appInfo.isDummy()) {
                preference.setVisible(false);
                Objects.requireNonNull(preference.getParent()).setVisible(false);
            }

            return null;
        });

    }

    private void bindPrivDataFieldsPref() {
        ContextExtKt.withThanos(requireContext(), thanos -> {

            ViewAwarePreference pref = findPreference(getString(R.string.key_app_feature_config_privacy_cheat));
            Fields currentMode = thanos.getPrivacyManager().getSelectedFieldsProfileForPackage(appInfo.getPkgName(), PrivacyOp.OP_NO_OP);
            String noSet = getString(github.tornaco.android.thanos.res.R.string.common_text_value_not_set);
            Objects.requireNonNull(pref).setSummary(currentMode == null ? noSet : currentMode.getLabel());

            pref.setOnPreferenceClickListener(preference -> {
                ViewAwarePreference vp = (ViewAwarePreference) preference;
                List<Fields> fields = ThanosManager.from(requireContext()).getPrivacyManager().getAllFieldsProfiles();
                Fields dummyNoop = Fields.builder().label(noSet).id(null).build();
                fields.add(dummyNoop);
                QuickDropdown.show(requireActivity(), vp.getView(), index -> {
                    if (index + 1 > fields.size()) {
                        return null;
                    }
                    Fields f = fields.get(index);
                    return f.getLabel();
                }, id -> {
                    Fields f = fields.get(id);
                    boolean isDummyNoop = f.getId() == null;
                    ThanosManager.from(requireContext()).getPrivacyManager().selectFieldsProfileForPackage(appInfo.getPkgName(), isDummyNoop ? null : f.getId());
                    vp.setSummary(isDummyNoop ? noSet : f.getLabel());
                    XposedScope.INSTANCE.requestOrRemoveScope(requireContext(), Pkg.fromAppInfo(appInfo));
                });

                return true;
            });

            return null;
        });
    }

    private void bindRecentTaskExcludePref() {
        ContextExtKt.withThanos(requireContext(), thanos -> {

            DropDownPreference pref = findPreference(getString(R.string.key_recent_task_exclude_settings));

            boolean supportForceInclude = thanos.hasFeature(BuildProp.THANOX_FEATURE_RECENT_TASK_FORCE_INCLUDE);
            Objects.requireNonNull(pref).setEntries(supportForceInclude ? R.array.recent_task_exclude_entry_default_include_exclude : R.array.recent_task_exclude_entry_default_exclude);
            Objects.requireNonNull(pref).setEntryValues(supportForceInclude ? R.array.recent_task_exclude_value_default_include_exclude : R.array.recent_task_exclude_value_default_exclude);

            int currentMode = thanos.getActivityManager().getRecentTaskExcludeSettingForPackage(Pkg.fromAppInfo(appInfo));

            if (!supportForceInclude && currentMode == ActivityManager.ExcludeRecentSetting.INCLUDE) {
                // Force change to default mode since we can not support this mode.
                currentMode = ActivityManager.ExcludeRecentSetting.NONE;
                thanos.getActivityManager().setRecentTaskExcludeSettingForPackage(Pkg.fromAppInfo(appInfo), currentMode);
            }

            pref.setValue(String.valueOf(currentMode));
            pref.setOnPreferenceChangeListener((preference, newValue) -> {
                AppFeatureManager.INSTANCE.withSubscriptionStatus(requireActivity(), isSubscribed -> {
                    if (isSubscribed) {
                        int mode = Integer.parseInt(String.valueOf(newValue));
                        thanos.getActivityManager().setRecentTaskExcludeSettingForPackage(Pkg.fromAppInfo(appInfo), mode);
                    } else {
                        AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                    }
                    return null;
                });
                return true;
            });
            if (appInfo.isDummy()) {
                pref.setVisible(false);
            }

            return null;
        });
    }

    private void bindLaunchOtherAppPref() {
        ContextExtKt.withThanos(requireContext(), thanos -> {

            DropDownPreference pref = findPreference(getString(R.string.key_launch_other_app));
            Preference allowListPref = findPreference(getString(R.string.key_launch_other_app_allow_list_settings));
            int currentMode = thanos.getActivityStackSupervisor().getLaunchOtherAppSetting(Pkg.fromAppInfo(appInfo));
            allowListPref.setVisible(currentMode == ActivityStackSupervisor.LaunchOtherAppPkgSetting.ALLOW_LISTED);

            pref.setEnabled(thanos.getActivityStackSupervisor().isLaunchOtherAppBlockerEnabled());
            pref.setValue(String.valueOf(currentMode));
            pref.setOnPreferenceChangeListener((preference, newValue) -> {
                AppFeatureManager.INSTANCE.withSubscriptionStatus(requireActivity(), isSubscribed -> {
                    if (isSubscribed) {
                        int mode = Integer.parseInt(String.valueOf(newValue));
                        thanos.getActivityStackSupervisor().setLaunchOtherAppSetting(Pkg.fromAppInfo(appInfo), mode);
                        allowListPref.setVisible(mode == ActivityStackSupervisor.LaunchOtherAppPkgSetting.ALLOW_LISTED);
                    } else {
                        AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                    }
                    return null;
                });
                return true;
            });

            allowListPref.setOnPreferenceClickListener(preference -> {
                AllowListActivity.start(requireContext(), appInfo);
                return true;
            });

            return null;
        });
    }

    private void bindSensorOffPref() {
        ContextExtKt.withThanos(requireContext(), thanos -> {
            DropDownPreference pref = findPreference(getString(R.string.key_sensor_off));
            int currentMode = thanos.getPrivacyManager().getSensorOffSettingsForPackage(Pkg.fromAppInfo(appInfo));

            pref.setEnabled(thanos.getPrivacyManager().isSensorOffEnabled());
            pref.setValue(String.valueOf(currentMode));
            pref.setOnPreferenceChangeListener((preference, newValue) -> {
                AppFeatureManager.INSTANCE.withSubscriptionStatus(requireActivity(), isSubscribed -> {
                    if (isSubscribed) {
                        int mode = Integer.parseInt(String.valueOf(newValue));
                        thanos.getPrivacyManager().setSensorOffSettingsForPackage(Pkg.fromAppInfo(appInfo), mode);
                    } else {
                        AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                    }
                    return null;
                });
                return true;
            });
            return null;
        });
    }

    private void bindOpsPref() {
        Preference opsPref = findPreference(getString(R.string.key_app_feature_config_ops));
        Objects.requireNonNull(opsPref).setVisible(ThanosManager.from(getContext()).hasFeature(BuildProp.THANOX_FEATURE_PRIVACY_OPS));
        Objects.requireNonNull(opsPref).setOnPreferenceClickListener(preference -> {
            if (AppPreference.isFeatureNoticeAccepted(requireContext(), "NEW_OPS")) {
                github.tornaco.thanos.android.ops2.byapp.AppOpsListActivity.start(requireContext(), appInfo);
            } else {
                AppOpsListActivity.start(requireContext(), appInfo);
            }
            return true;
        });
    }

    private void bindNotificationPrefs() {
        ContextExtKt.withThanos(requireContext(), thanos -> {

            SwitchPreferenceCompat enablePref = findPreference(getString(R.string.key_app_feature_config_redaction_notification));
            Preference textPref = findPreference(getString(R.string.key_app_feature_config_redaction_notification_text));
            Preference titlePref = findPreference(getString(R.string.key_app_feature_config_redaction_notification_title));

            boolean isEnabled = thanos.getNotificationManager().isPackageRedactionNotificationEnabled(Pkg.fromAppInfo(appInfo));
            Objects.requireNonNull(enablePref).setChecked(isEnabled);
            enablePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    boolean enable = (boolean) newValue;
                    thanos.getNotificationManager().setPackageRedactionNotificationEnabled(Pkg.fromAppInfo(appInfo), enable);
                    Objects.requireNonNull(titlePref).setEnabled(enable);
                    Objects.requireNonNull(textPref).setEnabled(enable);
                    return true;
                }
            });


            Objects.requireNonNull(textPref).setEnabled(isEnabled);
            textPref.setSummary(thanos.getNotificationManager().getPackageRedactionNotificationText(Pkg.fromAppInfo(appInfo)));
            textPref.setOnPreferenceClickListener(preference -> {
                EditTextDialog.show(requireActivity(), getString(github.tornaco.android.thanos.res.R.string.pre_title_redaction_notification_text), thanos.getNotificationManager().getPackageRedactionNotificationText(Pkg.fromAppInfo(appInfo)), newValue -> {
                    thanos.getNotificationManager().setPackageRedactionNotificationText(Pkg.fromAppInfo(appInfo), newValue);
                    String newCurrentValue = thanos.getNotificationManager().getPackageRedactionNotificationText(Pkg.fromAppInfo(appInfo));
                    textPref.setSummary(newCurrentValue);
                });
                return true;
            });

            Objects.requireNonNull(titlePref).setEnabled(isEnabled);
            titlePref.setSummary(thanos.getNotificationManager().getPackageRedactionNotificationTitle(Pkg.fromAppInfo(appInfo)));
            titlePref.setOnPreferenceClickListener(preference -> {
                EditTextDialog.show(requireActivity(), getString(github.tornaco.android.thanos.res.R.string.pre_title_redaction_notification_title), thanos.getNotificationManager().getPackageRedactionNotificationTitle(Pkg.fromAppInfo(appInfo)), newValue -> {
                    thanos.getNotificationManager().setPackageRedactionNotificationTitle(Pkg.fromAppInfo(appInfo), newValue);
                    String newCurrentValue = thanos.getNotificationManager().getPackageRedactionNotificationTitle(Pkg.fromAppInfo(appInfo));
                    titlePref.setSummary(newCurrentValue);
                });
                return true;
            });

            return null;
        });
    }

    private void bindMiscPrefs() {
        SwitchPreferenceCompat dialogForceCancelablePref = findPreference(getString(R.string.key_app_feature_config_dialog_force_cancelable));
        boolean hasFeat = ThanosManager.from(getContext()).hasFeature(BuildProp.THANOX_FEATURE_DIALOG_FORCE_CANCELABLE);
        Objects.requireNonNull(dialogForceCancelablePref).setVisible(hasFeat);
        if (hasFeat) {
            dialogForceCancelablePref.setChecked(ThanosManager.from(getContext()).getWindowManager().isDialogForceCancelable(appInfo.getPkgName()));
            dialogForceCancelablePref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isChecked = (boolean) newValue;
                ThanosManager.from(getContext()).getWindowManager().setDialogForceCancelable(appInfo.getPkgName(), isChecked);
                XposedScope.INSTANCE.requestOrRemoveScope(requireContext(), Pkg.fromAppInfo(appInfo));
                return true;
            });
        }

        SwitchPreferenceCompat residentPref = findPreference(getString(R.string.key_app_feature_config_resident));
        boolean hasResidentFeat = ThanosManager.from(getContext()).hasFeature(BuildProp.THANOX_FEATURE_RESIDENT);
        Objects.requireNonNull(residentPref).setVisible(hasResidentFeat);
        if (hasResidentFeat) {
            residentPref.setChecked(ThanosManager.from(getContext()).getActivityManager().isPkgResident(Pkg.fromAppInfo(appInfo)));
            residentPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isChecked = (boolean) newValue;
                ThanosManager.from(getContext()).getActivityManager().setPkgResident(Pkg.fromAppInfo(appInfo), isChecked);
                return true;
            });
        }
    }

    private void bindFeatureConfigPref() {
        new StartRestrictPref(requireContext()).bind();
        new BgRestrictPref(requireContext()).bind();
        new TaskCleanUp(requireContext()).bind();
        new RecentTaskBlur(requireContext()).bind();
        new ScreenOnNotification(requireContext()).bind();
        new OpRemind(requireContext()).bind();
        new SmartStandBy(requireContext()).bind();
        new AppLock(requireContext()).bind();
    }

    private void bindAppStatePrefDelayed() {
        ModernProgressDialog p = new ModernProgressDialog(getActivity());
        p.setMessage("~~~");
        p.show();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            bindAppStatePref();
            p.dismiss();
        }, 1200);
    }

    private void bindAppStatePref() {
        ContextExtKt.withThanos(requireContext(), thanos -> {
            // Current is disabled.
            Preference toEnablePref = findPreference(getString(R.string.key_app_feature_config_app_to_enable));
            Preference toDisablePref = findPreference(getString(R.string.key_app_feature_config_app_to_disable));
            SwitchPreferenceCompat smartFreezePref = findPreference(getString(R.string.key_app_feature_config_smart_freeze));
            SwitchPreferenceCompat enableOnLaunchPref = findPreference(getString(R.string.key_app_feature_config_enable_package_on_launch));


            boolean disabled = !thanos.getPkgManager().getApplicationEnableState(Pkg.fromAppInfo(appInfo));

            // Enable
            Objects.requireNonNull(toEnablePref).setVisible(!appInfo.isDummy() && disabled);
            toEnablePref.setOnPreferenceClickListener(preference -> {
                thanos.getPkgManager().setApplicationEnableState(Pkg.fromAppInfo(appInfo), true, true);
                // Reload.
                bindAppStatePrefDelayed();
                return true;
            });

            // Disable
            // Current is enabled.
            Objects.requireNonNull(toDisablePref).setVisible(!appInfo.isDummy() && !disabled);
            toDisablePref.setOnPreferenceClickListener(preference -> {
                thanos.getPkgManager().setApplicationEnableState(Pkg.fromAppInfo(appInfo), false, false);
                // Reload.
                bindAppStatePrefDelayed();
                return true;
            });

            // Freeze
            Objects.requireNonNull(smartFreezePref).setChecked(thanos.getPkgManager().isPkgSmartFreezeEnabled(Pkg.fromAppInfo(appInfo)));
            smartFreezePref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean enable = (boolean) newValue;
                thanos.getPkgManager().setPkgSmartFreezeEnabled(Pkg.fromAppInfo(appInfo), enable);
                // Reload.
                // Wait 500ms for app state setup.
                new Handler(Looper.getMainLooper()).postDelayed(FeatureConfigFragment.this::bindAppStatePref, 500);
                return true;
            });

            // EOL
            Objects.requireNonNull(enableOnLaunchPref).setVisible(!appInfo.isDummy());
            Objects.requireNonNull(enableOnLaunchPref).setChecked(thanos.getPkgManager().isEnablePackageOnLaunchRequestEnabled(Pkg.fromAppInfo(appInfo)));
            enableOnLaunchPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean checked = (boolean) newValue;
                thanos.getPkgManager().setEnablePackageOnLaunchRequestEnabled(Pkg.fromAppInfo(appInfo), checked);
                return true;
            });
            return null;
        });
    }

    class StartRestrictPref extends FeaturePref {

        StartRestrictPref(Context context) {
            super(context.getString(R.string.key_app_feature_config_start_restrict));
        }

        @Override
        boolean current() {
            return !ThanosManager.from(getContext()).getActivityManager().isPkgStartBlocking(Pkg.fromAppInfo(appInfo));
        }

        @Override
        void setTo(boolean value) {
            ThanosManager.from(getContext()).getActivityManager().setPkgStartBlockEnabled(Pkg.fromAppInfo(appInfo), !value);
        }
    }

    class BgRestrictPref extends FeaturePref {

        BgRestrictPref(Context context) {
            super(context.getString(R.string.key_app_feature_config_bg_restrict));
        }

        @Override
        boolean current() {
            return !ThanosManager.from(getContext()).getActivityManager().isPkgBgRestricted(appInfo);
        }

        @Override
        void setTo(boolean value) {
            ThanosManager.from(getContext()).getActivityManager().setPkgBgRestrictEnabled(appInfo, !value);
        }
    }

    class RecentTaskBlur extends FeaturePref {

        RecentTaskBlur(Context context) {
            super(context.getString(R.string.key_app_feature_config_recent_task_blur));
        }

        @Override
        boolean current() {
            return ThanosManager.from(getContext()).getActivityManager().isPkgRecentTaskBlurEnabled(Pkg.fromAppInfo(appInfo));
        }

        @Override
        void setTo(boolean value) {
            ThanosManager.from(getContext()).getActivityManager().setPkgRecentTaskBlurEnabled(Pkg.fromAppInfo(appInfo), value);
        }

        @Override
        boolean visible() {
            return ThanosManager.from(getContext()).hasFeature(BuildProp.THANOX_FEATURE_PRIVACY_TASK_BLUR);
        }
    }

    class TaskCleanUp extends FeaturePref {

        TaskCleanUp(Context context) {
            super(context.getString(R.string.key_app_feature_config_task_clean_up));
        }

        @Override
        boolean current() {
            return ThanosManager.from(getContext()).getActivityManager().isPkgCleanUpOnTaskRemovalEnabled(Pkg.fromAppInfo(appInfo));
        }

        @Override
        void setTo(boolean value) {
            ThanosManager.from(getContext()).getActivityManager().setPkgCleanUpOnTaskRemovalEnabled(Pkg.fromAppInfo(appInfo), value);
        }

        @Override
        boolean visible() {
            return true;
        }
    }

    class OpRemind extends FeaturePref {

        OpRemind(Context context) {
            super(context.getString(R.string.key_app_feature_config_op_remind));
        }

        @Override
        boolean current() {
            return ThanosManager.from(getContext()).getAppOpsManager().isPkgOpRemindEnable(appInfo.getPkgName());
        }

        @Override
        void setTo(boolean value) {
            ThanosManager.from(getContext()).getAppOpsManager().setPkgOpRemindEnable(appInfo.getPkgName(), value);
        }

        @Override
        boolean visible() {
            return true;
        }
    }

    class ScreenOnNotification extends FeaturePref {

        ScreenOnNotification(Context context) {
            super(context.getString(R.string.key_app_feature_config_screen_on_notification));
        }

        @Override
        boolean current() {
            return ThanosManager.from(getContext()).getNotificationManager().isScreenOnNotificationEnabledForPkg(appInfo.getPkgName());
        }

        @Override
        void setTo(boolean value) {
            ThanosManager.from(getContext()).getNotificationManager().setScreenOnNotificationEnabledForPkg(appInfo.getPkgName(), value);
        }

        @Override
        boolean visible() {
            return ThanosManager.from(getContext()).hasFeature(BuildProp.THANOX_FEATURE_EXT_N_UP);
        }
    }

    class SmartStandBy extends FeaturePref {

        SmartStandBy(Context context) {
            super(context.getString(R.string.key_app_feature_config_smart_standby));
        }

        @Override
        boolean current() {
            return ThanosManager.from(getContext()).getActivityManager().isPkgSmartStandByEnabled(Pkg.fromAppInfo(appInfo));
        }

        @Override
        void setTo(boolean value) {
            ThanosManager.from(getContext()).getActivityManager().setPkgSmartStandByEnabled(Pkg.fromAppInfo(appInfo), value);
        }

        @Override
        boolean visible() {
            return true;
        }
    }

    class AppLock extends FeaturePref {

        AppLock(Context context) {
            super(context.getString(R.string.key_app_feature_config_app_lock));
        }

        @Override
        boolean current() {
            return ThanosManager.from(getContext()).getActivityStackSupervisor().isPackageLocked(appInfo.getPkgName());
        }

        @Override
        void setTo(boolean value) {
            AppFeatureManager.INSTANCE.withSubscriptionStatus(requireContext(), isSubscribed -> {
                if (isSubscribed) {
                    ThanosManager.from(getContext()).getActivityStackSupervisor().setPackageLocked(appInfo.getPkgName(), value);
                } else {
                    AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                }
                return null;
            });
        }

        @Override
        boolean visible() {
            return ThanosManager.from(getContext()).hasFeature(BuildProp.ACTION_APP_LOCK);
        }
    }

    abstract class FeaturePref {

        private final String key;

        public FeaturePref(String key) {
            this.key = key;
        }

        abstract boolean current();

        abstract void setTo(boolean value);

        boolean visible() {
            return true;
        }

        void bind() {
            SwitchPreferenceCompat preference = findPreference(key);
            Objects.requireNonNull(preference).setVisible(visible());
            Objects.requireNonNull(preference).setChecked(current());
            preference.setOnPreferenceChangeListener((preference1, newValue) -> {
                boolean checked = (boolean) newValue;
                setTo(checked);
                return true;
            });
        }
    }
}
