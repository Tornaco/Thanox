/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package now.fortuitous.thanos.power;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.common.AppItemClickListener;
import github.tornaco.android.thanos.common.AppItemViewLongClickListener;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.databinding.ItemSmartFreezeAppBinding;
import util.Consumer;

public class SmartFreezeAppsAdapter extends RecyclerView.Adapter<SmartFreezeAppsAdapter.VH>
        implements Consumer<List<AppListModel>> {

    private final List<AppListModel> appListModels = new ArrayList<>();

    @Nullable
    private final AppItemClickListener itemViewClickListener;
    @Nullable
    private final AppItemViewLongClickListener itemViewLongClickListener;

    public SmartFreezeAppsAdapter(@Nullable AppItemClickListener itemViewClickListener,
                                  @Nullable AppItemViewLongClickListener itemViewLongClickListener) {
        this.itemViewClickListener = itemViewClickListener;
        this.itemViewLongClickListener = itemViewLongClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemSmartFreezeAppBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        AppListModel model = appListModels.get(position);
        holder.binding.setApp(model.appInfo);
        holder.binding.setListener(new AppItemClickListener() {
            @Override
            public void onAppItemClick(AppInfo appInfo) {
                if (itemViewClickListener != null) {
                    itemViewClickListener.onAppItemClick(appInfo);
                    notifyItemChanged(holder.getLayoutPosition());
                }
            }
        });
        holder.binding.appItemRoot.setOnLongClickListener(v -> {
            if (itemViewLongClickListener != null) {
                itemViewLongClickListener.onAppItemLongClick(holder.binding.appItemRoot, model);
            }
            return true;
        });
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return appListModels.size();
    }

    @Override
    public void accept(List<AppListModel> processModels) {
        this.appListModels.clear();
        this.appListModels.addAll(processModels);
        notifyDataSetChanged();
    }

    static final class VH extends RecyclerView.ViewHolder {
        private final ItemSmartFreezeAppBinding binding;

        VH(@NonNull ItemSmartFreezeAppBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public github.tornaco.android.thanos.databinding.ItemSmartFreezeAppBinding getBinding() {
            return this.binding;
        }
    }
}
