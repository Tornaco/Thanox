package github.tornaco.android.thanos.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.Comparator;
import java.util.List;

import github.tornaco.android.thanos.core.pm.PackageSet;
import github.tornaco.android.thanos.module.common.R;
import github.tornaco.android.thanos.module.common.databinding.ActivityCommonListFilterBinding;
import github.tornaco.android.thanos.module.common.databinding.CommonFeatureDescriptionBarLayoutBinding;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.widget.SwitchBar;

public abstract class CommonAppListFilterActivity extends ThemeActivity {

    protected CommonAppListFilterViewModel viewModel;
    private ActivityCommonListFilterBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommonListFilterBinding.inflate(LayoutInflater.from(this), null, false);
        viewModel = obtainViewModel(this);
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

        setTitle(getTitleString());

        onSetupFilter(binding.filterChipContainer.filterChip);
        onSetupChip(
                binding.chipContainer,
                binding.chipGroup,
                binding.chip1,
                binding.chip2,
                binding.chip3,
                binding.chip4);

        // List.
        binding.apps.setLayoutManager(onCreateLayoutManager());
        binding.apps.setAdapter(onCreateCommonAppListFilterAdapter());
        binding.swipe.setOnRefreshListener(() -> viewModel.start());
        binding.swipe.setColorSchemeColors(
                getResources().getIntArray(R.array.common_swipe_refresh_colors));

        // Switch.
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

        onSetupFab(binding.fab);

        onSetupDescription(binding.featureDescContainer);
    }

    protected LayoutManager onCreateLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @StringRes
    protected int getTitleRes() {
        return 0;
    }

    protected String getTitleString() {
        return getString(getTitleRes());
    }

    protected AppItemClickListener onCreateAppItemViewClickListener() {
        return null;
    }

    private void setupViewModel() {
        viewModel.setListModelLoader(onCreateListModelLoader());
        viewModel.start();

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    @NonNull
    protected abstract CommonAppListFilterViewModel.ListModelLoader onCreateListModelLoader();

    protected void onSetupSwitchBar(SwitchBar switchBar) {
        switchBar.setChecked(getSwitchBarCheckState());
        switchBar.addOnSwitchChangeListener(this::onSwitchBarCheckChanged);
    }

    protected boolean getSwitchBarCheckState() {
        return false;
    }

    protected void onSwitchBarCheckChanged(Switch switchBar, boolean isChecked) {
        // Noop.
    }

    protected void onSetupFilter(Chip filterAnchor) {
        // Creating the ArrayAdapter instance having the categoryArray list
        List<PackageSet> menuItemList = viewModel.getAllPackageSetFilterItems();
        menuItemList.sort(new Comparator<PackageSet>() {
            @Override
            public int compare(PackageSet o1, PackageSet o2) {
                if (o1.isPrebuilt() != o2.isPrebuilt()) {
                    return o1.isPrebuilt() ? -1 : 1;
                }
                return Long.compare(o1.getCreateAt(), o2.getCreateAt());
            }
        });

        PackageSet currentPackageSet = viewModel.getCurrentPackageSet();
        if (currentPackageSet != null) {
            filterAnchor.setText(currentPackageSet.getLabel());
        }

        filterAnchor.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(thisActivity(), filterAnchor);
            for (int i = 0; i < menuItemList.size(); i++) {
                PackageSet pkgSetItem = menuItemList.get(i);
                popupMenu.getMenu().add(
                        1000,
                        i,
                        Menu.NONE,
                        pkgSetItem.getLabel());
            }
            popupMenu.setOnMenuItemClickListener(item -> {
                int index = item.getItemId();
                viewModel.setAppCategoryFilter(menuItemList.get(index).getId());
                filterAnchor.setText(menuItemList.get(index).getLabel());
                return false;
            });
            popupMenu.show();
        });
    }

    protected void onSetupChip(
            ViewGroup chipContainer,
            ChipGroup chipGroup,
            Chip chip1,
            Chip chip2,
            Chip chip3,
            Chip chip4) {
        chipGroup.setVisibility(View.GONE);
        chipContainer.setVisibility(View.GONE);
    }

    protected void onSetupFab(ExtendedFloatingActionButton fab) {
        fab.hide();
    }

    protected void onSetupDescription(CommonFeatureDescriptionBarLayoutBinding featureDescContainer) {
        String text = provideFeatureDescText();
        if (text == null) {
            featureDescContainer.getRoot().setVisibility(View.GONE);
            return;
        }

        boolean alreadyRead =
                CommonPreferences.getInstance()
                        .isFeatureDescRead(getApplicationContext(), provideFeatureDescKey());
        if (alreadyRead) {
            featureDescContainer.getRoot().setVisibility(View.GONE);
            return;
        }

        featureDescContainer.featureDescText.setText(text);
        featureDescContainer.featureDescButton.setOnClickListener(
                v -> {
                    CommonPreferences.getInstance()
                            .setFeatureDescRead(getApplicationContext(), provideFeatureDescKey(), true);
                    featureDescContainer.getRoot().setVisibility(View.GONE);
                });
    }

    @Nullable
    protected String provideFeatureDescText() {
        return null;
    }

    @NonNull
    protected String provideFeatureDescKey() {
        return getClass().getName();
    }

    protected CommonAppListFilterAdapter onCreateCommonAppListFilterAdapter() {
        return new CommonAppListFilterAdapter(onCreateAppItemViewClickListener(), false);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        binding.toolbar.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        onInflateOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_search);
        binding.searchView.setMenuItem(item);
        return true;
    }

    protected void onInflateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_common_list_filter, menu);
    }

    @Override
    public void onBackPressed() {
        if (binding.searchView.isSearchOpen()) {
            binding.searchView.closeSearch();
            return;
        }
        super.onBackPressed();
    }

    public static CommonAppListFilterViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory =
                ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(CommonAppListFilterViewModel.class);
    }
}
