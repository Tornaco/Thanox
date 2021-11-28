package github.tornaco.android.thanos.infinite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.AppItemActionListener;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.app.infinite.AddPackageCallback;
import github.tornaco.android.thanos.core.app.infinite.EnableCallback;
import github.tornaco.android.thanos.core.app.infinite.LaunchPackageCallback;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.databinding.ActivityIniniteZAppsBinding;
import github.tornaco.android.thanos.picker.AppPickerActivity;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.ModernProgressDialog;
import github.tornaco.android.thanos.widget.SwitchBar;
import util.CollectionUtils;

public class InfiniteZActivity extends ThemeActivity {
    private static final int REQ_PICK_APPS = 0x100;

    private InfiniteZAppsViewModel viewModel;
    private ActivityIniniteZAppsBinding binding;

    private Handler uiHandler;
    private Runnable hideFabRunnable = new Runnable() {
        @Override
        public void run() {
            binding.fab.hide();
        }
    };
    private Runnable showFabRunnable = new Runnable() {
        @Override
        public void run() {
            binding.fab.show();
        }
    };

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
        uiHandler = new Handler(Looper.getMainLooper());
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
        binding.apps.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    delayHideFab();
                } else {
                    nowShowFab();
                }
            }
        });


        binding.swipe.setOnRefreshListener(() -> viewModel.start());
        binding.swipe.setColorSchemeColors(getResources()
                .getIntArray(github.tornaco.android.thanos.module.common.R.array.common_swipe_refresh_colors));

        onSetupSwitchBar(binding.switchBarContainer.switchBar);

        binding.fab.setOnClickListener(v -> ThanosManager.from(getApplicationContext()).ifServiceInstalled(thanosManager -> {
            ArrayList<String> exclude = Lists.newArrayList(thanosManager.getPkgManager().getSmartFreezePkgs());
            AppPickerActivity.start(thisActivity(), REQ_PICK_APPS, exclude);
        }));

        delayHideFab();
    }

    private void nowHideFab() {
        uiHandler.removeCallbacks(hideFabRunnable);
        uiHandler.post(hideFabRunnable);
    }

    private void delayHideFab() {
        uiHandler.removeCallbacks(hideFabRunnable);
        uiHandler.postDelayed(hideFabRunnable, 1800);
    }

    private void nowShowFab() {
        uiHandler.removeCallbacks(showFabRunnable);
        uiHandler.post(showFabRunnable);
    }

    @Verify
    private void showItemPopMenu(@NonNull View anchor, @NonNull AppInfo appInfo) {

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
                        }

                        @Override
                        public void onErrorMain(String errorMessage, int errorCode) {
                            p.dismiss();
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
            CollectionUtils.consumeRemaining(appInfos, appInfo -> ThanosManager.from(getApplicationContext())
                    .getInfiniteZ()
                    .addPackage(appInfo.getPkgName(), new AddPackageCallback() {
                        @Override
                        public void onSuccessMain(int userId) {

                        }

                        @Override
                        public void onErrorMain(String errorMessage, int errorCode) {

                        }
                    }));
            viewModel.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    @Verify
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public static InfiniteZAppsViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(InfiniteZAppsViewModel.class);
    }
}
