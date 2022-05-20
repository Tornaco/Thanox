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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.RecyclerView
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.databinding.ItemFeatureDashboardFooterBinding
import github.tornaco.android.thanos.databinding.ItemFeatureDashboardGroupBinding
import github.tornaco.android.thanos.databinding.ItemFeatureDashboardHeaderComposeBinding
import github.tornaco.android.thanos.module.compose.common.*
import github.tornaco.android.thanos.module.compose.common.widget.CircularProgressBar


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
        )
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
                headerHolder.bind(group.headerInfo) {
                    onHeaderClickListener.onHeaderClick()
                }
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

internal class HeaderHolder(val binding: ItemFeatureDashboardHeaderComposeBinding) :
    Holder(binding.root) {

    fun bind(headerInfo: StatusHeaderInfo, onHeaderClick: () -> Unit) {
        binding.compose.setContent {
            HeaderContent(headerInfo, onHeaderClick)
        }
    }
}

@Composable
private fun HeaderContent(headerInfo: StatusHeaderInfo, onHeaderClick: () -> Unit) {
    val cardBgColor = getColorAttribute(R.attr.appCardBackground)
    val primaryColor = getColorAttribute(R.attr.colorPrimary)
    val primaryContainerColor = getColorAttribute(R.attr.colorPrimaryContainer)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color = Color(cardBgColor))
            .clickableWithRipple {
                onHeaderClick()
            }
            .padding(16.dp)

    ) {
        Column {
            Row {
                FilledTonalButton(onClick = {
                    onHeaderClick()
                }) {
                    Text(
                        text = "${headerInfo.runningAppsCount}${stringResource(id = R.string.boost_status_running_apps)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            StandardSpacer()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 72.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circle
                CircularProgressBar(
                    modifier = Modifier
                        .size(88.dp)
                        .align(Alignment.CenterVertically),
                    progress = headerInfo.memUsagePercent.toFloat(),
                    progressMax = 100f,
                    progressBarColor = Color(primaryColor),
                    progressBarWidth = 16.dp,
                    backgroundProgressBarColor = MaterialTheme.colorScheme.surfaceVariant,
                    backgroundProgressBarWidth = 16.dp,
                    roundBorder = true,
                    startAngle = 90f
                )

                StandardSpacer()

                // Linear.
                Column {
                    Text(
                        text = stringResource(
                            id = R.string.boost_status_mem_usage_percent,
                            "7GB/12GB"
                        ),
                        style = MaterialTheme.typography.titleSmall
                    )
                    SmallSpacer()
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        color = Color(primaryColor),
                        progress = 0.24f
                    )

                    LargeSpacer()

                    Text(
                        text = stringResource(
                            id = R.string.boost_status_swap_usage_percent,
                            "3GB/8GB"
                        ),
                        style = MaterialTheme.typography.titleSmall
                    )
                    SmallSpacer()
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        color = Color(primaryColor),
                        progress = 0.72f
                    )
                }
            }
        }
    }
}