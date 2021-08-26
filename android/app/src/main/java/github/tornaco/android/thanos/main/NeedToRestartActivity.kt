package github.tornaco.android.thanos.main

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.util.ActivityUtils

class NeedToRestartActivity : ComponentActivity() {

    object Starter {
        fun start(context: Context?) {
            ActivityUtils.startActivity(context, NeedToRestartActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NeedToStartScreen()
        }
    }

    @Composable
    @Preview(name = "NeedToStartScreen", showSystemUi = true, showBackground = true)
    private fun NeedToStartScreen() {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.background(color = colorResource(id = R.color.md_red_a700))) {
                Column(modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    Icon(modifier = Modifier.size(72.dp),
                        tint = Color.White,
                        painter = painterResource(id = R.drawable.ic_alert_fill),
                        contentDescription = "AlertIcon")
                    Text(modifier = Modifier.padding(16.dp),
                        color = Color.White,
                        text = stringResource(id = R.string.message_reboot_needed),
                        style = MaterialTheme.typography.h6)
                }

                Row(modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp)) {
                    Text(modifier = Modifier
                        .clickable {
                            finish()
                        }
                        .padding(16.dp),
                        color = Color.White,
                        text = AnnotatedString(stringResource(id = R.string.reboot_later)).capitalize(),
                        style = MaterialTheme.typography.body2)

                    Text(modifier = Modifier
                        .clickable {
                            ThanosManager.from(applicationContext).powerManager.reboot()
                        }
                        .padding(vertical = 16.dp),
                        color = Color.White,
                        text = AnnotatedString(stringResource(id = R.string.reboot_now)).capitalize(),
                        style = MaterialTheme.typography.body2)
                }
            }
        }
    }
}