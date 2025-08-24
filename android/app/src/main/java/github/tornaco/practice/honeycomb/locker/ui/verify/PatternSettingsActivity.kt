package github.tornaco.practice.honeycomb.locker.ui.verify

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.elvishew.xlog.XLog
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.AnimatedTextContainer
import github.tornaco.android.thanos.module.compose.common.widget.LargeTitle
import github.tornaco.android.thanos.module.compose.common.widget.MediumSpacer
import github.tornaco.android.thanos.module.compose.common.widget.SmallTitle
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxMediumAppBarScaffold
import github.tornaco.android.thanos.res.R
import github.tornaco.practice.honeycomb.locker.ui.verify.composelock.ComposeLock
import github.tornaco.practice.honeycomb.locker.ui.verify.composelock.ComposeLockCallback
import github.tornaco.practice.honeycomb.locker.ui.verify.composelock.Dot

@AndroidEntryPoint
class PatternSettingsActivity : ComposeThemeActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, PatternSettingsActivity::class.java)
            context.startActivity(starter)
        }
    }

    @SuppressLint("UnusedBoxWithConstraintsScope")
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val viewModel = hiltViewModel<PatternSettingsVM>()
        val state by viewModel.state.collectAsState()

        ThanoxMediumAppBarScaffold(
            title = {
                Text(
                    text = stringResource(id = R.string.module_locker_title_verify_custom_pattern_settings),
                    style = TypographyDefaults.appBarTitleTextStyle()
                )
            },
            onBackPressed = {
                finish()
            },
            actions = {
            }
        ) { paddings ->
            LaunchedEffect(viewModel) {
                viewModel.events.collect { event ->
                    when (event) {
                        PatternSettingsEvent.Done -> {
                            Toast.makeText(
                                context,
                                R.string.module_locker_title_verify_custom_pattern_settings_draw_set_complete,
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }

                        PatternSettingsEvent.PatternMisMatch -> {
                            Toast.makeText(
                                context,
                                R.string.module_locker_title_verify_custom_pattern_settings_draw_mismatch,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(paddings),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val tip = when (state.step) {
                    Step.Done -> {
                        ""
                    }

                    Step.First -> {
                        stringResource(id = R.string.module_locker_title_verify_custom_pattern_settings_draw_1)
                    }

                    Step.Second -> {
                        stringResource(id = R.string.module_locker_title_verify_custom_pattern_settings_draw_2)
                    }
                }
                AnimatedTextContainer(text = tip) {
                    LargeTitle(text = it)
                }
                MediumSpacer()
                Box(Modifier.padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
                    SmallTitle(text = stringResource(id = R.string.module_locker_title_verify_custom_pattern_settings_warn))
                }

                BoxWithConstraints(Modifier.padding(top = 64.dp)) {
                    // Get the maximum width of the screen
                    val width = maxWidth
                    ComposeLock(
                        modifier = Modifier
                            .size(width),
                        callback = object : ComposeLockCallback {
                            override fun onStart(dot: Dot) {
                            }

                            override fun onDotConnected(dot: Dot) {
                            }

                            override fun onResult(result: List<Dot>) {
                                val patternString = result.joinToString("") { it.id.toString() }
                                XLog.d("patternString： $patternString")
                                viewModel.drawPattern(patternString)
                            }
                        },
                        dimension = 3
                    )
                }
            }
        }
    }
}