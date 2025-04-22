package github.tornaco.thanos.module.component.manager.redesign;

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import github.tornaco.android.thanos.module.compose.common.widget.LinkText
import github.tornaco.android.thanos.module.compose.common.widget.MediumSpacer
import github.tornaco.android.thanos.module.compose.common.widget.StandardSpacer
import github.tornaco.android.thanos.module.compose.common.widget.TinySpacer
import github.tornaco.android.thanos.res.R
import github.tornaco.android.thanos.util.BrowserUtils
import github.tornaco.thanos.module.component.manager.redesign.rule.BlockerRule

@Composable
fun BlockerRuleInfoDialog(rule: BlockerRule, dismiss: () -> Unit) {
    Surface(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.large,
        tonalElevation = AlertDialogDefaults.TonalElevation
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp)
                .padding(16.dp)
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                rule.name.orEmpty(),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W500)
            )
            TinySpacer()
            Text(
                "@blocker-general-rules indicator",
                style = MaterialTheme.typography.bodySmall
            )
            StandardSpacer()

            Text(
                rule.description.orEmpty(),
                style = MaterialTheme.typography.bodySmall
            )
            StandardSpacer()

            if (rule.safeToBlock) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.module_component_manager_safe_to_block))
                    TinySpacer()
                    Icon(
                        modifier = Modifier,
                        painter = painterResource(github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_shield_check_fill),
                        contentDescription = null,
                        tint = Color(0xFF32CD32)
                    )
                }
                MediumSpacer()
                val context = LocalContext.current
                LinkText(
                    stringResource(R.string.module_component_manager_safe_to_block_tip),
                    icon = {
                        Icon(
                            modifier = Modifier,
                            painter = painterResource(github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_external_link_fill),
                            contentDescription = null,
                        )
                    },
                    onClick = {
                        BrowserUtils.launch(
                            context,
                            "https://github.com/lihenggui/blocker-general-rules"
                        )
                    }
                )
            } else if (rule.sideEffect.orEmpty().isNotEmpty()) {
                Text("Not Safe to block: ${rule.sideEffect}")
            }
        }
    }
}