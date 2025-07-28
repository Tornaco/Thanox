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

package now.fortuitous.thanos.infinite;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import github.tornaco.android.thanos.util.ActivityUtils;

public class InfiniteZActivity2 extends InfiniteZActivity {
    public static void start(Context context) {
        ActivityUtils.startActivity(context, InfiniteZActivity2.class);
    }

    @Override
    protected BaseInfiniteZAppsViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(InfiniteZAppsViewModel2.class);
    }

    @Override
    protected int getTitleRes() {
        return github.tornaco.android.thanos.res.R.string.feature_title_infinite_z2;
    }

    @Override
    protected int getEnableDialogMsgRes() {
        return github.tornaco.android.thanos.res.R.string.feature_message_infinite_z2_enable;
    }

    @Override
    protected int getDisableDialogMsgRes() {
        return github.tornaco.android.thanos.res.R.string.feature_message_infinite_z2_disable;
    }

    @Override
    protected int getUninstallDialogMsgRes() {
        return github.tornaco.android.thanos.res.R.string.feature_message_infinite_z2_uninstall;
    }
}
