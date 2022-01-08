package github.tornaco.android.thanox.module.notification.recorder;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.Objects;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.SwitchBar;
import github.tornaco.android.thanos.widget.section.StickyHeaderLayoutManager;
import github.tornaco.android.thanox.module.notification.recorder.databinding.ModuleNotificationRecorderNrdListLayoutBinding;

public class NotificationRecordListActivity extends ThemeActivity {

    private ModuleNotificationRecorderNrdListLayoutBinding binding;
    private NRDListViewModel viewModel;

    public static void start(Context context) {
        ActivityUtils.startActivity(context, NotificationRecordListActivity.class);
    }

    public static NRDListViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(NRDListViewModel.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ModuleNotificationRecorderNrdListLayoutBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setupView();
        setupViewModel();
    }


    private void setupView() {
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.module_notification_recorder_feature_title_notification_recorder);

        StickyHeaderLayoutManager stickyHeaderLayoutManager = new StickyHeaderLayoutManager();
        // set a header position callback to set elevation on sticky headers, because why not
        stickyHeaderLayoutManager.setHeaderPositionChangedCallback((sectionIndex, header, oldPosition, newPosition) -> {
            boolean elevated = newPosition == StickyHeaderLayoutManager.HeaderPosition.STICKY;
            header.setElevation(elevated ? 8 : 0);
        });
        binding.apps.setLayoutManager(stickyHeaderLayoutManager);
        binding.apps.setAdapter(new NRListAdapter());

        binding.swipe.setOnRefreshListener(() -> viewModel.start());
        binding.swipe.setColorSchemeColors(getResources().getIntArray(github.tornaco.android.thanos.module.common.R.array.common_swipe_refresh_colors));

        onSetupSwitchBar(binding.switchBarContainer.switchBar);

        // Search.
        binding.searchView.setOnQueryTextListener(
                new MaterialSearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        viewModel.setSearchText(query);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        viewModel.setSearchText(newText);
                        return true;
                    }
                });

        binding.searchView.setOnSearchViewListener(
                new MaterialSearchView.SearchViewListener() {
                    @Override
                    public void onSearchViewShown() {
                        binding.toolbarLayout.setTitleEnabled(false);
                        binding.appbar.setExpanded(false, true);
                    }

                    @Override
                    public void onSearchViewClosed() {
                        viewModel.clearSearchText();
                        binding.toolbarLayout.setTitleEnabled(true);
                    }
                });
    }

    protected void onSetupSwitchBar(SwitchBar switchBar) {
        switchBar.setChecked(getSwitchBarCheckState());
        switchBar.addOnSwitchChangeListener(this::onSwitchBarCheckChanged);
    }

    protected boolean getSwitchBarCheckState() {
        return ThanosManager.from(getApplicationContext()).isServiceInstalled()
                && ThanosManager.from(getApplicationContext())
                .getNotificationManager()
                .isPersistOnNewNotificationEnabled();
    }

    protected void onSwitchBarCheckChanged(SwitchMaterial switchBar, boolean isChecked) {
        ThanosManager.from(getApplicationContext())
                .getNotificationManager()
                .setPersistOnNewNotificationEnabled(isChecked);
    }

    private void setupViewModel() {
        viewModel = obtainViewModel(this);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.module_notification_recorder_nr_list, menu);
        MenuItem item = menu.findItem(github.tornaco.android.thanos.module.common.R.id.action_search);
        binding.searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_clear_all) {
            new MaterialAlertDialogBuilder(thisActivity())
                    .setTitle(R.string.module_notification_recorder_clear_all)
                    .setMessage(github.tornaco.android.thanos.module.common.R.string.common_dialog_message_are_you_sure)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewModel.clearAllRecords();
                            finish();
                        }
                    }).show();
        }
        if (item.getItemId() == R.id.action_settings) {
            NotificationRecordSettingsActivity.start(thisActivity());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (binding.searchView.isSearchOpen()) {
            binding.searchView.closeSearch();
            return;
        }
        super.onBackPressed();
    }
}
