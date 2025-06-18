package github.tornaco.thanos.android.module.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.elvishew.xlog.XLog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.concurrent.atomic.AtomicInteger;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.profile.ProfileManager;
import github.tornaco.android.thanos.core.profile.RuleInfo;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.util.IntentUtils;
import github.tornaco.android.thanos.widget.ModernAlertDialog;
import github.tornaco.android.thanos.widget.SwitchBar;
import github.tornaco.permission.requester.RequiresPermission;
import github.tornaco.permission.requester.RuntimePermissions;
import github.tornaco.thanos.android.module.profile.databinding.ModuleProfileRuleListActivityBinding;

@RuntimePermissions
public class RuleListActivity extends ThemeActivity implements RuleItemClickListener {

    private static final int REQUEST_CODE_PICK_FILE_TO_IMPORT = 6;

    private ModuleProfileRuleListActivityBinding binding;
    RuleListViewModel viewModel;

    public static void start(Context context) {
        ActivityUtils.startActivity(context, RuleListActivity.class);
    }



    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ModuleProfileRuleListActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setupView();
        setupViewModel();
    }

    private void setupView() {
        setSupportActionBar(binding.toolbar);
        showHomeAsUpNavigator();

        // List.
        binding.ruleListView.setLayoutManager(new LinearLayoutManager(this));
        binding.ruleListView.setAdapter(new RuleListAdapter(this, this::onRequestDeleteRule, (ruleInfo, checked) -> {
            ruleInfo.setEnabled(checked);
            if (checked) ThanosManager.from(getApplicationContext())
                    .getProfileManager()
                    .enableRule(ruleInfo.getId());
            else ThanosManager.from(getApplicationContext())
                    .getProfileManager()
                    .disableRule(ruleInfo.getId());
        }));
        binding.swipe.setOnRefreshListener(() -> viewModel.start());
        binding.swipe.setColorSchemeColors(getResources().getIntArray(
                github.tornaco.android.thanos.module.common.R.array.common_swipe_refresh_colors));

        // Switch.
        onSetupSwitchBar(binding.switchBarContainer.switchBar);

        // Has overlap issue.
        binding.fab.hide();
    }

    private void onRequestDeleteRule(RuleInfo ruleInfo) {
        ModernAlertDialog dialog = new ModernAlertDialog(thisActivity());
        dialog.setDialogTitle(getString(github.tornaco.android.thanos.res.R.string.common_menu_title_remove));
        dialog.setDialogMessage(getString(github.tornaco.android.thanos.res.R.string.common_menu_title_remove) + "\t" + ruleInfo.getName());
        dialog.setNegative(getString(android.R.string.cancel));
        dialog.setPositive(getString(github.tornaco.android.thanos.res.R.string.common_menu_title_remove));
        dialog.setOnPositive(() -> viewModel.deleteRule(ruleInfo));
        dialog.setCancelable(true);
        dialog.show();
    }

    private void onSetupSwitchBar(SwitchBar switchBar) {
        switchBar.setOnLabel(getString(github.tornaco.android.thanos.res.R.string.common_switchbar_title_format,
                getString(github.tornaco.android.thanos.res.R.string.module_profile_feature_name)));
        switchBar.setOffLabel(getString(github.tornaco.android.thanos.res.R.string.common_switchbar_title_format,
                getString(github.tornaco.android.thanos.res.R.string.module_profile_feature_name)));
        switchBar.setChecked(getSwitchBarCheckState());
        switchBar.addOnSwitchChangeListener(this::onSwitchBarCheckChanged);
    }

    private boolean getSwitchBarCheckState() {
        return ThanosManager.from(getApplicationContext()).isServiceInstalled()
                && ThanosManager.from(getApplicationContext()).getProfileManager()
                .isProfileEnabled();
    }

    private void onSwitchBarCheckChanged(MaterialSwitch switchBar, boolean isChecked) {
        ThanosManager.from(getApplicationContext())
                .ifServiceInstalled(thanosManager -> thanosManager.getProfileManager()
                        .setProfileEnabled(isChecked));
    }

    private void setupViewModel() {
        viewModel = obtainViewModel(this);
        viewModel.start();

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    @Override
    public void onItemClick(@NonNull RuleInfo ruleInfo) {
        RuleEditorActivity.start(thisActivity(), ruleInfo, ruleInfo.getFormat(), false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.module_profile_rule, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (new RuleListActivityMenuHandler().handleOptionsItemSelected(this, item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PICK_FILE_TO_IMPORT) {
            if (data == null) {
                XLog.e("No data.");
                return;
            }

            Uri uri = data.getData();
            if (uri == null) {
                XLog.e("No uri.");
                return;
            }

            viewModel.importRuleFromUri(uri);
        }
    }

    @RequiresPermission({
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO,
    })
    void importFromFileTOrAbove() {
        IntentUtils.startFilePickerActivityForRes(this, REQUEST_CODE_PICK_FILE_TO_IMPORT);
    }

    @RequiresPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void importFromFileTBelow() {
        IntentUtils.startFilePickerActivityForRes(this, REQUEST_CODE_PICK_FILE_TO_IMPORT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RuleListActivityPermissionRequester.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void onRequestAddNewRule() {
        AtomicInteger format = new AtomicInteger(ProfileManager.RULE_FORMAT_JSON);
        new MaterialAlertDialogBuilder(thisActivity())
                .setTitle(github.tornaco.android.thanos.res.R.string.module_profile_editor_select_format)
                .setSingleChoiceItems(new String[]{
                        "JSON",
                        "YAML"
                }, 0, (dialog, which) -> format.set(which == 0 ? ProfileManager.RULE_FORMAT_JSON
                        : ProfileManager.RULE_FORMAT_YAML))
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, (dialog, which)
                        -> ThanosManager.from(getApplicationContext())
                        .ifServiceInstalled(thanosManager -> RuleEditorActivity.start(thisActivity(), null, format.get(), false)))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    public static RuleListViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(RuleListViewModel.class);
    }

}
