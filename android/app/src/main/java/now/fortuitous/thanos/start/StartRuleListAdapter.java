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

package now.fortuitous.thanos.start;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.databinding.StartRuleListItemBinding;
import util.Consumer;

class StartRuleListAdapter extends RecyclerView.Adapter<StartRuleListAdapter.VH>
        implements Consumer<List<StartRule>> {

    private final List<StartRule> startRules = new ArrayList<>();

    private final StartRuleItemClickListener startRuleItemClickListener;

    StartRuleListAdapter(StartRuleItemClickListener startRuleItemClickListener) {
        this.startRuleItemClickListener = startRuleItemClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(StartRuleListItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        StartRule model = startRules.get(position);
        holder.itemBinding.setRule(model);
        holder.itemBinding.setRuleItemClickListener(startRuleItemClickListener);
        holder.itemBinding.setIsLastOne(position == getItemCount() - 1);
        holder.itemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return this.startRules.size();
    }

    @Override
    public void accept(List<StartRule> ruleInfoList) {
        this.startRules.clear();
        this.startRules.addAll(ruleInfoList);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        private final StartRuleListItemBinding itemBinding;

        VH(@NonNull StartRuleListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public github.tornaco.android.thanos.databinding.StartRuleListItemBinding getItemBinding() {
            return this.itemBinding;
        }
    }
}
