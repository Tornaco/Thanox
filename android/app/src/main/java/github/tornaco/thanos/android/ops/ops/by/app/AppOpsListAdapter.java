package github.tornaco.thanos.android.ops.ops.by.app;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.secure.ops.AppOpsManager;
import github.tornaco.android.thanos.databinding.ModuleOpsItemFooterBinding;
import github.tornaco.android.thanos.databinding.ModuleOpsItemHeaderBinding;
import github.tornaco.android.thanos.databinding.ModuleOpsItemOpsCheckableBinding;
import github.tornaco.android.thanos.widget.section.SectioningAdapter;
import github.tornaco.thanos.android.ops.model.Op;
import github.tornaco.thanos.android.ops.model.OpGroup;
import util.Consumer;

public class AppOpsListAdapter extends SectioningAdapter implements Consumer<List<OpGroup>> {

    private final AppInfo appInfo;

    private final List<OpGroup> opGroups = new ArrayList<>();

    private Activity activity;

    AppOpsListAdapter(@NonNull Activity activity, @NonNull AppInfo appInfo) {
        this.activity = activity;
        this.appInfo = appInfo;
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemUserType) {
        return new IVH(ModuleOpsItemOpsCheckableBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
        ivh.binding.appItemRoot.setOnClickListener(v -> showModeSelectionDialog(op, v, sectionIndex, itemIndex));
        ivh.binding.itemState.setOnClickListener(v -> quickSwitch(op, sectionIndex, itemIndex));
        ivh.binding.setApp(appInfo);
        ivh.binding.executePendingBindings();
    }

    private void quickSwitch(Op op, int sectionIndex, int itemIndex) {
        int newMode = AppOpsManager.MODE_ALLOWED;
        int oldMode = op.getMode();
        if (oldMode == AppOpsManager.MODE_ALLOWED) {
            newMode = AppOpsManager.MODE_FOREGROUND;
        }
        if (oldMode == AppOpsManager.MODE_FOREGROUND) {
            newMode = AppOpsManager.MODE_IGNORED;
        }
        if (oldMode == AppOpsManager.MODE_IGNORED) {
            newMode = AppOpsManager.MODE_ALLOWED;
        }
        int finalNewMode = newMode;
        ThanosManager.from(activity)
                .ifServiceInstalled(thanosManager -> thanosManager.getAppOpsManager()
                        .setMode(op.getCode(), appInfo.getUid(), appInfo.getPkgName(), finalNewMode));
        op.setMode(finalNewMode);
        notifySectionItemChanged(sectionIndex, itemIndex);
    }

    private void showModeSelectionDialog(Op op, View view, int sectionIndex, int itemIndex) {
        Resources res = view.getResources();
        String[] items = Lists.newArrayList(
                        res.getString(github.tornaco.android.thanos.res.R.string.module_ops_mode_allow),
                        res.getString(github.tornaco.android.thanos.res.R.string.module_ops_mode_foreground),
                        res.getString(github.tornaco.android.thanos.res.R.string.module_ops_mode_ignore))
                .toArray(new String[0]);
        int currentMode = op.getMode();
        int currentSelection = 0;
        if (currentMode == AppOpsManager.MODE_FOREGROUND) {
            currentSelection = 1;
        }
        if (currentMode == AppOpsManager.MODE_IGNORED) {
            currentSelection = 2;
        }
        new MaterialAlertDialogBuilder(activity)
                .setTitle(op.getTitle())
                .setSingleChoiceItems(items, currentSelection, (dialog, which) -> {
                    dialog.dismiss();
                    int newMode = AppOpsManager.MODE_ALLOWED;
                    if (which == 1) {
                        newMode = AppOpsManager.MODE_FOREGROUND;
                    }
                    if (which == 2) {
                        newMode = AppOpsManager.MODE_IGNORED;
                    }
                    int finalNewMode = newMode;
                    ThanosManager.from(activity)
                            .ifServiceInstalled(thanosManager -> thanosManager.getAppOpsManager()
                                    .setMode(op.getCode(), appInfo.getUid(), appInfo.getPkgName(), finalNewMode));
                    op.setMode(finalNewMode);
                    notifySectionItemChanged(sectionIndex, itemIndex);
                })
                .setCancelable(true)
                .show();
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

        public ModuleOpsItemHeaderBinding getBinding() {
            return this.binding;
        }
    }

    final static class IVH extends ItemViewHolder {
        private ModuleOpsItemOpsCheckableBinding binding;

        IVH(ModuleOpsItemOpsCheckableBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ModuleOpsItemOpsCheckableBinding getBinding() {
            return this.binding;
        }
    }

    final static class FVH extends FooterViewHolder {
        private ModuleOpsItemFooterBinding binding;

        FVH(ModuleOpsItemFooterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ModuleOpsItemFooterBinding getBinding() {
            return this.binding;
        }
    }
}
