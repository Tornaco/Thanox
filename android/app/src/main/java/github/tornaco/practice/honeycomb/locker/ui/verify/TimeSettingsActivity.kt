package github.tornaco.practice.honeycomb.locker.ui.verify

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.MediumSpacer
import github.tornaco.android.thanos.module.compose.common.widget.SmallSpacer
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxMediumAppBarScaffold
import github.tornaco.android.thanos.res.R

@AndroidEntryPoint
class TimeSettingsActivity : ComposeThemeActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, TimeSettingsActivity::class.java)
            context.startActivity(starter)
        }
    }

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val thanos = ThanosManager.from(context)
        val initialFormula = try {
            thanos.activityStackSupervisor.lockTimeFormula
        } catch (_: Throwable) {
            ""
        }
        val formulaState = remember { mutableStateOf(initialFormula ?: "") }
        val previewState = remember { mutableStateOf("") }

        fun updatePreview() {
            previewState.value = evaluateTimeFormula(formulaState.value)
        }

        LaunchedEffect(formulaState.value) {
            updatePreview()
        }

        ThanoxMediumAppBarScaffold(
            title = {
                Text(
                    text = stringResource(id = R.string.module_locker_title_verify_custom_time_settings),
                    style = TypographyDefaults.appBarTitleTextStyle()
                )
            },
            onBackPressed = {
                finish()
            },
            actions = {}
        ) { paddings ->
            Column(Modifier.fillMaxSize().padding(paddings)) {
                Text(text = stringResource(id = R.string.module_locker_title_verify_custom_time_settings_hint))
                SmallSpacer()
                Text(text = stringResource(id = R.string.module_locker_title_verify_custom_time_settings_example))
                MediumSpacer()
                OutlinedTextField(
                    value = formulaState.value,
                    onValueChange = { formulaState.value = it },
                    label = { Text(text = stringResource(id = R.string.module_locker_title_verify_custom_time)) },
                    singleLine = true,
                    modifier = Modifier
                )
                SmallSpacer()
                Text(text = stringResource(id = R.string.module_locker_title_verify_custom_time_settings_preview) + ": " + previewState.value)
                MediumSpacer()
                Button(onClick = {
                    // Test: show toast with current preview
                    Toast.makeText(
                        context,
                        previewState.value,
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    Text(text = stringResource(id = R.string.module_locker_title_verify_custom_time_settings_test))
                }
                SmallSpacer()
                Button(onClick = {
                    val f = formulaState.value
                    if (f.contains("ss")) {
                        Toast.makeText(
                            context,
                            R.string.module_locker_title_verify_custom_time_settings_invalid,
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    try {
                        thanos.activityStackSupervisor.lockTimeFormula = f
                        Toast.makeText(
                            context,
                            R.string.module_locker_title_verify_custom_time_settings_complete,
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } catch (_: Throwable) {
                        // ignore
                    }
                }) {
                    Text(text = stringResource(id = R.string.module_locker_title_verify_custom_time_settings_save))
                }
            }
        }
    }
}