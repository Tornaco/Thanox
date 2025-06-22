package github.tornaco.android.thanos.module.compose.common.widget

import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

// https://github.com/jeziellago/compose-markdown
@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
    onClick: (() -> Unit)? = null,
    onLinkClicked: ((String) -> Unit)? = null,
) {
    dev.jeziellago.compose.markdowntext.MarkdownText(
        modifier = modifier,
        markdown = markdown,
        maxLines = maxLines,
        style = style,
        onClick = onClick,
        onLinkClicked = onLinkClicked
    )
}