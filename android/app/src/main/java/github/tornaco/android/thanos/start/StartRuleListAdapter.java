package github.tornaco.android.thanos.start;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.databinding.StartRuleListItemBinding;
import util.Consumer;

class StartRuleListAdapter extends RecyclerView.Adapter<StartRuleListAdapter.VH>
        implements Consumer<List<StartRule>> {

    private final List<StartRule> startRules = new ArrayList<>();

    private StartRuleItemClickListener startRuleItemClickListener;

    StartRuleListAdapter(StartRuleItemClickListener startRuleItemClickListener) {
        this.startRuleItemClickListener = startRuleItemClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(StartRuleListItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        StartRule model = startRules.get(position);
        holder.itemBinding.setRule(model);
        holder.itemBinding.setRuleItemClickListener(startRuleItemClickListener);
        holder.itemBinding.setIsLastOne(position == getItemCount() - 1);
        holder.itemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return this.startRules.size();
    }

    @Override
    public void accept(List<StartRule> ruleInfoList) {
        this.startRules.clear();
        this.startRules.addAll(ruleInfoList);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        private StartRuleListItemBinding itemBinding;

        VH(@NonNull StartRuleListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public github.tornaco.android.thanos.databinding.StartRuleListItemBinding getItemBinding() {
            return this.itemBinding;
        }
    }
}
