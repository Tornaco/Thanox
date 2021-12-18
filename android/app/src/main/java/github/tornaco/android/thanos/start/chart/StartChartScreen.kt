package github.tornaco.android.thanos.start.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.TopAppBar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.widget.pie.CenterText
import github.tornaco.android.thanos.widget.pie.ChartItem
import github.tornaco.android.thanos.widget.pie.Legend
import github.tornaco.android.thanos.widget.pie.PieChart

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
    Scaffold(topBar = {
        AppBar(onBackPressed = onBackPressed, onClear = { viewModel.clearRecordsForCurrentCategory() })
    }) {
        StartChartContent(startChartState = startChartState.value,
            onItemSelected = onItemSelected,
            onCategorySelected = { viewModel.selectCategory(it) },
            onCenterSelected = onCenterSelected)
    }
}

@Composable
private fun AppBar(onBackPressed: () -> Unit, onClear: () -> Unit) {
    TopAppBar(title = {
        Text(stringResource(id = R.string.menu_title_start_restrict_charts),
            style = MaterialTheme.typography.titleMedium)
    },
        backgroundColor = MaterialTheme.colorScheme.surface,
        elevation = 0.dp,
        navigationIcon = {
            IconButton(onClick = {
                onBackPressed()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.module_locker_ic_arrow_back_24dp),
                    contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = {
                onClear()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.module_common_ic_outline_delete_24),
                    contentDescription = "Clear")
            }
        })
}

@Composable
fun StartChartContent(
    startChartState: StartChartState,
    onItemSelected: ((ChartItem<String>) -> Unit),
    onCategorySelected: (Category) -> Unit,
    onCenterSelected: (() -> Unit),
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

@Composable
private fun StartChart(
    state: StartChartState,
    onItemSelected: ((ChartItem<String>) -> Unit),
    onCenterSelected: (() -> Unit),
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 64.dp)
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        PieChart(modifier = Modifier
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
            onCenterSelected = onCenterSelected)

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
    Box(modifier = Modifier
        .padding(16.dp)
        .wrapContentSize(Alignment.TopStart)) {
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