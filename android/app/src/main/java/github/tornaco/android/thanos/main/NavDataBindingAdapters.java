package github.tornaco.android.thanos.main;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.widget.ImageView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import java.util.Objects;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.dashboard.Tile;

public class NavDataBindingAdapters {

    @BindingAdapter("android:navTile")
    public static void setCircleBgTint(ImageView imageView, Tile tile) {
        try {
            LayerDrawable layerDrawable = (LayerDrawable) AppCompatResources.getDrawable(imageView.getContext(), tile.getIconRes());
            Drawable findDrawableByLayerId = Objects.requireNonNull(layerDrawable).findDrawableByLayerId(R.id.settings_ic_foreground);
            if (findDrawableByLayerId != null) {
                findDrawableByLayerId.setTint(ContextCompat.getColor(imageView.getContext(), tile.getThemeColor()));
                layerDrawable.setDrawableByLayerId(R.id.settings_ic_foreground, findDrawableByLayerId);
            }
            imageView.setImageDrawable(layerDrawable);
        } catch (Throwable ignored) {

        }
    }
}
