package github.tornaco.practice.honeycomb.locker.ui.verify

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.module.compose.common.widget.AppIcon
import github.tornaco.android.thanos.module.compose.common.widget.LargeTitle
import github.tornaco.android.thanos.module.compose.common.widget.MediumSpacer
import github.tornaco.android.thanos.res.R
import github.tornaco.practice.honeycomb.locker.ui.verify.composelock.ComposeLock
import github.tornaco.practice.honeycomb.locker.ui.verify.composelock.ComposeLockCallback
import github.tornaco.practice.honeycomb.locker.ui.verify.composelock.Dot

@Composable
fun LockPatternContent(
    appInfo: AppInfo,
    title: String = stringResource(id = R.string.module_locker_app_name),
    onResult: (String) -> Unit
) {
    Surface(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppIcon(modifier = Modifier.size(80.dp), appInfo = appInfo)
            MediumSpacer()
            LargeTitle(text = title)
            BoxWithConstraints(Modifier.padding(top = 64.dp)) {
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
                            onResult(patternString)
                        }
                    },
                    dimension = 3
                )
            }
        }

    }
}