/*
 * Copyright (C) 2015 The Android Open Source Project
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
 */

package github.tornaco.android.thanos.installer.permission;

import android.os.Bundle;
import android.view.LayoutInflater;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import github.tornaco.android.thanos.installer.R;
import github.tornaco.android.thanos.installer.databinding.InstallerGrantPermissionsBinding;
import github.tornaco.android.thanos.theme.Theme;
import github.tornaco.android.thanos.theme.ThemeActivity;

public class GrantPermissionsActivity extends ThemeActivity {
    private InstallerGrantPermissionsBinding binding;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        binding = InstallerGrantPermissionsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setFinishOnTouchOutside(false);
    }

    @Override
    protected int getThemeRes(Theme theme) {
        switch (theme) {
            case Light:
                return R.style.InstallerPermission;
            case Dark:
                return R.style.InstallerPermissionDark;
        }
        return R.style.InstallerPermission;
    }

    public static GrantPermissionsViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(GrantPermissionsViewModel.class);
    }
}
