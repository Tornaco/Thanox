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

package now.fortuitous.thanos.databinding;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.List;

import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.process.RunningState;
import github.tornaco.android.thanos.theme.CommonAppPrefs;
import github.tornaco.android.thanos.util.GlideApp;
import github.tornaco.android.thanos.util.GlideRequest;
import now.fortuitous.thanos.power.StandbyRule;
import now.fortuitous.thanos.start.StartRule;
import util.Consumer;

public class DataBindingAdapters {

    @BindingAdapter("android:startRules")
    public static void setStartRules(RecyclerView recyclerView, ObservableList<StartRule> rules) {
        @SuppressWarnings("unchecked")
        Consumer<List<StartRule>> adapter = (Consumer<List<StartRule>>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.accept(rules);
        }
    }

    @BindingAdapter("android:standByRules")
    public static void setStandByRules(RecyclerView recyclerView, ObservableList<StandbyRule> rules) {
        @SuppressWarnings("unchecked")
        Consumer<List<StandbyRule>> adapter = (Consumer<List<StandbyRule>>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.accept(rules);
        }
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @BindingAdapter({"android:mergedRunningStateItems"})
    public static void setProcessModels(RecyclerView view, List<RunningState.MergedItem> processModels) {
        Consumer<List<RunningState.MergedItem>> consumer = (Consumer<List<RunningState.MergedItem>>) view.getAdapter();
        consumer.accept(processModels);
    }

    @BindingAdapter("android:runningItemIcon")
    public static void setRunningItemIcon(ImageView imageView, RunningState.MergedItem mergedItem) {
        AppInfo appInfo = new AppInfo();
        appInfo.setPkgName(mergedItem.mPackageInfo.packageName);
        GlideRequest request = GlideApp.with(imageView)
                .load(appInfo)
                .error(github.tornaco.android.thanos.module.common.R.mipmap.ic_fallback_app_icon)
                .fallback(github.tornaco.android.thanos.module.common.R.mipmap.ic_fallback_app_icon)
                .transition(GenericTransitionOptions.with(github.tornaco.android.thanos.module.common.R.anim.grow_fade_in));
        if (CommonAppPrefs.getInstance().useRoundIcon(imageView.getContext())) {
            request = request.circleCrop();
        }
        request.into(imageView);
    }

    @BindingAdapter("android:mergeItemDesc")
    public static void setMergeItemDesc(TextView textView, RunningState.MergedItem mergeItem) {
        if (mergeItem.mBackground) {
            textView.setText(github.tornaco.android.thanos.res.R.string.title_cached_background_process);
        } else {
            int resid = github.tornaco.android.thanos.res.R.string.running_processes_item_description_s_s;
            if (mergeItem.mLastNumProcesses != 1) {
                resid = mergeItem.mLastNumServices != 1
                        ? github.tornaco.android.thanos.res.R.string.running_processes_item_description_p_p
                        : github.tornaco.android.thanos.res.R.string.running_processes_item_description_p_s;
            } else if (mergeItem.mLastNumServices != 1) {
                resid = github.tornaco.android.thanos.res.R.string.running_processes_item_description_s_p;
            }
            String description = textView.getContext().getResources().getString(resid,
                    mergeItem.mLastNumProcesses,
                    mergeItem.mLastNumServices);
            textView.setText(description);
        }
    }
}
