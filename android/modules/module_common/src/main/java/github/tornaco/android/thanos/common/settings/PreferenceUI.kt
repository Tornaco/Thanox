package github.tornaco.android.thanos.common.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import github.tornaco.android.thanos.module.compose.common.theme.ColorDefaults
import github.tornaco.android.thanos.module.compose.common.widget.CategoryTitle
import github.tornaco.android.thanos.module.compose.common.widget.MediumSpacer
import github.tornaco.android.thanos.module.compose.common.widget.OutlineBadge
import github.tornaco.android.thanos.module.compose.common.widget.SmallSpacer
import github.tornaco.android.thanos.module.compose.common.widget.StandardSpacer
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxCardRoundedCornerShape
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PreferenceUi(preferences: List<Preference>) {
    splitListByCategory(preferences).forEach { group ->
        Column {
            group.forEach {
                PreferenceUi(preference = it)
                MediumSpacer()
            }
        }
    }
}

private fun splitListByCategory(list: List<Preference>): List<List<Preference>> {
    val result = mutableListOf<MutableList<Preference>>()
    var currentList = mutableListOf<Preference>()

    list.forEach { item ->
        if (item is Preference.Category) {
            if (currentList.isNotEmpty()) {
                result.add(currentList)
            }
            currentList = mutableListOf()  // 每次遇到A开始一个新的子List
        }
        currentList.add(item)
    }

    // 添加最后一组
    if (currentList.isNotEmpty()) {
        result.add(currentList)
    }
    return result
}


@Composable
private fun PreferenceUi(preference: Preference) {
    when (preference) {
        is Preference.SwitchPreference -> {
            SwitchPreferenceUi(preference = preference)
        }

        is Preference.TextPreference -> {
            TextPreferenceUi(preference = preference)
        }

        is Preference.Divider -> {
            Divider()
        }

        is Preference.Category -> {
            StandardSpacer()
            CategoryUi(preference = preference)
        }

        is Preference.ExpandablePreference -> {
            ExpandablePreferenceUi(preference = preference)
        }
    }
}

@Composable
private fun CategoryUi(preference: Preference.Category) {
    CategoryTitle(title = preference.title)
}

@Composable
private fun ExpandablePreferenceUi(preference: Preference.ExpandablePreference) {
    ExpandableContainer(
        mainContent = { exp ->
            TextPreferenceUi(
                preference = Preference.TextPreference(
                    title = preference.title,
                    summary = preference.summary,
                    icon = preference.icon,
                    onClick = preference.onClick ?: {
                        exp()
                    }
                )
            )
        }, expandContent = {
            Column {
                preference.preferences.forEach {
                    PreferenceUi(it)
                }
            }
        })
}

@Composable
private fun TextPreferenceUi(preference: Preference.TextPreference) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .padding(horizontal = if (preference.withCardBg) 16.dp else 0.dp)
            .fillMaxWidth()
            .clip(ThanoxCardRoundedCornerShape)
            .then(if (preference.withCardBg) Modifier.background(ColorDefaults.backgroundSurfaceColor()) else Modifier)
            .clickable {
                scope.launch {
                    delay(128)
                    preference.onClick?.invoke()
                }
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = 60.dp)
                .weight(1f, fill = false)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PreferenceIcon(preference)
            StandardSpacer()
            Column(modifier = Modifier) {
                preference.title.takeIf { it.isNotBlank() }?.let {
                    Text(
                        modifier = Modifier.padding(end = 36.dp),
                        text = preference.title, style = MaterialTheme.typography.titleMedium
                    )
                }
                preference.summary?.let {
                    SmallSpacer()
                    Text(
                        modifier = Modifier.padding(end = 36.dp),
                        text = it,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        preference.badge?.let {
            OutlineBadge(modifier = Modifier.padding(end = 16.dp), text = it)
        }
    }
}

@Composable
private fun PreferenceIcon(preference: Preference) {
    preference.icon?.let {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(it),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null
        )
    } ?: Spacer(modifier = Modifier.size(24.dp))
}


@Composable
private fun SwitchPreferenceUi(preference: Preference.SwitchPreference) {

    var isShowMoreSummary by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ThanoxCardRoundedCornerShape)
            .clickable {
                if (preference.onClick != null) {
                    preference.onClick.invoke()
                } else {
                    if (preference.hasLongSummary) {
                        isShowMoreSummary = !isShowMoreSummary
                    } else {
                        preference.onCheckChanged(!preference.value)
                    }
                }
            }, verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = 60.dp)
                .weight(1f, fill = false)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PreferenceIcon(preference)
            StandardSpacer()
            Column(modifier = Modifier) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Text(
                        text = preference.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    StandardSpacer()
                    preference.badge?.let {
                        OutlineBadge(text = it)
                    }
                }
                SmallSpacer()

                val showMoreLines = !preference.hasLongSummary || isShowMoreSummary

                AnimatedVisibility(visible = showMoreLines) {
                    Text(
                        modifier = Modifier.padding(end = 36.dp),
                        text = preference.summary ?: preference.title,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 20
                    )
                }
                AnimatedVisibility(visible = !showMoreLines) {
                    Text(
                        modifier = Modifier.padding(end = 36.dp),
                        text = preference.summary ?: preference.title,
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 4
                    )
                }
            }
        }

        Switch(
            modifier = Modifier.padding(end = 16.dp),
            checked = preference.value,
            onCheckedChange = {
                preference.onCheckChanged(it)
            })
    }
}


private enum class ExpandableState { Expand, Collapsed }

@Composable
private fun ExpandableContainer(
    mainContent: @Composable (expand: () -> Unit) -> Unit,
    expandContent: @Composable () -> Unit,
) {
    var expandState by remember {
        mutableStateOf(ExpandableState.Collapsed)
    }
    Column(
        modifier = Modifier
            .clip(ThanoxCardRoundedCornerShape),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f, fill = false)) {
                mainContent(
                    {
                        expandState =
                            if (expandState == ExpandableState.Expand) {
                                ExpandableState.Collapsed
                            } else {
                                ExpandableState.Expand
                            }
                    }
                )
            }

            IconButton(
                modifier = Modifier
                    .padding(end = 16.dp),
                onClick = {
                    expandState =
                        if (expandState == ExpandableState.Expand) {
                            ExpandableState.Collapsed
                        } else {
                            ExpandableState.Expand
                        }
                }) {
                val rotation: Float by
                animateFloatAsState(
                    targetValue = if (expandState == ExpandableState.Expand) 180f else 0f,
                    label = "Trailing Icon Rotation"
                )
                Icon(
                    Icons.Filled.KeyboardArrowDown,
                    modifier =
                        Modifier
                            .size(24.dp)
                            .graphicsLayer {
                                this.rotationZ = rotation
                            },
                    contentDescription = ""
                )
            }

        }

        AnimatedVisibility(visible = expandState == ExpandableState.Expand) {
            expandContent()
        }
    }

}