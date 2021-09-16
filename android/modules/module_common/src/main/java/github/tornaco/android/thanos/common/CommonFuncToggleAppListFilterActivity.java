package github.tornaco.android.thanos.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.chip.ChipGroup;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import github.tornaco.android.thanos.module.common.R;
import github.tornaco.android.thanos.module.common.databinding.ActivityCommonFuncToggleListFilterBinding;
import github.tornaco.android.thanos.module.common.databinding.CommonFeatureDescriptionBarLayoutBinding;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.widget.ModernAlertDialog;
import github.tornaco.android.thanos.widget.SwitchBar;


public abstract class CommonFuncToggleAppListFilterActivity extends ThemeActivity {

    private CommonFuncToggleAppListFilterViewModel commonFuncToggleListFilterViewModel;
    private ActivityCommonFuncToggleListFilterBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommonFuncToggleListFilterBinding.inflate(
                LayoutInflater.from(this), null, false);
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

        onSetupFilterSpinner(binding.spinner);
        onSetupChip(binding.chipContainer, binding.chipGroup);

        // List.
        binding.apps.setLayoutManager(new LinearLayoutManager(this));
        binding.apps.setAdapter(new CommonFuncToggleAppListFilterAdapter(onCreateAppItemSelectStateChangeListener(), null));
        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                commonFuncToggleListFilterViewModel.start();
            }
        });
        binding.swipe.setColorSchemeColors(getResources().getIntArray(R.array.common_swipe_refresh_colors));

        // Switch.
        onSetupSwitchBar(binding.switchBarContainer.switchBar);

        // Search.
        binding.searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                commonFuncToggleListFilterViewModel.setSearchText(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                commonFuncToggleListFilterViewModel.setSearchText(newText);
                return true;
            }
        });

        binding.searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                // Noop.
            }

            @Override
            public void onSearchViewClosed() {
                commonFuncToggleListFilterViewModel.clearSearchText();
            }
        });

        onSetupDescription(binding.featureDescContainer);
    }

    protected void onSetupFilterSpinner(AppCompatSpinner spinner) {
        //Creating the ArrayAdapter instance having the category list
        String[] category = getResources().getStringArray(R.array.common_app_categories);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.ghost_text_view, category);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                commonFuncToggleListFilterViewModel.setAppCategoryFilter(index);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    protected void onSetupChip(ViewGroup chipContainer, ChipGroup chipGroup) {
        chipGroup.setVisibility(View.GONE);
        chipContainer.setVisibility(View.GONE);
    }

    protected void onSetupSwitchBar(SwitchBar switchBar) {
        switchBar.setChecked(getSwitchBarCheckState());
        switchBar.addOnSwitchChangeListener(new SwitchBar.OnSwitchChangeListener() {
            @Override
            public void onSwitchChanged(Switch switchBar1, boolean isChecked) {
                CommonFuncToggleAppListFilterActivity.this.onSwitchBarCheckChanged(switchBar1, isChecked);
            }
        });
    }

    protected boolean getSwitchBarCheckState() {
        return false;
    }

    protected void onSwitchBarCheckChanged(Switch switchBar, boolean isChecked) {
        // Noop.
    }

    protected void onSetupDescription(CommonFeatureDescriptionBarLayoutBinding featureDescContainer) {
        String text = provideFeatureDescText();
        if (text == null) {
            featureDescContainer.getRoot().setVisibility(View.GONE);
            return;
        }

        boolean alreadyRead = CommonPreferences.getInstance().isFeatureDescRead(
                getApplicationContext(),
                provideFeatureDescKey());
        if (alreadyRead) {
            featureDescContainer.getRoot().setVisibility(View.GONE);
            return;
        }

        featureDescContainer.featureDescText.setText(text);
        featureDescContainer.featureDescButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonPreferences.getInstance().setFeatureDescRead(
                        CommonFuncToggleAppListFilterActivity.this.getApplicationContext(),
                        CommonFuncToggleAppListFilterActivity.this.provideFeatureDescKey(), true);
                featureDescContainer.getRoot().setVisibility(View.GONE);
            }
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

    @StringRes
    protected int getTitleRes() {
        return 0;
    }

    @NonNull
    protected abstract String getTitleString();

    @NonNull
    protected abstract OnAppItemSelectStateChangeListener onCreateAppItemSelectStateChangeListener();

    private void setupViewModel() {
        commonFuncToggleListFilterViewModel = obtainViewModel(this);
        commonFuncToggleListFilterViewModel.setListModelLoader(onCreateListModelLoader());
        commonFuncToggleListFilterViewModel.setSelectStateChangeListener(onCreateAppItemSelectStateChangeListener());
        commonFuncToggleListFilterViewModel.start();

        binding.setViewModel(commonFuncToggleListFilterViewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    @NonNull
    protected abstract CommonFuncToggleAppListFilterViewModel.ListModelLoader onCreateListModelLoader();

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (android.R.id.home == item.getItemId()) finish();
        if (R.id.action_select_all == item.getItemId()) {
            ModernAlertDialog dialog = new ModernAlertDialog(thisActivity());
            dialog.setDialogTitle(getString(R.string.common_menu_title_select_all));
            dialog.setDialogMessage(getString(R.string.common_dialog_message_are_you_sure));
            dialog.setPositive(getString(android.R.string.ok));
            dialog.setNegative(getString(android.R.string.cancel));
            dialog.setOnPositive(() -> commonFuncToggleListFilterViewModel.selectAll());
            dialog.show();
            return true;
        }
        if (R.id.action_un_select_all == item.getItemId()) {
            ModernAlertDialog dialog = new ModernAlertDialog(thisActivity());
            dialog.setDialogTitle(getString(R.string.common_menu_title_un_select_all));
            dialog.setDialogMessage(getString(R.string.common_dialog_message_are_you_sure));
            dialog.setPositive(getString(android.R.string.ok));
            dialog.setNegative(getString(android.R.string.cancel));
            dialog.setOnPositive(() -> commonFuncToggleListFilterViewModel.unSelectAll());
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        binding.toolbar.setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        onInflateOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_search);
        binding.searchView.setMenuItem(item);
        return true;
    }

    protected void onInflateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_common_func_toggle_list_filter, menu);
    }

    @Override
    public void onBackPressed() {
        if (binding.searchView.isSearchOpen()) {
            binding.searchView.closeSearch();
            return;
        }
        super.onBackPressed();
    }

    public static CommonFuncToggleAppListFilterViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(CommonFuncToggleAppListFilterViewModel.class);
    }
}
