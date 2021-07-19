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
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.elvishew.xlog.XLog;

import java.util.concurrent.atomic.AtomicInteger;

import github.tornaco.android.rhino.annotations.Verify;
import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.profile.ProfileManager;
import github.tornaco.android.thanos.core.profile.RuleInfo;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.util.IntentUtils;
import github.tornaco.android.thanos.widget.SwitchBar;
import github.tornaco.permission.requester.RequiresPermission;
import github.tornaco.permission.requester.RuntimePermissions;
import github.tornaco.thanos.android.module.profile.databinding.ModuleProfileRuleListActivityBinding;

@RuntimePermissions
public class RuleListActivity extends ThemeActivity implements RuleItemClickListener {

    private static final int REQUEST_CODE_PICK_FILE_TO_IMPORT = 6;

    private ModuleProfileRuleListActivityBinding binding;
    private RuleListViewModel viewModel;

    public static void start(Context context) {
        ActivityUtils.startActivity(context, RuleListActivity.class);
    }

    @Override
    @Verify
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ModuleProfileRuleListActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setupView();
        setupViewModel();
    }

    private void setupView() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // List.
        binding.ruleListView.setLayoutManager(new LinearLayoutManager(this));
        binding.ruleListView.setAdapter(new RuleListAdapter(this, (ruleInfo, checked) -> {
            ruleInfo.setEnabled(checked);
            if (checked) ThanosManager.from(getApplicationContext())
                    .getProfileManager()
                    .enableRule(ruleInfo.getName());
            else ThanosManager.from(getApplicationContext())
                    .getProfileManager()
                    .disableRule(ruleInfo.getName());
        }));
        binding.swipe.setOnRefreshListener(() -> viewModel.start());
        binding.swipe.setColorSchemeColors(getResources().getIntArray(
                github.tornaco.android.thanos.module.common.R.array.common_swipe_refresh_colors));

        // Switch.
        onSetupSwitchBar(binding.switchBar);

        // Has overlap issue.
        binding.fab.hide();
    }

    private void onSetupSwitchBar(SwitchBar switchBar) {
        switchBar.setChecked(getSwitchBarCheckState());
        switchBar.addOnSwitchChangeListener(this::onSwitchBarCheckChanged);
    }

    private boolean getSwitchBarCheckState() {
        return ThanosManager.from(getApplicationContext()).isServiceInstalled()
                && ThanosManager.from(getApplicationContext()).getProfileManager()
                .isProfileEnabled();
    }

    private void onSwitchBarCheckChanged(Switch switchBar, boolean isChecked) {
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
        RuleEditorActivity.start(thisActivity(), ruleInfo, ruleInfo.getFormat());
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.resume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.module_profile_rule, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_view_wiki == item.getItemId()) {
            final Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(BuildProp.THANOX_URL_DOCS_PROFILE));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(intent, ""));
            }
            return true;
        }

        if (R.id.action_import_from_file == item.getItemId()) {
            RuleListActivityPermissionRequester.importFromFileChecked(this);
            return true;
        }

        if (R.id.action_import_examples == item.getItemId()) {
            viewModel.importRuleExamples();
            return true;
        }

        if (R.id.action_global_var == item.getItemId()) {
            GlobalVarListActivity.start(this);
            return true;
        }

        if (R.id.action_add == item.getItemId()) {
            onRequestAddNewRule();
            return true;
        }

        if (R.id.action_rule_engine == item.getItemId()) {
            RuleEngineSettingsActivity.start(thisActivity());
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

    @RequiresPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void importFromFile() {
        IntentUtils.startFilePickerActivityForRes(this, REQUEST_CODE_PICK_FILE_TO_IMPORT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RuleListActivityPermissionRequester.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void onRequestAddNewRule() {
        AtomicInteger format = new AtomicInteger(ProfileManager.RULE_FORMAT_JSON);
        new AlertDialog.Builder(thisActivity())
                .setTitle(R.string.module_profile_editor_select_format)
                .setSingleChoiceItems(new String[]{
                        "JSON",
                        "YAML"
                }, 0, (dialog, which) -> format.set(which == 0 ? ProfileManager.RULE_FORMAT_JSON
                        : ProfileManager.RULE_FORMAT_YAML))
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, (dialog, which)
                        -> ThanosManager.from(getApplicationContext())
                        .ifServiceInstalled(
                                thanosManager ->
                                        RuleEditorActivity.start(thisActivity(),
                                                null,
                                                format.get())))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    public static RuleListViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(RuleListViewModel.class);
    }

}
