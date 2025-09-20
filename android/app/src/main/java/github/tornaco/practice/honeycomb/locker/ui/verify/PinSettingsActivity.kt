package github.tornaco.practice.honeycomb.locker.ui.verify

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.AnimatedTextContainer
import github.tornaco.android.thanos.module.compose.common.widget.LargeTitle
import github.tornaco.android.thanos.module.compose.common.widget.MediumSpacer
import github.tornaco.android.thanos.module.compose.common.widget.SmallTitle
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxMediumAppBarScaffold
import github.tornaco.android.thanos.res.R

@AndroidEntryPoint
class PinSettingsActivity : ComposeThemeActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, PinSettingsActivity::class.java)
            context.startActivity(starter)
        }
    }

    @SuppressLint("UnusedBoxWithConstraintsScope")
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val viewModel = hiltViewModel<PinSettingsVM>()
        val state by viewModel.state.collectAsState()

        ThanoxMediumAppBarScaffold(
            title = {
                Text(
                    text = stringResource(id = R.string.module_locker_title_verify_custom_pin_settings),
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
                        PinSettingsEvent.Done -> {
                            Toast.makeText(
                                context,
                                R.string.module_locker_title_verify_custom_pin_settings_complete,
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }

                        PinSettingsEvent.PinMisMatch -> {
                            Toast.makeText(
                                context,
                                R.string.module_locker_title_verify_custom_pin_settings_mismatch,
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
                    PinStep.Done -> {
                        ""
                    }

                    PinStep.First -> {
                        stringResource(id = R.string.module_locker_title_verify_custom_pin_settings_input_1)
                    }

                    PinStep.Second -> {
                        stringResource(id = R.string.module_locker_title_verify_custom_pin_settings_input_2)
                    }
                }
                AnimatedTextContainer(text = tip) {
                    LargeTitle(text = it)
                }
                MediumSpacer()
                Box(Modifier.padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
                    SmallTitle(text = stringResource(id = R.string.module_locker_title_verify_custom_pin_settings_warn))
                }

                Box(Modifier.padding(top = 64.dp)) {
                    PinInputContent(
                        appInfo = null,
                        onVerifyPin = { pin ->
                            // 根据当前步骤决定验证结果
                            val isValid = when (state.step) {
                                PinStep.First -> true  // 第一次输入总是成功
                                PinStep.Second -> pin == state.pin  // 第二次输入检查是否匹配
                                else -> true
                            }
                            // 调用ViewModel处理输入
                            viewModel.inputPin(pin)
                            isValid
                        },
                        onSuccess = {
                            // PIN验证成功，不需要额外处理
                        },
                        onFailure = {
                            // PIN不匹配，ViewModel已经处理了状态更新
                        }
                    )
                }
            }
        }
    }
}