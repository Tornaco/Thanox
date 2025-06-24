package github.tornaco.android.thanos.module.compose.common.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ListItem(
    modifier: Modifier,
    title: String,
    icon: (@Composable () -> Unit)? = null,
    action1: @Composable () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(ThanoxCardRoundedCornerShape)
            .clickable {
                onClick()
            },
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = 60.dp)
                .weight(1f, fill = false)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = CenterVertically
        ) {
            icon?.let {
                it()
                Spacer(modifier = Modifier.size(16.dp))
            }
            Column(modifier = Modifier) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
            }
        }

        action1()
    }
}

@Composable
fun CheckableListItem(
    title: String? = null,
    text1: String? = null,
    text2: String? = null,
    overflow: @Composable ColumnScope.() -> Unit = {},
    icon: (@Composable () -> Unit)? = null,
    checked: Boolean,
    onClick: () -> Unit,
    onCheckedChange: ((Boolean) -> Unit),
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ThanoxCardRoundedCornerShape)
            .clickable {
                onClick()
            },
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .weight(1f, fill = false)
                .padding(end = 24.dp)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = CenterVertically
        ) {
            icon?.invoke()
            Spacer(modifier = Modifier.size(16.dp))
            Column(modifier = Modifier) {
                title?.let {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 16.sp,
                        )
                    )
                }
                TinySpacer()
                text1?.let {
                    Text(
                        text = text1,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                        maxLines = 12
                    )
                }
                TinySpacer()
                TinySpacer()
                TinySpacer()
                text2?.let {
                    Text(
                        text = text2,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                        maxLines = 12
                    )
                }
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            overflow()
            Switch(
                modifier = Modifier.padding(end = 16.dp, bottom = 16.dp),
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}


@Composable
fun ListItem(
    icon: (@Composable () -> Unit)? = null,
    title: String? = null,
    text1: String? = null,
    text2: String? = null,
    overflow: @Composable ColumnScope.() -> Unit = {},
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ThanoxCardRoundedCornerShape)
            .clickable {
                onClick()
            },
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .weight(1f, fill = false)
                .padding(end = 24.dp)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = CenterVertically
        ) {
            icon?.invoke()
            Spacer(modifier = Modifier.size(16.dp))
            Column(modifier = Modifier) {
                title?.let {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                        )
                    )
                }
                text1?.let {
                    TinySpacer()
                    Text(
                        text = text1,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                        maxLines = 12
                    )
                }
                TinySpacer()
                TinySpacer()
                TinySpacer()
                text2?.let {
                    Text(
                        text = text2,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        maxLines = 12
                    )
                }
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            overflow()
        }
    }
}

@Composable
@Preview
fun ListItemPreview() {
    LazyColumn {
        items(10) {
            CheckableListItem(title = "Title",
                text1 = "Text1",
                text2 = "Text2",
                checked = true,
                onClick = {},
                onCheckedChange = {}
            )
        }
    }
}

@Composable
@Preview
fun ListItem2Preview() {
    LazyColumn {
        items(10) {
            ListItem(title = "Title",
                text1 = "Text1",
                text2 = "Text2",
                onClick = {}
            )
        }
    }
}