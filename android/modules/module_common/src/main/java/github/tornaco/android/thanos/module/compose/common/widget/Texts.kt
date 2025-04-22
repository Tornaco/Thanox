package github.tornaco.android.thanos.module.compose.common.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LargeTitle(text: String) {
    Text(
        modifier = Modifier.padding(vertical = 12.dp),
        text = text,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun MediumTitle(text: String) {
    Text(
        modifier = Modifier.padding(vertical = 12.dp),
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.W500,
            fontSize = 16.sp
        )
    )
}

@Composable
fun SmallTitle(text: String) {
    Text(
        modifier = Modifier.padding(vertical = 12.dp),
        text = text,
        style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.W500,
            fontSize = 14.sp
        )
    )
}

@Composable
fun DialogTitle(text: String) {
    Text(
        modifier = Modifier.padding(8.dp),
        text = text,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun DialogMessage(text: String) {
    Text(
        modifier = Modifier.padding(8.dp),
        text = text,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun TipBody(text: String) {
    Text(
        modifier = Modifier.padding(vertical = 12.dp),
        text = text,
        style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 10.sp
        )
    )
}

@Composable
fun CategoryTitle(title: String) {
    Box(
        modifier = Modifier
            .padding(top = 24.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 12.sp
            )
        )
    }
}


@Composable
fun LinkText(
    text: String,
    color: Color = MaterialTheme.colorScheme.primary,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(ThanoxCardRoundedCornerShape)
            .clickable {
                onClick()
            }
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Text(
            modifier = Modifier,
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(
                textDecoration = TextDecoration.Underline
            ),
            color = color
        )
    }
}
