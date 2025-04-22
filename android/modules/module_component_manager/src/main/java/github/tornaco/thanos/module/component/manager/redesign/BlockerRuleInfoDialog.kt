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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import github.tornaco.android.thanos.module.compose.common.widget.LargeSpacer
import github.tornaco.android.thanos.module.compose.common.widget.SmallSpacer
import github.tornaco.android.thanos.module.compose.common.widget.StandardSpacer
import github.tornaco.thanos.module.component.manager.redesign.rule.BlockerRule

@Composable
internal fun BlockerRuleInfoDialog(rule: BlockerRule, dismiss: () -> Unit) {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "@blocker-general-rules indicator",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W500)
                )
                SmallSpacer()
            }
            LargeSpacer()

            Text(rule.name.orEmpty())
            StandardSpacer()

            Text(rule.description.orEmpty())
            StandardSpacer()

            if (rule.safeToBlock) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Safe to block")
                    BlockerRuleIcon(rule)
                }
            } else if (rule.sideEffect.orEmpty().isNotEmpty()) {
                Text("Not Safe to block: ${rule.sideEffect}")
            }
        }
    }
}