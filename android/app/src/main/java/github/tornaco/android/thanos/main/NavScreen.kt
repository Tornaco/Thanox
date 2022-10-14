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

@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)

package github.tornaco.android.thanos.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.module.compose.common.requireActivity
import github.tornaco.android.thanos.module.compose.common.theme.ColorDefaults
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.*

@Composable
fun NavScreen() {
    val viewModel =
        hiltViewModel<NavViewModel2>(LocalContext.current.requireActivity())
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    viewModel.bindLifecycle(lifecycle)
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.loadFeatures()
    }

    ThanoxSmallAppBarScaffold(
        title = {
            Text(
                stringResource(id = R.string.app_name),
                style = TypographyDefaults.appBarTitleTextStyle()
            )
        },
        actions = {
            Row(
                modifier = Modifier,
                verticalAlignment = CenterVertically
            ) {

            }
        },
        onBackPressed = null
    ) { contentPadding ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(state.isLoading),
            onRefresh = { viewModel.refresh() },
            indicatorPadding = contentPadding,
            clipIndicatorToPadding = false,
            indicator = { state, refreshTriggerDistance ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = refreshTriggerDistance,
                    scale = true,
                    arrowEnabled = false,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            }
        ) {

            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Features(state)
            }
        }
    }
}

@Composable
private fun Features(state: NavState) {
    state.features.forEach { group ->
        FeatureGroup(group)
    }
}

@Composable
private fun FeatureGroup(group: FeatureItemGroup) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = ColorDefaults.backgroundSurfaceColor()
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(id = group.titleRes))
            StandardSpacer()

            FlowLayout(
                lineSpacing = 16.dp
            ) {
                group.items.forEach { item ->
                    FeatureItem(item)
                }
            }
        }
    }
}

@Composable
private fun FeatureItem(item: FeatureItem) {
    Column(
        modifier = Modifier
            .width(64.dp)
            .clickableWithRippleBorderless {

            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        AsyncImage(
            model = item.iconRes,
            contentDescription = null
        )
        MediumSpacer()
        AutoResizeText(
            modifier = Modifier,
            text = stringResource(id = item.titleRes),
            textAlign = TextAlign.Center,
            maxLines = 3,
            fontSizeRange = FontSizeRange(
                min = 11.sp,
                max = 12.sp,
            ),
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}