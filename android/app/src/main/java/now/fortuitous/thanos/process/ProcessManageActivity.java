/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package now.fortuitous.thanos.process;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.chip.Chip;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.R;
import now.fortuitous.app.BaseTrustedActivity;
import github.tornaco.android.thanos.databinding.ActivityProcessManageBinding;
import github.tornaco.android.thanos.util.ActivityUtils;

@Deprecated
public class ProcessManageActivity extends BaseTrustedActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, ProcessManageActivity.class);
    }

    private ProcessManageViewModel startManageViewModel;
    private ActivityProcessManageBinding binding;

    @Override
    public boolean isF() {
        return true;
    }

    @Override
    public boolean isADVF() {
        return true;
    }

    @Override
    @Verify
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProcessManageBinding.inflate(
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

        setTitle(R.string.feature_title_process_manage);
        onSetupFilter(binding.filterChip);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new ProcessManageFragment())
                .commitAllowingStateLoss();
    }

    protected void onSetupFilter(Chip filterAnchor) {
        //Creating the ArrayAdapter instance having the category list
        String[] categoryArray = getResources().getStringArray(R.array.process_manage_categories);
        filterAnchor.setText(categoryArray[ProcessManageViewModel.DEFAULT_CATEGORY_INDEX.ordinal()]);

        filterAnchor.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(thisActivity(), filterAnchor);
            for (int i = 0; i < categoryArray.length; i++) {
                popupMenu.getMenu().add(1000, i, Menu.NONE, categoryArray[i]);
            }
            popupMenu.setOnMenuItemClickListener(item -> {
                int index = item.getItemId();
                startManageViewModel.setAppCategoryFilter(index);
                filterAnchor.setText(categoryArray[index]);
                return false;
            });
            popupMenu.show();
        });
    }

    private void setupViewModel() {
        startManageViewModel = obtainViewModel(this);
        startManageViewModel.start();

        binding.setViewModel(startManageViewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    @Override
    @Verify
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (android.R.id.home == item.getItemId()) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        binding.toolbar.setTitle(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startManageViewModel.start();
    }

    public static ProcessManageViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(ProcessManageViewModel.class);
    }
}
