/*
 * Copyright 2016, The Android Open Source Project
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

package github.tornaco.practice.honeycomb.locker.ui.binding;


import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.BindingAdapter;

import github.tornaco.practice.honeycomb.locker.R;
import github.tornaco.practice.honeycomb.locker.ui.setup.SetupViewModel;

public class SetupToolBarBindings {

    @BindingAdapter("android:toolbarAction")
    public static void bindWithPkg(Toolbar toolbar, SetupViewModel setupViewModel) {
        toolbar.setTitle(R.string.module_locker_locker_key_setup);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupViewModel.cancel();
            }
        });
    }
}
