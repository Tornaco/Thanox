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

package now.fortuitous.thanos.start.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxSmallAppBarScaffold
import github.tornaco.android.thanos.module.compose.common.widget.pie.CenterText
import github.tornaco.android.thanos.module.compose.common.widget.pie.ChartItem
import github.tornaco.android.thanos.module.compose.common.widget.pie.Legend
import github.tornaco.android.thanos.module.compose.common.widget.pie.PieChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartChartScreen(
    onBackPressed: () -> Unit,
    onItemSelected: ((ChartItem<String>) -> Unit),
    onCenterSelected: (() -> Unit),
) {
    val viewModel = hiltViewModel<StartChartViewModel>()
    val startChartState = viewModel.flow.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.startLoading()
    }
    ThanoxSmallAppBarScaffold(
        title = {
            Text(
                text = stringResource(id = R.string.menu_title_start_restrict_charts),
                style = TypographyDefaults.appBarTitleTextStyle()
            )
        },
        onBackPressed = onBackPressed,
        actions = {
            IconButton(onClick = {
                viewModel.clearRecordsForCurrentCategory()
            }) {
                Icon(
                    painter = painterResource(id = github.tornaco.android.thanos.module.common.R.drawable.module_common_ic_outline_delete_24),
                    contentDescription = "Clear"
                )
            }
        }
    ) { paddings ->
        StartChartContent(
            paddings = paddings,
            startChartState = startChartState.value,
            onItemSelected = onItemSelected,
            onCategorySelected = { viewModel.selectCategory(it) },
            onCenterSelected = onCenterSelected
        )
    }
}

@Composable
fun StartChartContent(
    paddings: PaddingValues,
    startChartState: StartChartState,
    onItemSelected: (ChartItem<String>) -> Unit,
    onCategorySelected: (Category) -> Unit,
    onCenterSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddings)
    ) {
        if (startChartState.isLoading) {
            LinearProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
            )
        } else {
            FilterDropDown(startChartState.category, onCategorySelected)
            StartChart(startChartState, onItemSelected, onCenterSelected)
        }
    }
}

@Composable
private fun StartChart(
    state: StartChartState,
    onItemSelected: ((ChartItem<String>) -> Unit),
    onCenterSelected: (() -> Unit),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PieChart(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1f),
            strokeSize = 38.dp,
            chartItems = state.entries,
            centerText = CenterText(
                text = state.totalTimes.toString(),
                color = MaterialTheme.colorScheme.onSurface,
                size = 24.dp
            ),
            onItemSelected = onItemSelected,
            onCenterSelected = onCenterSelected
        )

        Spacer(modifier = Modifier.size(32.dp))

        Legend(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(32.dp),
            chartItems = state.entries,
            columnCount = 1,
        )
    }
}

@Composable
fun FilterDropDown(
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentSize(Alignment.TopStart)
    ) {
        FilledTonalButton(onClick = { expanded = true }) {
            Text(text = stringResource(id = selectedCategory.labelRes))
        }
        DropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Category.values().forEach { category ->
                DropdownMenuItem(onClick = {
                    onCategorySelected(category)
                    expanded = false
                }) {
                    val itemLabel = stringResource(id = category.labelRes)
                    Text(text = itemLabel)
                }
            }
        }
    }
}