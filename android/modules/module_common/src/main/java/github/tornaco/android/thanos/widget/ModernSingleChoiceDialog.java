package github.tornaco.android.thanos.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import github.tornaco.android.thanos.module.common.R;
import github.tornaco.android.thanos.module.common.databinding.CommonSingleChoiceItemBinding;
import util.Consumer;

public class ModernSingleChoiceDialog {
    private final Context context;
    private String title;
    private String tips;
    private List<Item> items;
    private Dialog dialog;
    private String checkedId;

    private Consumer<String> onConfirm;

    public ModernSingleChoiceDialog(Context context) {
        this.context = context;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public void setTitle(@StringRes int titleRes) {
        this.title = context.getString(titleRes);
    }

    public void setCheckedId(String checkedId) {
        this.checkedId = checkedId;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setOnConfirm(Consumer<String> onConfirm) {
        this.onConfirm = onConfirm;
    }

    public void show() {
        View rootLayout = LayoutInflater.from(context)
                .inflate(R.layout.common_single_choice_dialog, null, false);

        TextView tipView = rootLayout.findViewById(R.id.tip_text);
        tipView.setText(tips);
        tipView.setVisibility(TextUtils.isEmpty(tips) ? View.GONE : View.VISIBLE);

        RecyclerView recyclerView = rootLayout.findViewById(R.id.recycler_view);
        ItemAdapter adapter = new ItemAdapter(items);
        adapter.setCheckedId(checkedId);
        recyclerView.setAdapter(adapter);

        this.dialog = new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setView(rootLayout)
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    // Noop.
                }).setPositiveButton(android.R.string.ok, (dialog, which) -> onConfirm.accept(adapter.getCheckedId()))
                .show();
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }

    private static class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {
        private final List<Item> items;
        private String checkedId;

        public ItemAdapter(List<Item> items) {
            this.items = items;
        }

        public String getCheckedId() {
            return checkedId;
        }

        @SuppressLint("NotifyDataSetChanged")
        public void setCheckedId(String checkedId) {
            this.checkedId = checkedId;
            for (int i = 0; i < items.size(); i++) {
                notifyItemChanged(i);
            }
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(CommonSingleChoiceItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            CommonSingleChoiceItemBinding binding = holder.binding;
            Item item = items.get(position);
            binding.setTitle(item.title);
            binding.setDescription(item.description);
            binding.setIsSelected(item.id.equals(checkedId));
            binding.itemRoot.setOnClickListener(v -> setCheckedId(item.id));
            binding.icon.setOnClickListener(v -> setCheckedId(item.id));
            binding.executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        CommonSingleChoiceItemBinding binding;

        public ItemViewHolder(@NonNull CommonSingleChoiceItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class Item {
        private final String id;
        private final String title;
        private final String description;

        public Item(String id, String title, String description) {
            this.id = id;
            this.title = title;
            this.description = description;
        }
    }
}
