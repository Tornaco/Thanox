package github.tornaco.thanos.android.ops.ops.remind;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.widget.section.SectioningAdapter;
import github.tornaco.thanos.android.ops.databinding.ModuleOpsItemFooterBinding;
import github.tornaco.thanos.android.ops.databinding.ModuleOpsItemHeaderBinding;
import github.tornaco.thanos.android.ops.databinding.ModuleOpsItemRemindOpsCheckableBinding;
import github.tornaco.thanos.android.ops.model.Op;
import github.tornaco.thanos.android.ops.model.OpGroup;
import util.Consumer;

public class RemindOpsListAdapter extends SectioningAdapter implements Consumer<List<OpGroup>> {

    private final OpItemSwitchChangeListener itemSwitchChangeListener;

    private final List<OpGroup> opGroups = new ArrayList<>();

    RemindOpsListAdapter(@NonNull OpItemSwitchChangeListener itemSwitchChangeListener) {
        this.itemSwitchChangeListener = itemSwitchChangeListener;
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemUserType) {
        return new IVH(ModuleOpsItemRemindOpsCheckableBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerUserType) {
        return new HVH(ModuleOpsItemHeaderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public FooterViewHolder onCreateFooterViewHolder(ViewGroup parent, int footerUserType) {
        return new FVH(ModuleOpsItemFooterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, int sectionIndex, int headerUserType) {
        super.onBindHeaderViewHolder(viewHolder, sectionIndex, headerUserType);
        HVH hvh = (HVH) viewHolder;
        hvh.binding.setGroup(opGroups.get(sectionIndex));
        hvh.binding.executePendingBindings();
    }

    @Override
    public void onBindItemViewHolder(ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemUserType) {
        super.onBindItemViewHolder(viewHolder, sectionIndex, itemIndex, itemUserType);
        IVH ivh = (IVH) viewHolder;
        OpGroup opGroup = opGroups.get(sectionIndex);
        Op op = opGroup.getOpList().get(itemIndex);
        ivh.binding.setOp(op);
        ivh.binding.appItemRoot.setOnClickListener(v -> ivh.binding.itemSwitch.performClick());
        ivh.binding.executePendingBindings();
        ivh.binding.itemSwitch.setOnClickListener(v -> itemSwitchChangeListener.onOpItemSwitchChanged(op, ((Checkable) v).isChecked()));
    }

    @Override
    public void onBindFooterViewHolder(FooterViewHolder viewHolder, int sectionIndex, int footerUserType) {
        super.onBindFooterViewHolder(viewHolder, sectionIndex, footerUserType);
    }

    @Override
    public void accept(List<OpGroup> opGroups) {
        this.opGroups.clear();
        this.opGroups.addAll(opGroups);
        notifyAllSectionsDataSetChanged();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return opGroups.get(sectionIndex).getOpList().size();
    }

    @Override
    public int getNumberOfSections() {
        return opGroups.size();
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }

    @Override
    public boolean doesSectionHaveFooter(int sectionIndex) {
        return true;
    }

    final static class HVH extends HeaderViewHolder {
        private ModuleOpsItemHeaderBinding binding;

        HVH(ModuleOpsItemHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public github.tornaco.thanos.android.ops.databinding.ModuleOpsItemHeaderBinding getBinding() {
            return this.binding;
        }
    }

    final static class IVH extends ItemViewHolder {
        private ModuleOpsItemRemindOpsCheckableBinding binding;

        IVH(ModuleOpsItemRemindOpsCheckableBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public github.tornaco.thanos.android.ops.databinding.ModuleOpsItemRemindOpsCheckableBinding getBinding() {
            return this.binding;
        }
    }

    final static class FVH extends FooterViewHolder {
        private ModuleOpsItemFooterBinding binding;

        FVH(ModuleOpsItemFooterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public github.tornaco.thanos.android.ops.databinding.ModuleOpsItemFooterBinding getBinding() {
            return this.binding;
        }
    }
}
