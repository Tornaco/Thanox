package github.tornaco.thanos.android.ops.ops;

import android.content.res.ColorStateList;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import github.tornaco.android.thanos.core.secure.ops.AppOpsManager;
import github.tornaco.thanos.android.ops.R;
import github.tornaco.thanos.android.ops.model.Op;
import github.tornaco.thanos.android.ops.model.OpGroup;
import util.Consumer;

public class DataBindingAdapters {

    @BindingAdapter("android:opIcon")
    public static void setAppIcon(ImageView imageView, Op op) {
        imageView.setImageResource(op.getIconRes());
    }

    @BindingAdapter("android:opGroups")
    public static void setOpGroups(RecyclerView recyclerView, List<OpGroup> groups) {
        @SuppressWarnings("unchecked")
        Consumer<List<OpGroup>> consumer = (Consumer<List<OpGroup>>) recyclerView.getAdapter();
        Objects.requireNonNull(consumer).accept(groups);
    }

    @BindingAdapter({"android:itemOpsStateImage"})
    public static void setItemOpsStateImage(ImageView view, Op op) {
        switch (op.getMode()) {
            case AppOpsManager.MODE_ALLOWED:
                view.setImageResource(R.drawable.module_ops_ic_checkbox_circle_fill);
                ColorStateList green = ColorStateList.valueOf(ContextCompat.getColor(view.getContext(), R.color.md_green_500));
                view.setImageTintList(green);
                break;
            case AppOpsManager.MODE_FOREGROUND:
                view.setImageResource(R.drawable.module_ops_ic_checkbox_circle_fill);
                ColorStateList amber = ColorStateList.valueOf(ContextCompat.getColor(view.getContext(), R.color.md_amber_700));
                view.setImageTintList(amber);
                break;
            case AppOpsManager.MODE_IGNORED:
                view.setImageResource(R.drawable.module_ops_ic_forbid_2_fill);
                ColorStateList red = ColorStateList.valueOf(ContextCompat.getColor(view.getContext(), R.color.md_red_500));
                view.setImageTintList(red);
                break;
        }
    }
}
