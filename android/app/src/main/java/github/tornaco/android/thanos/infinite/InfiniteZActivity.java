package github.tornaco.android.thanos.infinite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.elvishew.xlog.XLog;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.AppItemActionListener;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.app.infinite.AddPackageCallback;
import github.tornaco.android.thanos.core.app.infinite.EnableCallback;
import github.tornaco.android.thanos.core.app.infinite.LaunchPackageCallback;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.databinding.ActivityIniniteZAppsBinding;
import github.tornaco.android.thanos.picker.AppPickerActivity;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.util.DialogUtils;
import github.tornaco.android.thanos.widget.ModernProgressDialog;
import github.tornaco.android.thanos.widget.SwitchBar;
import util.CollectionUtils;

public class InfiniteZActivity extends ThemeActivity {
    private static final int REQ_PICK_APPS = 0x100;

    private InfiniteZAppsViewModel viewModel;
    private ActivityIniniteZAppsBinding binding;

    @Verify
    public static void start(Context context) {
        ActivityUtils.startActivity(context, InfiniteZActivity.class);
    }

    @Override
    @Verify
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIniniteZAppsBinding.inflate(
                LayoutInflater.from(this), null, false);
        setContentView(binding.getRoot());
        setupView();
        setupViewModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupView() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // List.
        binding.apps.setLayoutManager(new GridLayoutManager(this, 5));
        binding.apps.setAdapter(new InfiniteZAppsAdapter(new AppItemActionListener() {
            @Override
            public void onAppItemClick(AppInfo appInfo) {
                ThanosManager.from(getApplicationContext())
                        .getInfiniteZ()
                        .launchPackage(appInfo.getPkgName(), new LaunchPackageCallback() {
                            @Override
                            public void onSuccessMain() {

                            }

                            @Override
                            public void onErrorMain(String errorMessage, int errorCode) {

                            }
                        });
            }

            @Override
            public void onAppItemSwitchStateChange(AppInfo appInfo, boolean checked) {

            }
        }, this::showItemPopMenu));

        binding.swipe.setOnRefreshListener(this::refreshState);
        binding.swipe.setColorSchemeColors(getResources()
                .getIntArray(github.tornaco.android.thanos.module.common.R.array.common_swipe_refresh_colors));

        onSetupSwitchBar(binding.switchBarContainer.switchBar);
    }

    private void refreshState() {
        viewModel.start();
    }

    @Verify
    private void showItemPopMenu(@NonNull View anchor, @NonNull AppListModel model) {

    }

    protected void onSetupSwitchBar(SwitchBar switchBar) {
        switchBar.setChecked(ThanosManager.from(thisActivity()).getInfiniteZ().isEnabled());
        switchBar.addOnSwitchChangeListener((switchView, isChecked) -> {
            ModernProgressDialog p = new ModernProgressDialog(thisActivity());
            p.setMessage(getString(R.string.common_text_wait_a_moment));
            p.show();
            ThanosManager.from(getApplicationContext()).getInfiniteZ()
                    .setEnabled(isChecked, new EnableCallback() {
                        @Override
                        public void onSuccessMain(int userId) {
                            p.dismiss();
                            refreshState();
                        }

                        @Override
                        public void onErrorMain(String errorMessage, int errorCode) {
                            XLog.e("Disable infiniteZ fail: %s %s", errorCode, errorMessage);
                            DialogUtils.showMessage(thisActivity(), null, getString(R.string.common_generic_error));
                            p.dismiss();
                            refreshState();
                        }
                    });
        });
    }

    @Verify
    private void setupViewModel() {
        viewModel = obtainViewModel(this);
        viewModel.start();

        binding.setViewmodel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_PICK_APPS == requestCode && resultCode == RESULT_OK && data != null && data.hasExtra("apps")) {
            List<AppInfo> appInfos = data.getParcelableArrayListExtra("apps");
            ModernProgressDialog p = new ModernProgressDialog(thisActivity());
            p.setMessage(getString(R.string.common_text_wait_a_moment));
            p.show();
            CollectionUtils.consumeRemaining(appInfos, appInfo -> ThanosManager.from(getApplicationContext())
                    .getInfiniteZ()
                    .addPackage(appInfo.getPkgName(), new AddPackageCallback() {
                        @Override
                        public void onSuccessMain(int userId) {
                            postOnUiDelayed(p::dismiss, 2400);
                            viewModel.start();
                        }

                        @Override
                        public void onErrorMain(String errorMessage, int errorCode) {
                            DialogUtils.showMessage(thisActivity(), String.valueOf(errorCode), errorMessage);
                            p.dismiss();
                        }
                    }));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.infinite_z, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    @Verify
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_add == item.getItemId()) {
            onRequestAddApp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onRequestAddApp() {
        ThanosManager.from(getApplicationContext()).ifServiceInstalled(thanosManager -> {
            ArrayList<Pkg> exclude = thanosManager.getInfiniteZ().getInstalledPackages().stream().map(Pkg::fromAppInfo).collect(Collectors.toCollection(Lists::newArrayList));
            AppPickerActivity.start(thisActivity(), REQ_PICK_APPS, exclude);
        });
    }

    public static InfiniteZAppsViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(InfiniteZAppsViewModel.class);
    }
}
