package github.tornaco.android.thanos.apps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import java.util.List;
import java.util.Objects;

import github.tornaco.android.common.util.ApkUtil;
import github.tornaco.android.thanos.BaseWithFabPreferenceFragmentCompat;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.ThanosApp;
import github.tornaco.android.thanos.app.donate.DonateSettings;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.secure.PrivacyManager.PrivacyOp;
import github.tornaco.android.thanos.core.secure.field.Fields;
import github.tornaco.android.thanos.core.util.ClipboardUtils;
import github.tornaco.android.thanos.core.util.function.Function;
import github.tornaco.android.thanos.widget.QuickDropdown;
import github.tornaco.android.thanos.widget.pref.ViewAwarePreference;
import github.tornaco.thanos.android.ops.ops.by.app.AppOpsListActivity;
import github.tornaco.thanos.module.component.manager.ActivityListActivity;
import github.tornaco.thanos.module.component.manager.ReceiverListActivity;
import github.tornaco.thanos.module.component.manager.ServiceListActivity;
import util.Consumer;

public class FeatureConfigFragment extends BaseWithFabPreferenceFragmentCompat {

  private AppInfo appInfo;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    appInfo = Objects.requireNonNull(getArguments()).getParcelable("app");
  }

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.app_feature_config, rootKey);
  }

  @Override
  protected void onBindPreferences() {
    super.onBindPreferences();
    ThanosManager thanos = ThanosManager.from(getContext());
    if (!thanos.isServiceInstalled()) {
      getPreferenceScreen().setEnabled(false);
      return;
    }

    bindAppInfoPref();
    bindFeatureConfigPref();
    bindAppStatePref();
    bindOpsPref();
    bindRecentTaskExcludePref();
    bindPrivDataFieldsPref();
    bindManagePref();
    bindProtectPrefs();
  }

  private void bindAppInfoPref() {
    Preference preference =
        findPreference(getString(R.string.key_app_feature_config_app_info_detailed));
    if (appInfo.isDummy()) {
      Objects.requireNonNull(Objects.requireNonNull(preference).getParent()).setVisible(false);
      return;
    }
    Objects.requireNonNull(preference).setTitle(appInfo.getAppLabel());
    preference.setSummary(
        String.format(
            "%s\n%s\n%s\nUID: %s\nMin sdk: %s\nTarget sdk: %s\nDebuggable: %s",
            appInfo.getPkgName(),
            appInfo.getVersionName(),
            appInfo.getVersionCode(),
            appInfo.getUid(),
            appInfo.getMinSdkVersion(),
            appInfo.getTargetSdkVersion(),
            appInfo.isDebuggable()));
    //noinspection ConstantConditions
    preference.setIcon(ApkUtil.loadIconByPkgName(getContext(), appInfo.getPkgName()));
    preference.setOnPreferenceClickListener(
        _it -> {
          showAppInfoPopMenu(getListView().getChildAt(2));
          return true;
        });
  }

  private void showAppInfoPopMenu(@NonNull View anchor) {
    PopupMenu popupMenu = new PopupMenu(Objects.requireNonNull(getActivity()), anchor);
    popupMenu.inflate(R.menu.feature_config_app_info_menu);
    popupMenu.setOnMenuItemClickListener(
        item -> {
          if (item.getItemId() == R.id.action_copy_pkg_name) {
            ClipboardUtils.copyToClipboard(
                Objects.requireNonNull(getContext()), appInfo.getAppLabel(), appInfo.getPkgName());
            Toast.makeText(
                getContext(),
                github
                    .tornaco
                    .android
                    .thanos
                    .module
                    .common
                    .R
                    .string
                    .common_toast_copied_to_clipboard,
                Toast.LENGTH_SHORT)
                .show();
            return true;
          }
          if (item.getItemId() == R.id.action_launch_app) {
            ThanosManager.from(getActivity())
                .getPkgManager()
                .launchSmartFreezePkg(appInfo.getPkgName());
            return true;
          }
          if (item.getItemId() == R.id.action_system_settings) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", appInfo.getPkgName(), null);
            intent.setData(uri);
            getActivity().startActivity(intent);
            return true;
          }
          return false;
        });
    popupMenu.show();
  }

  private void bindProtectPrefs() {
    ThanosManager thanos = ThanosManager.from(getContext());
    SwitchPreferenceCompat preference =
        findPreference(getString(R.string.key_app_feature_config_block_uninstall));
    Objects.requireNonNull(preference)
        .setChecked(thanos.getPkgManager().isPackageBlockUninstallEnabled(appInfo.getPkgName()));
    preference.setOnPreferenceChangeListener(
        new Preference.OnPreferenceChangeListener() {
          @Override
          public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(getActivity())) {
              Toast.makeText(
                  getActivity(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT)
                  .show();
              return false;
            }
            thanos
                .getPkgManager()
                .setPackageBlockUninstallEnabled(appInfo.getPkgName(), (Boolean) newValue);
            return true;
          }
        });

    preference = findPreference(getString(R.string.key_app_feature_config_block_clear_data));
    Objects.requireNonNull(preference)
        .setChecked(thanos.getPkgManager().isPackageBlockClearDataEnabled(appInfo.getPkgName()));
    preference.setOnPreferenceChangeListener(
        new Preference.OnPreferenceChangeListener() {
          @Override
          public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(getActivity())) {
              Toast.makeText(
                  getActivity(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT)
                  .show();
              return false;
            }
            thanos
                .getPkgManager()
                .setPackageBlockClearDataEnabled(appInfo.getPkgName(), (Boolean) newValue);
            return true;
          }
        });
  }

  private void bindManagePref() {
    ThanosManager thanos = ThanosManager.from(getContext());
    Preference preference = findPreference(getString(R.string.key_app_feature_config_a_manage));
    Objects.requireNonNull(preference)
        .setOnPreferenceClickListener(
            preference1 -> {
              if (ThanosApp.isPrc() && !DonateSettings.isActivated(getActivity())) {
                Toast.makeText(
                    getActivity(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT)
                    .show();
                return false;
              }
              ActivityListActivity.start(Objects.requireNonNull(getActivity()), appInfo);
              return true;
            });
    int ac = thanos.getPkgManager().getActivitiesCount(appInfo.getPkgName());
    preference.setSummary(ac == 0 ? null : String.valueOf(ac));

    preference = findPreference(getString(R.string.key_app_feature_config_r_manage));
    Objects.requireNonNull(preference)
        .setOnPreferenceClickListener(
            preference1 -> {
              if (ThanosApp.isPrc() && !DonateSettings.isActivated(getActivity())) {
                Toast.makeText(
                    getActivity(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT)
                    .show();
                return false;
              }
              ReceiverListActivity.start(Objects.requireNonNull(getActivity()), appInfo);
              return true;
            });
    int rc = thanos.getPkgManager().getReceiverCount(appInfo.getPkgName());
    preference.setSummary(rc == 0 ? null : String.valueOf(rc));

    preference = findPreference(getString(R.string.key_app_feature_config_s_manage));
    Objects.requireNonNull(preference)
        .setOnPreferenceClickListener(
            preference1 -> {
              if (ThanosApp.isPrc() && !DonateSettings.isActivated(getActivity())) {
                Toast.makeText(
                    getActivity(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT)
                    .show();
                return false;
              }
              ServiceListActivity.start(Objects.requireNonNull(getActivity()), appInfo);
              return true;
            });
    int sc = thanos.getPkgManager().getServiceCount(appInfo.getPkgName());
    preference.setSummary(sc == 0 ? null : String.valueOf(sc));

    if (appInfo.isDummy()) {
      preference.setVisible(false);
      Objects.requireNonNull(preference.getParent()).setVisible(false);
    }
  }

  private void bindPrivDataFieldsPref() {
    ThanosManager thanos = ThanosManager.from(getContext());
    ViewAwarePreference pref =
        findPreference(getString(R.string.key_app_feature_config_privacy_cheat));
    Fields currentMode =
        thanos.getPrivacyManager()
            .getSelectedFieldsProfileForPackage(appInfo.getPkgName(), PrivacyOp.OP_NO_OP);
    String noSet = getString(R.string.common_text_value_not_set);
    Objects.requireNonNull(pref).setSummary(currentMode == null ? noSet : currentMode.getLabel());

    pref.setOnPreferenceClickListener(
        new Preference.OnPreferenceClickListener() {
          @Override
          public boolean onPreferenceClick(Preference preference) {
            ViewAwarePreference vp = (ViewAwarePreference) preference;
            List<Fields> fields =
                ThanosManager.from(requireContext()).getPrivacyManager().getAllFieldsProfiles();
            Fields dummyNoop = Fields.builder().label(noSet).id(null).build();
            fields.add(dummyNoop);
            QuickDropdown.show(
                requireActivity(),
                vp.getView(),
                new Function<Integer, String>() {
                  @Override
                  public String apply(Integer index) {
                    if (index + 1 > fields.size()) {
                      return null;
                    }
                    Fields f = fields.get(index);
                    return f.getLabel();
                  }
                },
                new Consumer<Integer>() {
                  @Override
                  public void accept(Integer id) {
                    Fields f = fields.get(id);
                    boolean isDummyNoop = f.getId() == null;
                    ThanosManager.from(requireContext())
                        .getPrivacyManager()
                        .selectFieldsProfileForPackage(
                            appInfo.getPkgName(), isDummyNoop ? null : f.getId());
                    vp.setSummary(isDummyNoop ? noSet : f.getLabel());
                  }
                });

            return true;
          }
        });
  }

  private void bindRecentTaskExcludePref() {
    ThanosManager thanos = ThanosManager.from(getContext());
    DropDownPreference pref = findPreference(getString(R.string.key_recent_task_exclude_settings));
    int currentMode =
        thanos.getActivityManager().getRecentTaskExcludeSettingForPackage(appInfo.getPkgName());
    Objects.requireNonNull(pref).setValue(String.valueOf(currentMode));
    pref.setOnPreferenceChangeListener(
        (preference, newValue) -> {
          if (ThanosApp.isPrc() && !DonateSettings.isActivated(getActivity())) {
            Toast.makeText(
                getActivity(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT)
                .show();
            return false;
          }
          int mode = Integer.parseInt(String.valueOf(newValue));
          thanos
              .getActivityManager()
              .setRecentTaskExcludeSettingForPackage(appInfo.getPkgName(), mode);
          return true;
        });
    if (appInfo.isDummy()) {
      pref.setVisible(false);
    }
  }

  private void bindOpsPref() {
    Preference opsPref = findPreference(getString(R.string.key_app_feature_config_ops));
    Objects.requireNonNull(opsPref)
        .setOnPreferenceClickListener(
            preference -> {
              AppOpsListActivity.start(Objects.requireNonNull(getContext()), appInfo);
              return true;
            });
  }

  private void bindFeatureConfigPref() {
    new StartRestrictPref(Objects.requireNonNull(getContext())).bind();
    new BgRestrictPref(getContext()).bind();
    new TaskCleanUp(getContext()).bind();
    new RecentTaskBlur(getContext()).bind();
    new ScreenOnNotification(getContext()).bind();
    new OpRemind(getContext()).bind();
    new SmartStandBy(getContext()).bind();
    new AppLock(getContext()).bind();
  }

  private void bindAppStatePrefDelayed() {
    ProgressDialog p = new ProgressDialog(getActivity());
    p.setMessage("~~~");
    p.show();
    new Handler(Looper.getMainLooper())
            .postDelayed(() -> {
              bindAppStatePref();
              p.dismiss();
            }, 1200);
  }

  private void bindAppStatePref() {
    // Current is disabled.
    Preference toEnablePref =
        findPreference(getString(R.string.key_app_feature_config_app_to_enable));
    Preference toDisablePref =
        findPreference(getString(R.string.key_app_feature_config_app_to_disable));
    SwitchPreferenceCompat smartFreezePref =
        findPreference(getString(R.string.key_app_feature_config_smart_freeze));

    if (appInfo.isDummy()) {
      Objects.requireNonNull(toEnablePref).setVisible(false);
      Objects.requireNonNull(toDisablePref).setVisible(false);
      Objects.requireNonNull(smartFreezePref).setVisible(false);
      Objects.requireNonNull(toEnablePref.getParent()).setVisible(false);
      return;
    }

    ThanosManager thanos = ThanosManager.from(getContext());
    boolean disabled = !thanos.getPkgManager().getApplicationEnableState(appInfo.getPkgName());

    Objects.requireNonNull(toEnablePref).setVisible(disabled);
    toEnablePref.setOnPreferenceClickListener(
        preference -> {
          thanos
              .getPkgManager()
              .setApplicationEnableState(
                  appInfo.getPkgName(), true, true);
          // Reload.
          bindAppStatePrefDelayed();
          return true;
        });

    // Current is enabled.
    Objects.requireNonNull(toDisablePref).setVisible(!disabled);
    toDisablePref.setOnPreferenceClickListener(
        preference -> {
          thanos
              .getPkgManager()
              .setApplicationEnableState(
                  appInfo.getPkgName(), false, false);
          // Reload.
          bindAppStatePrefDelayed();
          return true;
        });

    Objects.requireNonNull(smartFreezePref)
        .setChecked(thanos.getPkgManager().isPkgSmartFreezeEnabled(appInfo.getPkgName()));
    smartFreezePref.setOnPreferenceChangeListener(
        new Preference.OnPreferenceChangeListener() {
          @Override
          public boolean onPreferenceChange(Preference preference, Object newValue) {
            boolean enable = (boolean) newValue;
            thanos.getPkgManager().setPkgSmartFreezeEnabled(appInfo.getPkgName(), enable);
            // Reload.
            // Wait 500ms for app state setup.
            new Handler(Looper.getMainLooper())
                .postDelayed(FeatureConfigFragment.this::bindAppStatePref, 500);
            return true;
          }
        });
  }

  class StartRestrictPref extends FeaturePref {

    StartRestrictPref(Context context) {
      super(context.getString(R.string.key_app_feature_config_start_restrict));
    }

    @Override
    boolean current() {
      return !ThanosManager.from(getContext())
          .getActivityManager()
          .isPkgStartBlocking(appInfo.getPkgName());
    }

    @Override
    void setTo(boolean value) {
      ThanosManager.from(getContext())
          .getActivityManager()
          .setPkgStartBlockEnabled(appInfo.getPkgName(), !value);
    }
  }

  class BgRestrictPref extends FeaturePref {

    BgRestrictPref(Context context) {
      super(context.getString(R.string.key_app_feature_config_bg_restrict));
    }

    @Override
    boolean current() {
      return !ThanosManager.from(getContext())
          .getActivityManager()
          .isPkgBgRestricted(appInfo.getPkgName());
    }

    @Override
    void setTo(boolean value) {
      ThanosManager.from(getContext())
          .getActivityManager()
          .setPkgBgRestrictEnabled(appInfo.getPkgName(), !value);
    }
  }

  class RecentTaskBlur extends FeaturePref {

    RecentTaskBlur(Context context) {
      super(context.getString(R.string.key_app_feature_config_recent_task_blur));
    }

    @Override
    boolean current() {
      return ThanosManager.from(getContext())
          .getActivityManager()
          .isPkgRecentTaskBlurEnabled(appInfo.getPkgName());
    }

    @Override
    void setTo(boolean value) {
      ThanosManager.from(getContext())
          .getActivityManager()
          .setPkgRecentTaskBlurEnabled(appInfo.getPkgName(), value);
    }
  }

  class TaskCleanUp extends FeaturePref {

    TaskCleanUp(Context context) {
      super(context.getString(R.string.key_app_feature_config_task_clean_up));
    }

    @Override
    boolean current() {
      return ThanosManager.from(getContext())
          .getActivityManager()
          .isPkgCleanUpOnTaskRemovalEnabled(appInfo.getPkgName());
    }

    @Override
    void setTo(boolean value) {
      ThanosManager.from(getContext())
          .getActivityManager()
          .setPkgCleanUpOnTaskRemovalEnabled(appInfo.getPkgName(), value);
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
      return ThanosManager.from(getContext())
          .getAppOpsManager()
          .isPkgOpRemindEnable(appInfo.getPkgName());
    }

    @Override
    void setTo(boolean value) {
      ThanosManager.from(getContext())
          .getAppOpsManager()
          .setPkgOpRemindEnable(appInfo.getPkgName(), value);
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
      return ThanosManager.from(getContext())
          .getNotificationManager()
          .isScreenOnNotificationEnabledForPkg(appInfo.getPkgName());
    }

    @Override
    void setTo(boolean value) {
      ThanosManager.from(getContext())
          .getNotificationManager()
          .setScreenOnNotificationEnabledForPkg(appInfo.getPkgName(), value);
    }

    @Override
    boolean visible() {
      return true;
    }
  }

  class SmartStandBy extends FeaturePref {

    SmartStandBy(Context context) {
      super(context.getString(R.string.key_app_feature_config_smart_standby));
    }

    @Override
    boolean current() {
      return ThanosManager.from(getContext())
          .getActivityManager()
          .isPkgSmartStandByEnabled(appInfo.getPkgName());
    }

    @Override
    void setTo(boolean value) {
      ThanosManager.from(getContext())
          .getActivityManager()
          .setPkgSmartStandByEnabled(appInfo.getPkgName(), value);
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
      return ThanosManager.from(getContext())
          .getActivityStackSupervisor()
          .isPackageLocked(appInfo.getPkgName());
    }

    @Override
    void setTo(boolean value) {
      if (ThanosApp.isPrc() && !DonateSettings.isActivated(getActivity())) {
        Toast.makeText(getActivity(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT)
            .show();
        return;
      }
      ThanosManager.from(getContext())
          .getActivityStackSupervisor()
          .setPackageLocked(appInfo.getPkgName(), value);
    }

    @Override
    boolean visible() {
      return true;
    }
  }

  abstract class FeaturePref {

    private String key;

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
      preference.setOnPreferenceChangeListener(
          (preference1, newValue) -> {
            boolean checked = (boolean) newValue;
            setTo(checked);
            return true;
          });
    }
  }
}
