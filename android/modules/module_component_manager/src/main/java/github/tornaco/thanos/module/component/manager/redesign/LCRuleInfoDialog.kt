@file:OptIn(ExperimentalLayoutApi::class)

package github.tornaco.thanos.module.component.manager.redesign;

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Badge
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import github.tornaco.android.thanos.core.annotation.DoNotStrip
import github.tornaco.android.thanos.core.util.GsonUtils
import github.tornaco.android.thanos.module.compose.common.widget.LargeSpacer
import github.tornaco.android.thanos.module.compose.common.widget.SmallSpacer
import github.tornaco.android.thanos.module.compose.common.widget.StandardSpacer
import github.tornaco.thanos.module.component.manager.redesign.rule.ComponentRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

@Composable
internal fun LCRuleInfoDialog(rule: ComponentRule, dismiss: () -> Unit) {
    Surface(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.large,
        tonalElevation = AlertDialogDefaults.TonalElevation
    ) {
        var description by remember { mutableStateOf<RuleDescription?>(null) }
        LaunchedEffect(rule) {
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    val json =
                        URL(rule.descriptionUrl.orEmpty()).readText(charset = Charsets.UTF_8)
                    val model = GsonUtils.GSON.fromJson(json, RuleDescription::class.java)
                    description = model
                }.onFailure {
                    description = RuleDescription(
                        description = "${it.message}",
                        label = "ERROR"
                    )
                }
            }
        }
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
                    rule.label,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W500)
                )
                SmallSpacer()
                LCRuleIcon(rule)
            }
            LargeSpacer()
            AnimatedContent(description) { rd ->
                if (rd == null) {
                    CircularProgressIndicator()
                } else {
                    Column {
                        Text("Team: ${rd.team.orEmpty()}")
                        StandardSpacer()
                        FlowRow {
                            rd.contributors?.forEach {
                                Badge(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                                    Icon(
                                        painter = painterResource(github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_user_line),
                                        contentDescription = "Filter",
                                        modifier = Modifier.size(9.dp)
                                    )
                                    SmallSpacer()
                                    Text(it)
                                }
                                SmallSpacer()
                            }
                        }
                        StandardSpacer()
                        Text(
                            rd.description.orEmpty(),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

// {
//    "label": "Jetpack Camera",
//    "team": "Google",
//    "iconUrl": "",
//    "contributors": ["Absinthe"],
//    "description": "CameraX 是 Jetpack 的新增库。利用该库，可以更轻松地向应用添加相机功能。该库提供了很多兼容性修复程序和解决方法，有助于在众多设备上打造一致的开发者体验。",
//    "relativeUrl": "https://developer.android.com/jetpack/androidx/releases/camera"
//}
@DoNotStrip
data class RuleDescription(
    val label: String? = null,
    val team: String? = null,
    val iconUrl: String? = null,
    val contributors: List<String>? = null,
    val description: String? = null,
    val relativeUrl: String? = null
)
