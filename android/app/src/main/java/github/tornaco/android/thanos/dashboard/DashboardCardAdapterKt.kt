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

package github.tornaco.android.thanos.dashboard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.recyclerview.widget.RecyclerView
import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.databinding.ItemFeatureDashboardFooterBinding
import github.tornaco.android.thanos.databinding.ItemFeatureDashboardGroupBinding
import github.tornaco.android.thanos.databinding.ItemFeatureDashboardHeaderComposeBinding
import kotlinx.coroutines.flow.MutableStateFlow

class DashboardCardAdapter(
    private val onTileClickListener: OnTileClickListener,
    private val onTileLongClickListener: OnTileLongClickListener,
    private val onHeaderClickListener: OnHeaderClickListener
) : RecyclerView.Adapter<Holder>() {
    private val groups: MutableList<TileGroup> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun replaceData(tiles: List<TileGroup>?) {
        groups.clear()
        groups.addAll(tiles!!)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) return ViewType.HEADER
        return if (position == itemCount - 1) ViewType.FOOTER else ViewType.ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        if (viewType == ViewType.HEADER) return HeaderHolder(
            ItemFeatureDashboardHeaderComposeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        ) { onHeaderClickListener.onHeaderClick() }
        if (viewType == ViewType.ITEM) return GroupHolder(
            ItemFeatureDashboardGroupBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        if (viewType == ViewType.FOOTER) return FooterHolder(
            ItemFeatureDashboardFooterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        throw IllegalArgumentException("Unknown type: $viewType")
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val group = groups[position]
        when (getItemViewType(position)) {
            ViewType.HEADER -> {
                val headerHolder = holder as HeaderHolder
                headerHolder.bind(group.headerInfo)
                headerHolder.binding.executePendingBindings()
            }
            ViewType.ITEM -> {
                val groupHolder = holder as GroupHolder
                groupHolder.binding.group = group
                groupHolder.binding.tilesContainer.setup(
                    group,
                    onTileClickListener,
                    onTileLongClickListener
                )
                groupHolder.binding.executePendingBindings()
            }
            ViewType.FOOTER -> {
                val footerHolder = holder as FooterHolder
                footerHolder.binding.footerInfo = group.footerInfo
                footerHolder.binding.executePendingBindings()
            }
        }
    }

    override fun getItemCount(): Int {
        return groups.size
    }
}


abstract class Holder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView)

internal class GroupHolder(val binding: ItemFeatureDashboardGroupBinding) :
    Holder(binding.root)

internal class FooterHolder(val binding: ItemFeatureDashboardFooterBinding) :
    Holder(binding.root)

internal object ViewType {
    const val HEADER = 0X1
    const val ITEM = 0x2
    const val FOOTER = 0x3
}

internal class HeaderHolder(
    val binding: ItemFeatureDashboardHeaderComposeBinding,
    private val onHeaderClick: () -> Unit
) :
    Holder(binding.root) {

    private val _state = MutableStateFlow(HeaderState(StatusHeaderInfo(0, MemUsage(), MemUsage())))

    init {
        XLog.d("init: HeaderHolder-${hashCode()}")
        binding.compose.setContent {
            val state by _state.collectAsState()
            HeaderContent(state, onHeaderClick)
        }
    }

    fun bind(headerInfo: StatusHeaderInfo) {
        XLog.d("Bind header: HeaderHolder-${hashCode()} $headerInfo")
        _state.value = _state.value.copy(
            headerInfo = headerInfo
        )
    }
}

data class HeaderState(val headerInfo: StatusHeaderInfo)