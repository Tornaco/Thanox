package github.tornaco.android.thanos.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.core.util.function.Predicate;
import github.tornaco.android.thanos.module.common.R;
import github.tornaco.android.thanos.module.common.databinding.ItemCommonAppBinding;
import util.Consumer;

public class CommonAppListFilterAdapter extends RecyclerView.Adapter<CommonAppListFilterAdapter.VH>
    implements Consumer<List<AppListModel>>,
    FastScrollRecyclerView.SectionedAdapter,
    FastScrollRecyclerView.MeasurableAdapter<CommonAppListFilterAdapter.VH> {

  private final List<AppListModel> listModels = new ArrayList<>();

  @Nullable
  private AppItemClickListener itemClickListener;
  @Nullable
  private AppItemViewClickListener itemViewClickListener;

  private StateImageProvider stateImageProvider;

  private boolean iconCheckable = false;

  public CommonAppListFilterAdapter(@Nullable AppItemClickListener itemClickListener) {
    this.itemClickListener = itemClickListener;
  }

  public CommonAppListFilterAdapter(@Nullable AppItemViewClickListener itemViewClickListener) {
    this.itemViewClickListener = itemViewClickListener;
  }

  public CommonAppListFilterAdapter(
      @Nullable AppItemClickListener itemClickListener, boolean iconCheckable) {
    this.itemClickListener = itemClickListener;
    this.iconCheckable = iconCheckable;
  }

  public CommonAppListFilterAdapter(
      @Nullable AppItemClickListener itemClickListener,
      StateImageProvider stateImageProvider,
      boolean iconCheckable) {
    this.itemClickListener = itemClickListener;
    this.stateImageProvider = stateImageProvider;
    this.iconCheckable = iconCheckable;
  }

  public void removeItem(Predicate<AppListModel> predicate) {
    List<AppListModel> modelsToRemove = new ArrayList<>();
    for (AppListModel model : listModels) {
      if (predicate.test(model)) {
        modelsToRemove.add(model);
      }
    }
    listModels.removeAll(modelsToRemove);
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new VH(
        ItemCommonAppBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull VH holder, int position) {
    AppListModel model = listModels.get(position);
    holder.binding.setApp(model.appInfo);
    holder.binding.setIsLastOne(false);
    holder.binding.setListener(
        appInfo -> {
          if (iconCheckable) {
            appInfo.setSelected(!appInfo.isSelected());
            holder.binding.icon.toggle();
          }
          if (itemViewClickListener != null) {
            itemViewClickListener.onAppItemClick(appInfo, holder.itemView);
          } else if (itemClickListener != null) {
            itemClickListener.onAppItemClick(appInfo);
          }
        });

    // Badge
    holder.binding.setBadge1(model.badge);
    holder.binding.setBadge2(model.badge2);
    if (model.badge1BgColor != 0) {
      holder.binding.badge1View.setBackgroundColor(model.badge1BgColor);
    }
    if (model.badge2BgColor != 0) {
      holder.binding.badge2View.setBackgroundColor(model.badge2BgColor);
    }

    holder.binding.setDescription(model.description);
    holder.binding.setShowStateBadge(model.showStateBadge);
    if (iconCheckable) {
      holder.binding.icon.setChecked(model.appInfo.isSelected(), false);
    }

    // Image.
    holder.binding.stateImage.setVisibility(stateImageProvider == null ? View.GONE : View.VISIBLE);
    if (stateImageProvider != null) {
      holder.binding.stateImage.setImageResource(stateImageProvider.provideImageRes(model));
      holder.binding.stateImage.setOnClickListener(stateImageProvider.provideOnClickListener(model, position));
    }

    holder.binding.executePendingBindings();
  }

  @Override
  public int getItemCount() {
    return listModels.size();
  }

  public void updateSingleItem(Predicate<AppListModel> finder) {
    for (int i = 0; i < listModels.size(); i++) {
      if (finder.test(listModels.get(i))) {
        notifyItemChanged(i);
      }
    }
  }

  @Override
  public void accept(List<AppListModel> processModels) {
    this.listModels.clear();
    this.listModels.addAll(processModels);
    notifyDataSetChanged();
  }

  @Override
  public int getViewTypeHeight(RecyclerView recyclerView, @Nullable VH viewHolder, int viewType) {
    return recyclerView.getResources().getDimensionPixelSize(R.dimen.common_list_item_height);
  }

  @NonNull
  @Override
  public String getSectionName(int position) {
    AppListModel model = listModels.get(position);
    String appName = model.appInfo.getAppLabel();
    if (appName == null || appName.length() < 1) {
      appName = model.appInfo.getPkgName();
    }
    if (appName == null) {
      return "*";
    }
    return String.valueOf(appName.charAt(0));
  }

    public List<AppListModel> getListModels() {
        return this.listModels;
    }

    public void setIconCheckable(boolean iconCheckable) {
        this.iconCheckable = iconCheckable;
    }

    static final class VH extends RecyclerView.ViewHolder {

    private ItemCommonAppBinding binding;

    VH(@NonNull ItemCommonAppBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

        public github.tornaco.android.thanos.module.common.databinding.ItemCommonAppBinding getBinding() {
            return this.binding;
        }
    }
}
