package github.tornaco.android.thanos.dashboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import github.tornaco.android.thanos.databinding.ItemFeatureDashboardTileOfCardBinding;
import util.CollectionUtils;

public class TilesContainerView extends RecyclerView {

    public TilesContainerView(@NonNull Context context) {
        super(context);
    }

    public TilesContainerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TilesContainerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    void setup(TileGroup tileGroup,
               OnTileClickListener onTileClickListener,
               OnTileLongClickListener onTileLongClickListener) {
        Adapter adapter = new Adapter(tileGroup, onTileClickListener, onTileLongClickListener);
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        setAdapter(adapter);
    }

    class Adapter extends RecyclerView.Adapter<VH> {
        private TileGroup tileGroup;
        private OnTileClickListener onTileClickListener;
        private OnTileLongClickListener onTileLongClickListener;

        public Adapter(TileGroup tileGroup, OnTileClickListener onTileClickListener, OnTileLongClickListener onTileLongClickListener) {
            this.tileGroup = tileGroup;
            this.onTileClickListener = onTileClickListener;
            this.onTileLongClickListener = onTileLongClickListener;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(ItemFeatureDashboardTileOfCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            Tile tile = tileGroup.getTiles().get(position);
            holder.binding.setTile(tile);
            holder.binding.tileRoot.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTileClickListener.onClick(tile);
                }
            });
            holder.binding.tileRoot.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onTileLongClickListener.onLongClick(tile, v);
                    return true;
                }
            });
            holder.binding.executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return CollectionUtils.sizeOf(tileGroup.getTiles());
        }
    }

    class VH extends RecyclerView.ViewHolder {
        private ItemFeatureDashboardTileOfCardBinding binding;

        VH(@NonNull ItemFeatureDashboardTileOfCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
