package github.tornaco.android.thanos.dashboard;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.databinding.ItemFeatureDashboardBinding;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.TileHolder> {
    private final List<Tile> tiles = new ArrayList<>();

    // Global.
    @NonNull
    private OnTileClickListener onClickListener;
    @NonNull
    private OnTileLongClickListener onLongClickListener;
    @Nullable
    private OnTileSwitchChangeListener onSwitchChangeListener;

    public DashboardAdapter(@NonNull OnTileClickListener onClickListener,
                            @NonNull OnTileLongClickListener onLongClickListener,
                            @Nullable OnTileSwitchChangeListener onSwitchChangeListener) {
        this.onClickListener = onClickListener;
        this.onLongClickListener = onLongClickListener;
        this.onSwitchChangeListener = onSwitchChangeListener;
    }

    public void replaceData(List<Tile> tiles) {
        this.tiles.clear();
        this.tiles.addAll(tiles);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TileHolder(ItemFeatureDashboardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TileHolder holder, int position) {
        Tile tile = tiles.get(position);
        holder.binding.setTile(tile);
        holder.binding.setIsLastOne(position == getItemCount() - 1);
        holder.binding.tileRoot.setOnLongClickListener(v -> {
            onLongClickListener.onLongClick(tile, v);
            return true;
        });
        holder.binding.tileRoot.setOnClickListener(v -> onClickListener.onClick(tile));
        holder.binding.setCheckable(tile.isCheckable());
        if (onSwitchChangeListener != null) {
            holder.binding.tileSwitch.setOnClickListener(v -> {
                boolean checked = holder.binding.tileSwitch.isChecked();
                tile.setChecked(checked);
                onSwitchChangeListener.onSwitchChange(tile, checked);
            });
        }
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return tiles.size();
    }

    static final class TileHolder extends RecyclerView.ViewHolder {
        private ItemFeatureDashboardBinding binding;

        TileHolder(@NonNull ItemFeatureDashboardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnTileSwitchChangeListener {
        void onSwitchChange(@NonNull Tile tile, boolean checked);
    }
}
