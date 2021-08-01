package github.tornaco.android.thanos.power;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.databinding.ActivityStandbyRulesBinding;
import github.tornaco.android.thanos.start.StartRule;
import github.tornaco.android.thanos.start.StartRuleItemClickListener;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.util.ToastUtils;
import github.tornaco.android.thanos.widget.SwitchBar;

public class StandByRuleActivity extends ThemeActivity implements StartRuleItemClickListener, StandbyRuleItemClickListener {

    private StandByRuleViewModel viewModel;
    private ActivityStandbyRulesBinding binding;

    public static void start(Context context) {
        ActivityUtils.startActivity(context, StandByRuleActivity.class);
    }

    @Override
    @Verify
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStandbyRulesBinding.inflate(LayoutInflater.from(this));
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
        binding.ruleListView.setAdapter(new StandbyRuleListAdapter(this));
        binding.swipe.setOnRefreshListener(() -> viewModel.start());
        binding.swipe.setColorSchemeColors(getResources().getIntArray(
                github.tornaco.android.thanos.module.common.R.array.common_swipe_refresh_colors));

        binding.fab.setOnClickListener((View v) -> showEditOrAddDialog(null));

        // Switch.
        onSetupSwitchBar(binding.switchBar);
    }

    @Verify
    private void setupViewModel() {
        viewModel = obtainViewModel(this);
        viewModel.start();

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    protected void onSetupSwitchBar(SwitchBar switchBar) {
        switchBar.setChecked(getSwitchBarCheckState());
        switchBar.addOnSwitchChangeListener(this::onSwitchBarCheckChanged);
    }

    protected boolean getSwitchBarCheckState() {
        return ThanosManager.from(getApplicationContext()).isServiceInstalled()
                && ThanosManager.from(getApplicationContext()).getActivityManager().isStandbyRuleEnabled();
    }

    protected void onSwitchBarCheckChanged(Switch switchBar, boolean isChecked) {
        ThanosManager.from(getApplicationContext()).getActivityManager().setStandbyRuleEnabled(isChecked);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.resume();
    }

    public static StandByRuleViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(StandByRuleViewModel.class);
    }

    @Override
    public void onItemClick(@NonNull StartRule rule) {
        showEditOrAddDialog(rule.getRaw());
    }

    private void showEditOrAddDialog(String ruleIfEdit) {
        ThanosManager thanosManager = ThanosManager.from(getApplicationContext());
        if (!thanosManager.isServiceInstalled()) return;

        AppCompatEditText editText = new AppCompatEditText(thisActivity());
        editText.setText(ruleIfEdit);
        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity())
                .setTitle(R.string.menu_title_rules)
                .setView(editText)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.getText() == null) {
                            return;
                        }
                        String newRule = editText.getText().toString();
                        if (TextUtils.isEmpty(newRule)) {
                            return;
                        }
                        if (ruleIfEdit != null) {
                            thanosManager.getActivityManager().deleteStandbyRule(ruleIfEdit);
                        }
                        thanosManager.getActivityManager().addStandbyRule(newRule);
                        viewModel.start();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);
        if (!TextUtils.isEmpty(ruleIfEdit)) {
            builder.setNeutralButton(R.string.common_menu_title_remove, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    thanosManager.getActivityManager().deleteStandbyRule(ruleIfEdit);
                    viewModel.start();
                }
            });
        }

        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start_rules_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_info) {
            showInfoDialog();
            return true;
        }
        if (item.getItemId() == R.id.action_import) {
            if (importRulesFromClipboard()) {
                ToastUtils.ok(thisActivity());
            } else {
                ToastUtils.nook(thisActivity());
            }
            viewModel.start();
            return true;
        }
        if (item.getItemId() == R.id.action_export) {
            exportAllRulesToClipboard();
            ToastUtils.ok(thisActivity());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean importRulesFromClipboard() {
        return viewModel.importRulesFromClipboard();
    }

    private void exportAllRulesToClipboard() {
        viewModel.exportAllRulesToClipboard();
    }

    private void showInfoDialog() {
        new AlertDialog.Builder(thisActivity())
                .setTitle(R.string.menu_title_rules)
                .setMessage(R.string.feature_summary_standby_restrict_rules)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public void onItemClick(@NonNull StandbyRule rule) {
        showEditOrAddDialog(rule.getRaw());
    }
}
