package github.tornaco.android.thanos.databinding;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.matrixxun.starry.badgetextview.MaterialBadgeTextView;

import java.util.List;
import java.util.Objects;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.process.RunningState;
import github.tornaco.android.thanos.dashboard.DashboardAdapter;
import github.tornaco.android.thanos.dashboard.DashboardCardAdapter;
import github.tornaco.android.thanos.dashboard.Tile;
import github.tornaco.android.thanos.dashboard.TileGroup;
import github.tornaco.android.thanos.main.State;
import github.tornaco.android.thanos.power.StandbyRule;
import github.tornaco.android.thanos.start.StartRule;
import github.tornaco.android.thanos.theme.AppThemePreferences;
import github.tornaco.android.thanos.util.GlideApp;
import github.tornaco.android.thanos.util.GlideRequest;
import util.Consumer;

public class DataBindingAdapters {

    @BindingAdapter("android:activeStatusFabImage")
    public static void setActiveStatusFabImage(FloatingActionButton fab, ObservableField<State> state) {
        if (state.get() == null) {
            return;
        }
        switch (Objects.requireNonNull(state.get())) {
            case Active:
                fab.setImageResource(R.drawable.module_ops_ic_checkbox_circle_fill);
                fab.setSupportBackgroundTintList(ColorStateList.valueOf(fab.getResources().getColor(R.color.accent)));
                break;
            case InActive:
                fab.setImageResource(R.drawable.ic_forbid_fill);
                fab.setSupportBackgroundTintList(ColorStateList.valueOf(fab.getResources().getColor(R.color.md_red_600)));
                break;
            case RebootNeeded:
                fab.setImageResource(R.drawable.ic_information_fill);
                fab.setSupportBackgroundTintList(ColorStateList.valueOf(fab.getResources().getColor(R.color.md_amber_600)));
                break;
            default:
                break;
        }
    }

    @BindingAdapter("android:boostStatusAppsCount")
    public static void setBoostStatusAppsCount(TextView textView, int count) {
        textView.setText(String.valueOf(count));
    }

    @BindingAdapter("android:privacyAppsCount")
    public static void setPrivacyAppsCount(TextView textView, ObservableInt count) {
        textView.setText(String.valueOf(count.get()));
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("android:boostStatusMemPercent")
    public static void setBoostStatusMemPercent(TextView textView, int percent) {
        textView.setText(percent + "%");
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("android:boostStatusStoragePercent")
    public static void setBoostStatusStoragePercent(TextView textView, ObservableInt percent) {
        textView.setText(percent.get() + "%");
    }

    @BindingAdapter("android:features")
    public static void setFeatures(RecyclerView recyclerView, ObservableList<Tile> features) {
        DashboardAdapter adapter = (DashboardAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.replaceData(features);
        }
    }

    @BindingAdapter("android:tileGroups")
    public static void setTileGroups(RecyclerView recyclerView, ObservableList<TileGroup> features) {
        DashboardCardAdapter adapter = (DashboardCardAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.replaceData(features);
        }
    }

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

    @BindingAdapter("android:featureIcon")
    public static void setFeatureIcon(ImageView imageView, @DrawableRes int res) {
        imageView.setImageResource(res);
    }

    @BindingAdapter("android:iconThemeColor")
    public static void setIconTint(ImageView imageView, @ColorRes int res) {
        if (res == 0) return;
        imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), res));
    }


    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @BindingAdapter({"android:appsAllowed", "android:appsDisAllowed"})
    public static void setAppInfoList(RecyclerView recyclerView,
                                      ObservableList appsAllowed,
                                      ObservableList appsDisAllowed) {
        Consumer<Pair<List<AppInfo>, List<AppInfo>>> adapter = (Consumer) recyclerView.getAdapter();
        adapter.accept(Pair.create(appsAllowed, appsDisAllowed));
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
        if (AppThemePreferences.getInstance().useRoundIcon(imageView.getContext())) {
            request = request.circleCrop();
        }
        request.into(imageView);
    }

    @BindingAdapter("android:mergeItemDesc")
    public static void setMergeItemDesc(TextView textView, RunningState.MergedItem mergeItem) {
        if (mergeItem.mBackground) {
            textView.setText(R.string.title_cached_background_process);
        } else {
            int resid = R.string.running_processes_item_description_s_s;
            if (mergeItem.mLastNumProcesses != 1) {
                resid = mergeItem.mLastNumServices != 1
                        ? R.string.running_processes_item_description_p_p
                        : R.string.running_processes_item_description_p_s;
            } else if (mergeItem.mLastNumServices != 1) {
                resid = R.string.running_processes_item_description_s_p;
            }
            String description = textView.getContext().getResources().getString(resid,
                    mergeItem.mLastNumProcesses,
                    mergeItem.mLastNumServices);
            textView.setText(description);
        }
    }

    @BindingAdapter({"android:thanosStateText"})
    public static void setThanosStateText(TextView view, @NonNull ObservableField<State> state) {
        switch (Objects.requireNonNull(state.get())) {
            case Active:
                view.setText(view.getResources().getString(R.string.status_active));
                break;
            case InActive:
                view.setText(view.getResources().getString(R.string.status_not_active));
                break;
            case RebootNeeded:
                view.setText(view.getResources().getString(R.string.status_need_reboot));
                break;
            default:
                view.setText(view.getResources().getString(R.string.status_active));
                break;
        }
    }

    @BindingAdapter({"android:thanosStateText"})
    public static void setThanosStateText(MaterialBadgeTextView view, @NonNull ObservableField<State> state) {
        setThanosStateText((TextView) view, state);
    }

    @BindingAdapter({"android:thanosStateTint"})
    public static void setThanosStateTint(ImageView view, @NonNull ObservableField<State> state) {
        switch (Objects.requireNonNull(state.get())) {
            case Active:
                setIconTint(view, R.color.md_green_500);
                break;
            case InActive:
                setIconTint(view, R.color.md_red_500);
                break;
            case RebootNeeded:
                setIconTint(view, R.color.md_amber_500);
                break;
        }
    }
}
