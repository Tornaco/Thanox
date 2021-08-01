package github.tornaco.android.thanos.power;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.databinding.StandbyRuleListItemBinding;
import lombok.Getter;
import util.Consumer;

class StandbyRuleListAdapter extends RecyclerView.Adapter<StandbyRuleListAdapter.VH>
        implements Consumer<List<StandbyRule>> {

    private final List<StandbyRule> standbyRules = new ArrayList<>();

    private StandbyRuleItemClickListener ruleItemClickListener;

    StandbyRuleListAdapter(StandbyRuleItemClickListener ruleItemClickListener) {
        this.ruleItemClickListener = ruleItemClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(StandbyRuleListItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        StandbyRule model = standbyRules.get(position);
        holder.itemBinding.setRule(model);
        holder.itemBinding.setRuleItemClickListener(ruleItemClickListener);
        holder.itemBinding.setIsLastOne(position == getItemCount() - 1);
        holder.itemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return this.standbyRules.size();
    }

    @Override
    public void accept(List<StandbyRule> ruleInfoList) {
        this.standbyRules.clear();
        this.standbyRules.addAll(ruleInfoList);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        @Getter
        private StandbyRuleListItemBinding itemBinding;

        VH(@NonNull StandbyRuleListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }
}
