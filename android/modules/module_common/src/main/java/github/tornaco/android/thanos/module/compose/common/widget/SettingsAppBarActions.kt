package github.tornaco.android.thanos.module.compose.common.widget


import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun SettingsAppBarActions(localDrawerState: DrawerState, hasUnReadMsg: Boolean) {
    val scope = rememberCoroutineScope()
    IconButton(onClick = {
        scope.launch { localDrawerState.open() }
    }) {
        BadgedBox(badge = {
            if (hasUnReadMsg) {
                Badge(containerColor = Color.Red)
            }
        }) {
            val iconSize by animateDpAsState(
                targetValue = if (hasUnReadMsg) 18.dp else 24.dp,
                animationSpec = tween(durationMillis = 300), label = "Icon size"
            )
            Icon(
                imageVector = Icons.Outlined.Menu,
                modifier = Modifier.size(iconSize),
                contentDescription = null
            )
        }
    }
}