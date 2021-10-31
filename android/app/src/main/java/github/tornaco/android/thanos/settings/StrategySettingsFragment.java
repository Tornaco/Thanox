package github.tornaco.android.thanos.settings;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.preference.DropDownPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.SwitchPreferenceCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import github.tornaco.android.thanos.BaseWithFabPreferenceFragmentCompat;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.apps.AppDetailsActivity;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.profile.ConfigTemplate;
import github.tornaco.android.thanos.core.profile.ProfileManager;
import github.tornaco.android.thanos.core.util.function.Function;
import github.tornaco.android.thanos.widget.EditTextDialog;
import github.tornaco.android.thanos.widget.QuickDropdown;
import github.tornaco.android.thanos.widget.pref.ViewAwarePreference;
import util.CollectionUtils;
import util.Consumer;

public class StrategySettingsFragment extends BaseWithFabPreferenceFragmentCompat {

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.strategy_settings_pref, rootKey);
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  protected void onBindPreferences() {
    super.onBindPreferences();

    ThanosManager thanos = ThanosManager.from(getContext());
    if (!thanos.isServiceInstalled()) {
      return;
    }

    showFab();
    getFab().setOnClickListener(v -> requestAddTemplate());

    // Auto config.
    SwitchPreferenceCompat autoConfigPref =
        findPreference(getString(R.string.key_new_installed_apps_config_enabled));
    autoConfigPref.setChecked(
        thanos.isServiceInstalled()
            && thanos.getProfileManager().isAutoApplyForNewInstalledAppsEnabled());
    autoConfigPref.setOnPreferenceChangeListener(
        (preference, newValue) -> {
          if (thanos.isServiceInstalled()) {
            boolean checked = (boolean) newValue;
            thanos.getProfileManager().setAutoApplyForNewInstalledAppsEnabled(checked);
          }
          return true;
        });

    updateAutoConfigSelection();
    updateConfigTemplatePrefs();
  }

  private void updateAutoConfigSelection() {
    ThanosManager thanos = ThanosManager.from(getContext());
    ProfileManager profileManager = thanos.getProfileManager();
    List<String> entries = new ArrayList<>();
    List<String> values = new ArrayList<>();
    String selectedId = profileManager.getAutoConfigTemplateSelectionId();
    ConfigTemplate selectedTemplate = profileManager.getConfigTemplateById(selectedId);
    String valueNotSet = getString(R.string.common_text_value_not_set);

    CollectionUtils.consumeRemaining(
        profileManager.getAllConfigTemplates(),
            template -> {
              entries.add(template.getTitle());
              values.add(template.getId());
            });
    DropDownPreference newInstalledAppsConfig =
        findPreference(getString(R.string.key_new_installed_apps_config));
    Objects.requireNonNull(newInstalledAppsConfig).setEntries(entries.toArray(new String[0]));
    newInstalledAppsConfig.setEntryValues(values.toArray(new String[0]));
    newInstalledAppsConfig.setValue(selectedId);
    newInstalledAppsConfig.setSummary(
        selectedTemplate == null ? valueNotSet : selectedTemplate.getTitle());
    newInstalledAppsConfig.setOnPreferenceChangeListener(
            (preference, newValue) -> {
              String newId = (String) newValue;
              profileManager.setAutoConfigTemplateSelection(newId);

              updateAutoConfigSelection();
              return true;
            });
  }

  private void updateConfigTemplatePrefs() {
    PreferenceCategory templatesCategory =
        findPreference(getString(R.string.key_config_template_category));
    Objects.requireNonNull(templatesCategory).removeAll();
    ThanosManager thanos = ThanosManager.from(getContext());
    ProfileManager profileManager = thanos.getProfileManager();
    for (ConfigTemplate template : profileManager.getAllConfigTemplates()) {
      ViewAwarePreference tp = new ViewAwarePreference(Objects.requireNonNull(getContext()));
      tp.setTitle(template.getTitle());
      tp.setKey(template.getId());
      tp.setDefaultValue(template);
      tp.setOnPreferenceClickListener(
              preference -> {
                ViewAwarePreference vp = (ViewAwarePreference) preference;
                showConfigTemplateOptionsDialog(template, vp.getView());
                return true;
              });
      templatesCategory.addPreference(tp);
    }
  }

  private void requestAddTemplate() {
    EditTextDialog.show(
        getActivity(),
        getString(R.string.pref_action_create_new_config_template),
            content -> {
              if (TextUtils.isEmpty(content)) {
                return;
              }
              String uuid = UUID.randomUUID().toString();
              ConfigTemplate template =
                  ConfigTemplate.builder()
                      .title(content)
                      .id(uuid)
                      .dummyPackageName(
                          ProfileManager
                                  .PROFILE_AUTO_APPLY_NEW_INSTALLED_APPS_CONFIG_TEMPLATE_PACKAGE_PREFIX
                              + uuid)
                      .createAt(System.currentTimeMillis())
                      .build();
              boolean added =
                  ThanosManager.from(getContext()).getProfileManager().addConfigTemplate(template);
              if (added) {
                updateConfigTemplatePrefs();
                updateAutoConfigSelection();
              }
            });
  }

  private void showConfigTemplateOptionsDialog(ConfigTemplate template, View anchor) {
    QuickDropdown.show(
        requireActivity(),
        anchor,
            input -> {
              switch (input) {
                case 0:
                  return getString(R.string.pref_action_edit_or_view_config_template);
                case 1:
                  return getString(R.string.pref_action_delete_config_template);
              }
              return null;
            },
            id -> {
              switch (id) {
                case 0:
                  AppInfo appInfo = new AppInfo();
                  appInfo.setSelected(false);
                  appInfo.setPkgName(template.getDummyPackageName());
                  appInfo.setAppLabel(template.getTitle());
                  appInfo.setDummy(true);
                  appInfo.setVersionCode(-1);
                  appInfo.setVersionCode(-1);
                  appInfo.setUid(-1);
                  AppDetailsActivity.start(getActivity(), appInfo);
                  break;
                case 1:
                  if (ThanosManager.from(getContext())
                      .getProfileManager()
                      .deleteConfigTemplate(template)) {
                    updateConfigTemplatePrefs();
                    updateAutoConfigSelection();
                  }
                  break;
              }
            });
  }
}
