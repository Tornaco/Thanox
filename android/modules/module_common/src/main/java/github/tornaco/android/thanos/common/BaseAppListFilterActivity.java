package github.tornaco.android.thanos.common;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.Comparator;
import java.util.List;

import github.tornaco.android.thanos.common.sort.AppSort;
import github.tornaco.android.thanos.core.pm.PackageSet;
import github.tornaco.android.thanos.module.common.R;
import github.tornaco.android.thanos.module.common.databinding.ActivityCommonListFilterBinding;
import github.tornaco.android.thanos.module.common.databinding.CommonFeatureDescriptionBarLayoutBinding;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.widget.SwitchBar;

public abstract class BaseAppListFilterActivity<VM extends CommonAppListFilterViewModel> extends ThemeActivity {

    protected VM viewModel;
    ActivityCommonListFilterBinding binding;

    @Override
    public boolean isF() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = onCreateViewModel(this);
        viewModel.bindFeatureId("thanox_app_feature_" + getClass().getName());
        binding = ActivityCommonListFilterBinding.inflate(LayoutInflater.from(this), null, false);
        setContentView(binding.getRoot());
        setupView();
        setupViewModel();
    }

    @CallSuper
    protected void setupView() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle(getTitleString());

        onSetupFilter(binding.filterChipContainer.filterChip);
        onSetupCustomFilter(binding.customFilterChipContainer.filterChip);
        onSetupSorter(binding.sortChipContainer.sortChip);

        onSetupChip(
                binding.chipContainer,
                binding.chipGroup,
                binding.chip1,
                binding.chip2,
                binding.chip3,
                binding.chip4);

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
        viewModel.setAppListLoader(onCreateListModelLoader());
        viewModel.start();

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    @NonNull
    protected abstract CommonAppListFilterViewModel.ListModelLoader onCreateListModelLoader();

    protected void onSetupSwitchBar(SwitchBar switchBar) {
        switchBar.setOnLabel(getString(R.string.common_switchbar_title_format, getTitleString()));
        switchBar.setOffLabel(getString(R.string.common_switchbar_title_format, getTitleString()));
        switchBar.setChecked(getSwitchBarCheckState());
        switchBar.addOnSwitchChangeListener(this::onSwitchBarCheckChanged);
    }

    protected boolean getSwitchBarCheckState() {
        return false;
    }

    protected void onSwitchBarCheckChanged(MaterialSwitch switchBar, boolean isChecked) {
        // Noop.
    }

    @SuppressLint("RestrictedApi")
    protected void onSetupSorter(Chip sorterAnchor) {
        AppSort[] appSortArray = AppSort.values();
        AppSort currentSort = viewModel.getCurrentAppSort();
        sorterAnchor.setText(currentSort.labelRes);

        sorterAnchor.setOnClickListener(view -> {
            MenuBuilder menuBuilder = new MenuBuilder(this);
            MenuPopupHelper menuPopupHelper = new MenuPopupHelper(this, menuBuilder, view);
            menuPopupHelper.setForceShowIcon(true);

            int reverseItemId = 10086;
            MenuItem reverseItem = menuBuilder.add(1000,
                    reverseItemId,
                    Menu.NONE,
                    R.string.common_sort_reverse);
            reverseItem.setCheckable(true);
            reverseItem.setChecked(viewModel.isSortReverse());
            reverseItem.setIcon(R.drawable.module_common_ic_arrow_up_down_line);

            for (int i = 0; i < appSortArray.length; i++) {
                AppSort sort = appSortArray[i];
                MenuItem sortItem = menuBuilder.add(1000,
                        i,
                        Menu.NONE,
                        sort.labelRes);
                boolean isSelected = viewModel.getCurrentAppSort() == sort;
                if (isSelected) {
                    sortItem.setTitle(getString(sort.labelRes) + " \uD83C\uDFAF");
                }
            }
            menuBuilder.setCallback(new MenuBuilder.Callback() {
                @Override
                public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                    int index = item.getItemId();
                    if (index == reverseItemId) {
                        viewModel.setSortReverse(!viewModel.isSortReverse());
                        item.setChecked(viewModel.isSortReverse());
                    } else {
                        viewModel.setAppSort(appSortArray[index]);
                        sorterAnchor.setText(appSortArray[index].labelRes);
                    }
                    return true;
                }

                @Override
                public void onMenuModeChange(@NonNull MenuBuilder menu) {
                    // Noop
                }
            });
            menuPopupHelper.show();
        });
    }

    @SuppressLint("RestrictedApi")
    protected void onSetupFilter(Chip filterAnchor) {
        // Creating the ArrayAdapter instance having the categoryArray list
        List<PackageSet> menuItemList = viewModel.getAllPackageSetFilterItems();
        menuItemList.sort(new Comparator<PackageSet>() {
            @Override
            public int compare(PackageSet o1, PackageSet o2) {
                if (o1.isPrebuilt() != o2.isPrebuilt()) {
                    return o1.isPrebuilt() ? 1 : -1;
                }
                return Long.compare(o1.getCreateAt(), o2.getCreateAt());
            }
        });

        PackageSet currentPackageSet = viewModel.getCurrentPackageSet();
        if (currentPackageSet != null) {
            filterAnchor.setText(currentPackageSet.getLabel());
        }

        filterAnchor.setOnClickListener(view -> {
            MenuBuilder menuBuilder = new MenuBuilder(this);
            MenuPopupHelper menuPopupHelper = new MenuPopupHelper(this, menuBuilder, view);
            menuPopupHelper.setForceShowIcon(true);
            for (int i = 0; i < menuItemList.size(); i++) {
                PackageSet pkgSetItem = menuItemList.get(i);
                MenuItem menuItem = menuBuilder.add(
                        1000,
                        i,
                        Menu.NONE,
                        pkgSetItem.getLabel());
                if (!pkgSetItem.isPrebuilt()) {
                    menuItem.setIcon(R.drawable.module_common_ic_baseline_add_reaction_24);
                }
            }
            menuBuilder.setCallback(new MenuBuilder.Callback() {
                @Override
                public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                    int index = item.getItemId();
                    viewModel.setAppCategoryFilter(menuItemList.get(index).getId());
                    filterAnchor.setText(menuItemList.get(index).getLabel());
                    return true;
                }

                @Override
                public void onMenuModeChange(@NonNull MenuBuilder menu) {
                    // Noop
                }
            });
            menuPopupHelper.show();
        });
    }

    protected void onSetupCustomFilter(Chip filterAnchor) {
        filterAnchor.setVisibility(View.GONE);
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

        featureDescContainer.featureDescView.setDescription(text);
        featureDescContainer.featureDescView.setOnCloseClickListener(() -> {
            CommonPreferences.getInstance()
                    .setFeatureDescRead(getApplicationContext(), provideFeatureDescKey(), true);
            featureDescContainer.getRoot().setVisibility(View.GONE);
            return null;
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

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        binding.toolbar.setTitle(title);
        binding.toolbarLayout.setTitle(title);
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
        if (closeSearch()) {
            return;
        }
        super.onBackPressed();
    }

    protected boolean closeSearch() {
        if (binding.searchView.isSearchOpen()) {
            binding.searchView.closeSearch();
            return true;
        }
        return false;
    }

    protected abstract VM onCreateViewModel(FragmentActivity activity);
}
