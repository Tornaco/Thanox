package github.tornaco.thanos.module.component.manager;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import github.tornaco.thanos.module.component.manager.model.ComponentModel;
import util.Consumer;

public class DataBindingAdapters {

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @BindingAdapter("android:components")
    public static void setComponents(RecyclerView view, List<ComponentModel> models) {
        Consumer<List<ComponentModel>> consumer = (Consumer<List<ComponentModel>>) view.getAdapter();
        consumer.accept(models);
    }
}
