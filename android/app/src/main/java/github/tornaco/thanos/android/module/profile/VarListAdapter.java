package github.tornaco.thanos.android.module.profile;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.core.profile.GlobalVar;
import github.tornaco.android.thanos.databinding.ModuleProfileVarListItemBinding;
import util.Consumer;

class VarListAdapter extends RecyclerView.Adapter<VarListAdapter.VH>
        implements Consumer<List<GlobalVar>> {

    private final List<GlobalVar> varArrayList = new ArrayList<>();

    private VarItemClickListener varItemClickListener;

    VarListAdapter(VarItemClickListener varItemClickListener) {
        this.varItemClickListener = varItemClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ModuleProfileVarListItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        GlobalVar model = varArrayList.get(position);
        holder.itemBinding.setVar(model);
        holder.itemBinding.setVarItemClickListener(varItemClickListener);
        holder.itemBinding.setIsLastOne(position == getItemCount() - 1);
        holder.itemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return this.varArrayList.size();
    }

    @Override
    public void accept(List<GlobalVar> ruleInfoList) {
        this.varArrayList.clear();
        this.varArrayList.addAll(ruleInfoList);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        private ModuleProfileVarListItemBinding itemBinding;

        VH(@NonNull ModuleProfileVarListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public ModuleProfileVarListItemBinding getItemBinding() {
            return this.itemBinding;
        }
    }
}
