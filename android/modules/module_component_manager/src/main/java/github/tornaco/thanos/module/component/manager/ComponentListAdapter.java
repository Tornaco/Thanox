package github.tornaco.thanos.module.component.manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.thanos.module.component.manager.databinding.ModuleComponentManagerComponentListItemBinding;
import github.tornaco.thanos.module.component.manager.model.ComponentModel;
import util.Consumer;

public class ComponentListAdapter extends RecyclerView.Adapter<ComponentListAdapter.VH>
        implements Consumer<List<ComponentModel>>,
        FastScrollRecyclerView.SectionedAdapter,
        FastScrollRecyclerView.MeasurableAdapter<ComponentListAdapter.VH> {

    private final List<ComponentModel> listModels = new ArrayList<>();

    @NonNull
    private final AppInfo appInfo;
    @NonNull
    private OnComponentItemSwitchChangeListener onComponentItemSwitchChangeListener;
    @NonNull
    private OnComponentItemClickListener onComponentItemClickListener;
    @NonNull
    private OnComponentRuleClickListener onComponentRuleClickListener;

    public ComponentListAdapter(@NonNull AppInfo appInfo,
                                @NonNull OnComponentItemSwitchChangeListener onComponentItemSwitchChangeListener,
                                @NonNull OnComponentItemClickListener onComponentItemClickListener,
                                @NonNull OnComponentRuleClickListener onComponentRuleClickListener) {
        this.appInfo = appInfo;
        this.onComponentItemSwitchChangeListener = onComponentItemSwitchChangeListener;
        this.onComponentItemClickListener = onComponentItemClickListener;
        this.onComponentRuleClickListener = onComponentRuleClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ModuleComponentManagerComponentListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ComponentModel componentModel = listModels.get(position);
        holder.binding.setApp(appInfo);
        holder.binding.setComponent(componentModel);
        holder.binding.appItemRoot.setOnClickListener(v -> onComponentItemClickListener.onComponentItemClick(v, componentModel));
        holder.binding.itemSwitch.setOnClickListener(v ->
                onComponentItemSwitchChangeListener.onComponentItemSwitchChange(componentModel, holder.binding.itemSwitch.isChecked()));

        holder.binding.ruleIcon.setVisibility(View.GONE);
        holder.binding.ruleLabel.setVisibility(View.GONE);
        if (componentModel.getComponentRule() != null) {
            if (componentModel.getComponentRule().getIconRes() > 0) {
                holder.binding.ruleIcon.setImageResource(componentModel.getComponentRule().getIconRes());
                holder.binding.ruleIcon.setVisibility(View.VISIBLE);
            }

            holder.binding.ruleLabel.setVisibility(View.VISIBLE);
            holder.binding.ruleLabel.setText(componentModel.getComponentRule().getLabel());
            holder.binding.ruleLabel.setOnClickListener(v -> onComponentRuleClickListener.onComponentRuleClick(v, componentModel.getComponentRule()));
        }
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return listModels.size();
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return String.valueOf(listModels.get(position).getName().charAt(0));
    }

    @Override
    public int getViewTypeHeight(RecyclerView recyclerView, @Nullable VH viewHolder, int viewType) {
        return recyclerView.getResources()
                .getDimensionPixelSize(github.tornaco.android.thanos.module.common.R.dimen.common_list_item_height);
    }

    @Override
    public void accept(List<ComponentModel> componentModels) {
        this.listModels.clear();
        this.listModels.addAll(componentModels);
        notifyDataSetChanged();
    }

    public List<ComponentModel> getListModels() {
        return this.listModels;
    }

    static final class VH extends RecyclerView.ViewHolder {
        private final ModuleComponentManagerComponentListItemBinding binding;

        VH(ModuleComponentManagerComponentListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public github.tornaco.thanos.module.component.manager.databinding.ModuleComponentManagerComponentListItemBinding getBinding() {
            return this.binding;
        }
    }
}
