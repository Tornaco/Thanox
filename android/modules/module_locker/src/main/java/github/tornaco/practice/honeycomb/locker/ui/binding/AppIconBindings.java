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

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.theme.AppThemePreferences;
import github.tornaco.android.thanos.util.GlideApp;
import github.tornaco.android.thanos.util.GlideRequest;

/**
 * Contains {@link BindingAdapter}s for the {@link AppInfo} list.
 */
public class AppIconBindings {

    @BindingAdapter("android:iconFromPackage")
    public static void displayAppIcon(ImageView imageView, String pkg) {
        AppInfo appInfo = new AppInfo();
        appInfo.setPkgName(pkg);
        GlideRequest request = GlideApp.with(imageView.getContext())
                .load(appInfo);
        if (AppThemePreferences.getInstance().useRoundIcon(imageView.getContext())) {
            request = request.circleCrop();
        }
        request.into(imageView);
    }
}
