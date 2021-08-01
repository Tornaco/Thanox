package github.tornaco.android.thanos.apps;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.common.AppItemClickListener;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.databinding.ItemSuggestedAppBinding;
import lombok.Getter;
import util.Consumer;

public class SuggestedAppsAdapter extends RecyclerView.Adapter<SuggestedAppsAdapter.VH>
        implements Consumer<List<AppListModel>> {

    private final List<AppListModel> processModels = new ArrayList<>();

    @Nullable
    private final AppItemClickListener itemViewClickListener;

    public SuggestedAppsAdapter(
            @Nullable AppItemClickListener itemViewClickListener) {
        this.itemViewClickListener = itemViewClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemSuggestedAppBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        AppListModel model = processModels.get(position);
        holder.binding.setApp(model.appInfo);
        holder.binding.setListener(itemViewClickListener);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return processModels.size();
    }

    @Override
    public void accept(List<AppListModel> processModels) {
        this.processModels.clear();
        this.processModels.addAll(processModels);
        notifyDataSetChanged();
    }

    @Getter
    static final class VH extends RecyclerView.ViewHolder {
        private ItemSuggestedAppBinding binding;

        VH(@NonNull ItemSuggestedAppBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
