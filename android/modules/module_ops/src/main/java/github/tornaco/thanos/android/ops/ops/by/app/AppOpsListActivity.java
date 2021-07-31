package github.tornaco.thanos.android.ops.ops.by.app;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.elvishew.xlog.XLog;

import java.util.Objects;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.secure.ops.AppOpsManager;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.section.StickyHeaderLayoutManager;
import github.tornaco.thanos.android.ops.R;
import github.tornaco.thanos.android.ops.databinding.ModuleOpsLayoutOpsListBinding;

public class AppOpsListActivity extends ThemeActivity {

    private ModuleOpsLayoutOpsListBinding binding;
    private AppOpsListViewModel viewModel;

    private AppInfo appInfo;

    public static void start(@NonNull Context context, @NonNull AppInfo appInfo) {
        Bundle data = new Bundle();
        data.putParcelable("app", appInfo);
        ActivityUtils.startActivity(context, AppOpsListActivity.class, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ModuleOpsLayoutOpsListBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        resolveIntent();
        setupView();
        setupViewModel();
    }

    @Verify
    private void resolveIntent() {
        this.appInfo = getIntent().getParcelableExtra("app");
        if (appInfo == null) {
            finish();
            return;
        }
        ThanosManager thanos = ThanosManager.from(this);
        if (thanos.isServiceInstalled()) {
            setTitle(appInfo.getAppLabel());
            binding.toolbar.setTitle(appInfo.getAppLabel());
        }
    }

    private void setupView() {
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Creating the ArrayAdapter instance having the category list
        // 0:All 1:allow 2:ignore
        String[] category = getResources().getStringArray(R.array.module_ops_filter_by_op_mode);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                github.tornaco.android.thanos.module.common.R.layout.ghost_text_view, category);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        binding.spinner.setAdapter(arrayAdapter);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                XLog.w("onItemSelected: %s", index);
                binding.toolbar.setTitle(appInfo.getAppLabel() + " - " + category[index]);
                viewModel.setModeFilter(index);
                viewModel.start(appInfo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        StickyHeaderLayoutManager stickyHeaderLayoutManager = new StickyHeaderLayoutManager();
        // set a header position callback to set elevation on sticky headers, because why not
        stickyHeaderLayoutManager.setHeaderPositionChangedCallback((sectionIndex, header, oldPosition, newPosition) -> {
            boolean elevated = newPosition == StickyHeaderLayoutManager.HeaderPosition.STICKY;
            header.setElevation(elevated ? 8 : 0);
        });
        binding.apps.setLayoutManager(stickyHeaderLayoutManager);
        binding.apps.setAdapter(new AppOpsListAdapter(thisActivity(), appInfo));

        binding.swipe.setOnRefreshListener(() -> viewModel.start(appInfo));
        binding.swipe.setColorSchemeColors(getResources().getIntArray(github.tornaco.android.thanos.module.common.R.array.common_swipe_refresh_colors));

    }

    private int getTitleRes() {
        return R.string.module_ops_activity_title_app_ops_list;
    }

    private void setupViewModel() {
        viewModel = obtainViewModel(this);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.module_ops_app_op_list, menu);
        return true;
    }

    @Override
    @Verify
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_select_all_allow == item.getItemId()) {
            new AlertDialog.Builder(thisActivity())
                    .setMessage(github.tornaco.android.thanos.module.common.R.string.common_dialog_message_are_you_sure)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewModel.selectAll(appInfo, AppOpsManager.MODE_ALLOWED);
                        }
                    }).show();
            return true;
        }
        if (R.id.action_select_all_foreground == item.getItemId()) {
            new AlertDialog.Builder(thisActivity())
                    .setMessage(github.tornaco.android.thanos.module.common.R.string.common_dialog_message_are_you_sure)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewModel.selectAll(appInfo, AppOpsManager.MODE_FOREGROUND);
                        }
                    }).show();
            return true;
        }
        if (R.id.action_un_select_all_ignore == item.getItemId()) {
            new AlertDialog.Builder(thisActivity())
                    .setMessage(github.tornaco.android.thanos.module.common.R.string.common_dialog_message_are_you_sure)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewModel.selectAll(appInfo, AppOpsManager.MODE_IGNORED);
                        }
                    }).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.start(appInfo);
    }

    public static AppOpsListViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(AppOpsListViewModel.class);
    }
}
