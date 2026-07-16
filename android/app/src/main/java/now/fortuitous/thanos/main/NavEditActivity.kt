package now.fortuitous.thanos.main

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxMediumAppBarScaffold

@AndroidEntryPoint
class NavEditActivity : ComposeThemeActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, NavEditActivity::class.java))
        }
    }

    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<NavEditViewModel>()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(viewModel) {
            viewModel.load()
        }

        ThanoxMediumAppBarScaffold(
            title = {
                Text(
                    text = stringResource(id = github.tornaco.android.thanos.res.R.string.nav_edit_title),
                    style = TypographyDefaults.appBarTitleTextStyle()
                )
            },
            onBackPressed = { finish() },
            actions = {
                TextButton(onClick = { viewModel.resetOrder() }) {
                    Text(stringResource(id = github.tornaco.android.thanos.res.R.string.nav_edit_reset))
                }
            }
        ) { paddings ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings)
                    .padding(horizontal = 16.dp)
            ) {
                itemsIndexed(state.sections, key = { _, g -> g.key }) { index, section ->
                    val isExpandable = section.items.isNotEmpty()
                    if (isExpandable) {
                        GroupEditCard(
                            group = section,
                            isFirst = index == 0,
                            isLast = index == state.sections.size - 1,
                            isExpanded = state.expandedGroupKey == section.key,
                            onToggleExpand = { viewModel.toggleExpand(section.key) },
                            onMoveUp = { viewModel.moveSectionUp(section.key) },
                            onMoveDown = { viewModel.moveSectionDown(section.key) },
                            onFeatureMoveUp = { viewModel.moveFeatureUp(section.key, it) },
                            onFeatureMoveDown = { viewModel.moveFeatureDown(section.key, it) },
                        )
                    } else {
                        SectionEditCard(
                            section = section,
                            isFirst = index == 0,
                            isLast = index == state.sections.size - 1,
                            onMoveUp = { viewModel.moveSectionUp(section.key) },
                            onMoveDown = { viewModel.moveSectionDown(section.key) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionEditCard(
    section: FeatureItemGroup,
    isFirst: Boolean,
    isLast: Boolean,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = section.titleRes),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.W600,
                modifier = Modifier.weight(1f)
            )
            Row {
                IconButton(onClick = onMoveUp, enabled = !isFirst) {
                    Icon(
                        painter = painterResource(id = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_arrow_up_s_fill),
                        contentDescription = "Move up",
                        tint = if (!isFirst) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
                IconButton(onClick = onMoveDown, enabled = !isLast) {
                    Icon(
                        painter = painterResource(id = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_arrow_down_s_fill),
                        contentDescription = "Move down",
                        tint = if (!isLast) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
}

@Composable
private fun GroupEditCard(
    group: FeatureItemGroup,
    isFirst: Boolean,
    isLast: Boolean,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onFeatureMoveUp: (Int) -> Unit,
    onFeatureMoveDown: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleExpand() }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isExpanded)
                            github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_arrow_down_s_fill
                        else
                            github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_arrow_right_s_fill
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(id = group.titleRes),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Text(
                    text = "(${group.items.size})",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            Row {
                IconButton(onClick = onMoveUp, enabled = !isFirst) {
                    Icon(
                        painter = painterResource(id = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_arrow_up_s_fill),
                        contentDescription = "Move up",
                        tint = if (!isFirst) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
                IconButton(onClick = onMoveDown, enabled = !isLast) {
                    Icon(
                        painter = painterResource(id = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_arrow_down_s_fill),
                        contentDescription = "Move down",
                        tint = if (!isLast) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
            }
        }

        AnimatedVisibility(visible = isExpanded) {
            Column(modifier = Modifier.padding(start = 16.dp)) {
                HorizontalDivider(modifier = Modifier.padding(end = 16.dp))
                group.items.forEachIndexed { index, item ->
                    FeatureEditRow(
                        item = item,
                        isFirst = index == 0,
                        isLast = index == group.items.size - 1,
                        onMoveUp = { onFeatureMoveUp(item.id) },
                        onMoveDown = { onFeatureMoveDown(item.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun FeatureEditRow(
    item: FeatureItem,
    isFirst: Boolean,
    isLast: Boolean,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = item.titleRes),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        )
        Row {
            IconButton(onClick = onMoveUp, enabled = !isFirst, modifier = Modifier.size(36.dp)) {
                Icon(
                    painter = painterResource(id = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_arrow_up_s_fill),
                    contentDescription = "Move up",
                    modifier = Modifier.size(18.dp),
                    tint = if (!isFirst) MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                )
            }
            IconButton(onClick = onMoveDown, enabled = !isLast, modifier = Modifier.size(36.dp)) {
                Icon(
                    painter = painterResource(id = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_arrow_down_s_fill),
                    contentDescription = "Move down",
                    modifier = Modifier.size(18.dp),
                    tint = if (!isLast) MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                )
            }
        }
    }
}
