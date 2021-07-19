package github.tornaco.android.thanox.module.activity.trampoline;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanox.module.activity.trampoline.databinding.ModuleActivityTrampolineCompReplacementListItemBinding;
import lombok.Getter;
import util.Consumer;

class ActivityTrampolineAdapter extends RecyclerView.Adapter<ActivityTrampolineAdapter.VH>
        implements Consumer<List<ActivityTrampolineModel>> {

    private final List<ActivityTrampolineModel> replacements = new ArrayList<>();

    private ActivityTrampolineItemClickListener listener;

    ActivityTrampolineAdapter(ActivityTrampolineItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ModuleActivityTrampolineCompReplacementListItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ActivityTrampolineModel model = replacements.get(position);
        holder.itemBinding.setReplacement(model.getReplacement());
        holder.itemBinding.setApp(model.getApp());
        holder.itemBinding.setListener(listener);
        holder.itemBinding.setItemView(holder.itemView);
        holder.itemBinding.setIsLastOne(position == getItemCount() - 1);
        holder.itemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return this.replacements.size();
    }

    @Override
    public void accept(List<ActivityTrampolineModel> componentReplacements) {
        this.replacements.clear();
        this.replacements.addAll(componentReplacements);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        @Getter
        private ModuleActivityTrampolineCompReplacementListItemBinding itemBinding;

        VH(@NonNull ModuleActivityTrampolineCompReplacementListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }
}
