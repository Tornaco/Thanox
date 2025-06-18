package github.tornaco.thanos.android.module.profile;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import github.tornaco.android.thanos.core.profile.RuleInfo;
import github.tornaco.android.thanos.databinding.ModuleProfileRuleListItemBinding;
import si.virag.fuzzydateformatter.FuzzyDateTimeFormatter;
import util.Consumer;

class RuleListAdapter extends RecyclerView.Adapter<RuleListAdapter.VH>
        implements Consumer<List<github.tornaco.thanos.android.module.profile.RuleUiItem>>
        , FastScrollRecyclerView.SectionedAdapter,
        FastScrollRecyclerView.MeasurableAdapter<RuleListAdapter.VH> {

    private final List<github.tornaco.thanos.android.module.profile.RuleUiItem> ruleInfoList = new ArrayList<>();

    private final RuleItemClickListener ruleItemClickListener;
    private final RuleItemSwitchChangeListener ruleItemSwitchChangeListener;
    private final RuleItemDeleteClickListener ruleItemDeleteClickListener;

    RuleListAdapter(RuleItemClickListener ruleItemClickListener,
                    RuleItemDeleteClickListener ruleItemDeleteClickListener,
                    RuleItemSwitchChangeListener ruleItemSwitchChangeListener) {
        this.ruleItemClickListener = ruleItemClickListener;
        this.ruleItemSwitchChangeListener = ruleItemSwitchChangeListener;
        this.ruleItemDeleteClickListener = ruleItemDeleteClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ModuleProfileRuleListItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        github.tornaco.thanos.android.module.profile.RuleUiItem item = ruleInfoList.get(position);
        RuleInfo model = item.ruleInfo;
        holder.itemBinding.setRule(item);
        holder.itemBinding.setRuleItemClickListener(ruleItemClickListener);
        holder.itemBinding.setSwitchListener(ruleItemSwitchChangeListener);
        holder.itemBinding.setIsLastOne(position == getItemCount() - 1);
        holder.itemBinding.switchContainer.setOnClickListener(v -> holder.itemBinding.itemSwitch.performClick());
        holder.itemBinding.setUpdateTimeString(FuzzyDateTimeFormatter.getTimeAgo(holder.itemView.getContext(), new Date(model.getUpdateTimeMills())));
        holder.itemBinding.setFormattedVersionName("v" + model.getVersionCode());
        holder.itemBinding.delete.setOnClickListener(v -> ruleItemDeleteClickListener.onDeleteItemClick(model));
        holder.itemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return this.ruleInfoList.size();
    }

    @Override
    public void accept(List<RuleUiItem> ruleInfoList) {
        this.ruleInfoList.clear();
        this.ruleInfoList.addAll(ruleInfoList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        RuleInfo ruleInfo = ruleInfoList.get(position).ruleInfo;
        String name = ruleInfo.getName();
        if (TextUtils.isEmpty(name.trim())) {
            name = "*";
        }
        return String.valueOf(name.charAt(0));
    }

    @Override
    public int getViewTypeHeight(RecyclerView recyclerView, @Nullable VH viewHolder, int viewType) {
        return recyclerView
                .getResources()
                .getDimensionPixelSize(github.tornaco.android.thanos.module.common.R.dimen.common_list_item_height);
    }

    static class VH extends RecyclerView.ViewHolder {
        private final ModuleProfileRuleListItemBinding itemBinding;

        VH(@NonNull ModuleProfileRuleListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public ModuleProfileRuleListItemBinding getItemBinding() {
            return this.itemBinding;
        }
    }
}
