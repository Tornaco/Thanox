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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.databinding.StandbyRuleListItemBinding;
import util.Consumer;

class StandbyRuleListAdapter extends RecyclerView.Adapter<StandbyRuleListAdapter.VH>
        implements Consumer<List<StandbyRule>> {

    private final List<StandbyRule> standbyRules = new ArrayList<>();

    private final StandbyRuleItemClickListener ruleItemClickListener;

    StandbyRuleListAdapter(StandbyRuleItemClickListener ruleItemClickListener) {
        this.ruleItemClickListener = ruleItemClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(StandbyRuleListItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        StandbyRule model = standbyRules.get(position);
        holder.itemBinding.setRule(model);
        holder.itemBinding.setRuleItemClickListener(ruleItemClickListener);
        holder.itemBinding.setIsLastOne(position == getItemCount() - 1);
        holder.itemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return this.standbyRules.size();
    }

    @Override
    public void accept(List<StandbyRule> ruleInfoList) {
        this.standbyRules.clear();
        this.standbyRules.addAll(ruleInfoList);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        private final StandbyRuleListItemBinding itemBinding;

        VH(@NonNull StandbyRuleListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public github.tornaco.android.thanos.databinding.StandbyRuleListItemBinding getItemBinding() {
            return this.itemBinding;
        }
    }
}
