package github.tornaco.android.thanos.apps;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.ThanosApp;
import github.tornaco.android.thanos.app.BaseTrustedActivity;
import github.tornaco.android.thanos.app.donate.DonateSettings;
import github.tornaco.android.thanos.common.CategoryIndex;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.profile.ConfigTemplate;
import github.tornaco.android.thanos.core.profile.ProfileManager;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.databinding.ActivityAppDetailsBinding;
import github.tornaco.android.thanos.settings.StrategySettingsActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.util.ToastUtils;
import util.ObjectsUtils;

public class AppDetailsActivity extends BaseTrustedActivity {
    private ActivityAppDetailsBinding binding;
    private AppInfo appInfo;
    private FeatureConfigFragment featureConfigFragment;

    @Verify
    public static void start(Context context, AppInfo appInfo) {
        Bundle data = new Bundle();
        data.putParcelable("app", appInfo);
        ActivityUtils.startActivity(context, AppDetailsActivity.class, data);
    }

    @Override
    @Verify
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!resolveIntent()) {
            finish();
            return;
        }
        binding = ActivityAppDetailsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        initView();
        initViewModel();

        if (savedInstanceState == null) {
            reAddFragment();
        }
    }

    private void reAddFragment() {
        if (this.featureConfigFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(featureConfigFragment)
                    .commitAllowingStateLoss();
        }
        this.featureConfigFragment = null;

        this.featureConfigFragment = new FeatureConfigFragment();
        Bundle data = new Bundle();
        data.putParcelable("app", appInfo);
        featureConfigFragment.setArguments(data);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, featureConfigFragment)
                .commit();
    }

    private boolean resolveIntent() {
        if (getIntent() == null) {
            return false;
        }
        appInfo = getIntent().getParcelableExtra("app");
        return appInfo != null && (appInfo.isDummy() || PkgUtils.isPkgInstalled(thisActivity(), appInfo.getPkgName()));
    }

    private void initView() {
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setTitle(appInfo.getAppLabel());
        binding.setApp(appInfo);

        CategoryIndex categoryIndex = CategoryIndex.fromFlags(appInfo.getFlags());
        binding.setCate(categoryIndex);
    }

    @Verify
    private void initViewModel() {
        AppDetailsViewModel viewModel = obtainViewModel(this);
        binding.setViewmodel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_apply_template == item.getItemId()) {
            requestApplyTemplateSelection();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Verify
    private void requestApplyTemplateSelection() {
        if (ThanosApp.isPrc() && !DonateSettings.isActivated(thisActivity())) {
            Toast.makeText(thisActivity(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT).show();
            return;
        }

        ThanosManager thanos = ThanosManager.from(thisActivity());
        ProfileManager profileManager = thanos.getProfileManager();
        List<String> entries = new ArrayList<>();
        List<String> values = new ArrayList<>();
        String selectedId = profileManager.getAutoConfigTemplateSelectionId();
        int selectionIndex = 0;

        List<ConfigTemplate> allConfigTemplates = profileManager.getAllConfigTemplates();

        if (allConfigTemplates.size() == 0) {
            Toast.makeText(thisActivity(), R.string.pref_action_create_new_config_template, Toast.LENGTH_LONG).show();
            StrategySettingsActivity.start(thisActivity());
            return;
        }

        for (int i = 0; i < allConfigTemplates.size(); i++) {
            ConfigTemplate template = allConfigTemplates.get(i);
            entries.add(template.getTitle());
            values.add(template.getId());
            if (selectedId != null
                    && ObjectsUtils.equals(template.getId(), selectedId)) {
                selectionIndex = i;
            }
        }

        AlertDialog alertDialog = new AlertDialog.Builder(thisActivity())
                .setTitle(R.string.pref_action_apply_config_template)
                .setSingleChoiceItems(entries.toArray(new String[0]),
                        selectionIndex,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                ConfigTemplate selectedTemplate = profileManager.getConfigTemplateById(values.get(which));

                                if (selectedTemplate == null) {
                                    ToastUtils.nook(getApplicationContext());
                                    return;
                                }

                                Toast.makeText(thisActivity(), selectedTemplate.getTitle(), Toast.LENGTH_SHORT).show();

                                if (profileManager.applyConfigTemplateForPackage(appInfo.getPkgName(), selectedTemplate)) {
                                    ToastUtils.ok(getApplicationContext());
                                    reAddFragment();
                                } else {
                                    ToastUtils.nook(getApplicationContext());
                                }
                            }
                        })
                .create();
        alertDialog.show();
    }

    public static AppDetailsViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(AppDetailsViewModel.class);
    }
}
