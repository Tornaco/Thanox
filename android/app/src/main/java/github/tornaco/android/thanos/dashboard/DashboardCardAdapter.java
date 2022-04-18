package github.tornaco.android.thanos.dashboard;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.databinding.ItemFeatureDashboardFooterBinding;
import github.tornaco.android.thanos.databinding.ItemFeatureDashboardGroupBinding;
import github.tornaco.android.thanos.databinding.ItemFeatureDashboardHeaderBinding;

@SuppressLint("NotifyDataSetChanged")
public class DashboardCardAdapter extends RecyclerView.Adapter<DashboardCardAdapter.Holder> {

    private final List<TileGroup> groups = new ArrayList<>();

    private final OnTileClickListener onTileClickListener;
    private final OnTileLongClickListener onTileLongClickListener;
    private final OnHeaderClickListener onHeaderClickListener;

    public DashboardCardAdapter(OnTileClickListener onTileClickListener,
                                OnTileLongClickListener onTileLongClickListener,
                                OnHeaderClickListener onHeaderClickListener) {
        this.onTileClickListener = onTileClickListener;
        this.onTileLongClickListener = onTileLongClickListener;
        this.onHeaderClickListener = onHeaderClickListener;
    }

    public void replaceData(List<TileGroup> tiles) {
        this.groups.clear();
        this.groups.addAll(tiles);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return ViewType.HEADER;
        if (position == getItemCount() - 1) return ViewType.FOOTER;
        return ViewType.ITEM;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ViewType.HEADER)
            return new HeaderHolder(ItemFeatureDashboardHeaderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        if (viewType == ViewType.ITEM)
            return new GroupHolder(ItemFeatureDashboardGroupBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        if (viewType == ViewType.FOOTER)
            return new FooterHolder(ItemFeatureDashboardFooterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        throw new IllegalArgumentException("Unknown type: " + viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        TileGroup group = groups.get(position);
        int viewType = getItemViewType(position);
        switch (viewType) {
            case ViewType.HEADER:
                HeaderHolder headerHolder = (HeaderHolder) holder;
                headerHolder.binding.setHeaderInfo(group.getHeaderInfo());
                headerHolder.binding.statusCard.setOnClickListener(v -> onHeaderClickListener.onClick());
                headerHolder.binding.executePendingBindings();
                break;
            case ViewType.ITEM:
                GroupHolder groupHolder = (GroupHolder) holder;
                groupHolder.binding.setGroup(group);
                groupHolder.binding.tilesContainer.setup(group, onTileClickListener, onTileLongClickListener);
                groupHolder.binding.executePendingBindings();
                break;
            case ViewType.FOOTER:
                FooterHolder footerHolder = (FooterHolder) holder;
                footerHolder.binding.setFooterInfo(group.getFooterInfo());
                footerHolder.binding.executePendingBindings();
                break;
        }

    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    static abstract class Holder extends RecyclerView.ViewHolder {

        Holder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static final class GroupHolder extends Holder {
        private final ItemFeatureDashboardGroupBinding binding;

        GroupHolder(@NonNull ItemFeatureDashboardGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static final class HeaderHolder extends Holder {
        private final ItemFeatureDashboardHeaderBinding binding;

        HeaderHolder(@NonNull ItemFeatureDashboardHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static final class FooterHolder extends Holder {
        private final ItemFeatureDashboardFooterBinding binding;


        FooterHolder(@NonNull ItemFeatureDashboardFooterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class ViewType {
        static final int HEADER = 0X1;
        static final int ITEM = 0x2;
        static final int FOOTER = 0x3;
    }
}
