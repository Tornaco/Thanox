package github.tornaco.android.thanos.common;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.ColorRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.module.common.R;
import github.tornaco.android.thanos.theme.AppThemePreferences;
import github.tornaco.android.thanos.util.GlideApp;
import github.tornaco.android.thanos.util.GlideRequest;
import github.tornaco.android.thanos.widget.GrayscaleTransformation;
import util.Consumer;

public class CommonDataBindingAdapters {
    private static final GrayscaleTransformation GRAY_SCALE_TRANSFORMATION = new GrayscaleTransformation();

    @BindingAdapter("android:iconThemeColor")
    public static void setIconTint(ImageView imageView, @ColorRes int res) {
        if (res == 0) return;
        imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), res));
    }

    @BindingAdapter("android:circleBgTint")
    public static void setCircleBgTint(ImageView imageView, @ColorRes int res) {
        if (res == 0) return;
        Drawable bg = AppCompatResources.getDrawable(imageView.getContext(), R.drawable.module_common_circle_bg_blue);
        if (bg == null) return;
        bg.setColorFilter(new PorterDuffColorFilter(imageView.getContext().getColor(res), PorterDuff.Mode.SRC_IN));
        imageView.setBackground(bg);
    }

    @BindingAdapter("android:appIcon")
    public static void setAppIcon(ImageView imageView, AppInfo appInfo) {
        if (appInfo != null && appInfo.getIconDrawable() > 0) {
            loadAppIconWithDrawable(imageView, appInfo);
            return;
        }

        if (appInfo != null && !TextUtils.isEmpty(appInfo.getIconUrl())) {
            loadAppIconWithUrl(imageView, appInfo);
            return;
        }

        loadAppIconWithAppInfo(imageView, appInfo);
    }

    private static void loadAppIconWithAppInfo(ImageView imageView, AppInfo appInfo) {
        GlideRequest<Drawable> request =
                GlideApp.with(imageView)
                        .load(appInfo)
                        .error(R.mipmap.ic_fallback_app_icon)
                        .fallback(R.mipmap.ic_fallback_app_icon)
                        .transition(GenericTransitionOptions.with(R.anim.grow_fade_in));
        if (AppThemePreferences.getInstance().useRoundIcon(imageView.getContext())) {
            request = request.circleCrop();
        }
        if (appInfo != null && appInfo.disabled()) {
            request = request.transform(GRAY_SCALE_TRANSFORMATION);
        }
        request.into(imageView);
    }

    private static void loadAppIconWithUrl(ImageView imageView, AppInfo appInfo) {
        Glide.with(imageView)
                .load(appInfo.getIconUrl())
                .error(R.mipmap.ic_fallback_app_icon)
                .fallback(R.mipmap.ic_fallback_app_icon)
                .transition(GenericTransitionOptions.with(R.anim.grow_fade_in))
                .into(imageView);
    }

    private static void loadAppIconWithDrawable(ImageView imageView, AppInfo appInfo) {
        RequestBuilder<Drawable> transition =
                Glide.with(imageView)
                        .load(appInfo.getIconDrawable())
                        .error(R.mipmap.ic_fallback_app_icon)
                        .fallback(R.mipmap.ic_fallback_app_icon)
                        .transition(GenericTransitionOptions.with(R.anim.grow_fade_in));
        if (appInfo.disabled()) {
            transition = transition.transform(GRAY_SCALE_TRANSFORMATION);
        }
        transition.into(imageView);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @BindingAdapter({"android:listModels"})
    public static void setProcessModels(RecyclerView view, List<AppListModel> models) {
        Consumer<List<AppListModel>> consumer = (Consumer<List<AppListModel>>) view.getAdapter();
        consumer.accept(models);
    }

    @BindingAdapter({"android:switchApp", "android:switchListener"})
    public static void setSwitchAppAndListener(
            Switch view, AppInfo appInfo, final AppItemActionListener listener) {
        view.setOnClickListener(
                (b) -> listener.onAppItemSwitchStateChange(appInfo, ((Checkable) b).isChecked()));
    }

    @BindingAdapter({"android:switchApp", "android:switchListener"})
    public static void setSwitchAppAndListener(
            SwitchMaterial view, AppInfo appInfo, final AppItemActionListener listener) {
        view.setOnClickListener(
                (b) -> listener.onAppItemSwitchStateChange(appInfo, ((Checkable) b).isChecked()));
    }
}
