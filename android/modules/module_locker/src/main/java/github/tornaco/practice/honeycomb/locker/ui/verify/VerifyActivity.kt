package github.tornaco.practice.honeycomb.locker.ui.verify

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.bitmap.BitmapPool
import coil.compose.rememberImagePainter
import coil.decode.DataSource
import coil.decode.Options
import coil.fetch.DrawableResult
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.size.Size
import com.elvishew.xlog.XLog
import com.google.accompanist.appcompattheme.AppCompatTheme
import github.tornaco.android.thanos.core.T
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.app.activity.VerifyResult
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.theme.ThemeActivity
import github.tornaco.android.thanos.util.AppIconLoaderUtil.loadAppIconDrawableWithIconPack

class VerifyActivity : ThemeActivity() {
    private var requestCode = 0
    private var appInfo: AppInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (resolveIntent()) {
            setContent {
                AppCompatTheme {
                    AppVerifyContent()
                }
            }
            startVerifyWithBiometrics(appInfo!!)
        } else {
            XLog.e("VerifyActivity, bad intent.")
            finish()
        }
    }

    private fun resolveIntent(): Boolean {
        val pkg =
            intent?.getStringExtra(T.Actions.ACTION_LOCKER_VERIFY_EXTRA_PACKAGE) ?: return false
        requestCode =
            intent.getIntExtra(T.Actions.ACTION_LOCKER_VERIFY_EXTRA_REQUEST_CODE, Int.MIN_VALUE)
        appInfo = ThanosManager.from(this).pkgManager.getAppInfo(pkg)
        if (appInfo == null) {
            return false
        }
        return true
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun AppVerifyContent() {
        var visible by remember { mutableStateOf(false) }

        Surface(modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colors.background
            )) {
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(
                    initialOffsetY = {
                        -it
                    },
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = CubicBezierEasing(0f, 0f, 0f, 1f)

                    )
                ),
            ) {
                Box {
                    Column(modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally) {

                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(fraction = 0.3f))

                        AppIcon(modifier = Modifier.size(72.dp), appInfo!!)
                    }
                }
            }
        }

        LaunchedEffect(true) {
            visible = true
        }
    }


    @OptIn(ExperimentalCoilApi::class)
    @Composable
    fun AppIcon(modifier: Modifier, appInfo: AppInfo) {
        val context = LocalContext.current
        // Advanced
        Image(
            modifier = modifier,
            painter = rememberImagePainter(
                data = appInfo,
                builder = {
                    fetcher(object : Fetcher<AppInfo> {
                        override suspend fun fetch(
                            pool: BitmapPool,
                            data: AppInfo,
                            size: Size,
                            options: Options,
                        ): FetchResult {
                            return DrawableResult(
                                drawable = loadAppIconDrawableWithIconPack(context,
                                    appInfo.pkgName),
                                isSampled = false,
                                dataSource = DataSource.MEMORY
                            )
                        }

                        override fun key(data: AppInfo): String {
                            return data.pkgName + data.versionCode
                        }

                        override fun handles(data: AppInfo): Boolean {
                            return true
                        }
                    })
                }
            ),
            contentDescription = appInfo.appLabel
        )
    }

    private fun startVerifyWithBiometrics(appInfo: AppInfo) {
        val thanox = ThanosManager.from(this)
        if (!isBiometricReady(this)) {
            thanox.activityStackSupervisor.setVerifyResult(requestCode,
                VerifyResult.ALLOW,
                VerifyResult.REASON_USER_KEY_NOT_SET)
            finish()
            // Disable app lock
            thanox.activityStackSupervisor.isAppLockEnabled = false
            return
        }

        authenticateWithBiometric(appInfo) { success: Boolean, message: String ->
            XLog.i("authenticateWithBiometric result: $success $message")
            if (success) {
                thanox.activityStackSupervisor.setVerifyResult(requestCode,
                    VerifyResult.ALLOW,
                    VerifyResult.REASON_USER_INPUT_CORRECT)
                finish()
            } else {
                thanox.activityStackSupervisor.setVerifyResult(requestCode,
                    VerifyResult.IGNORE,
                    VerifyResult.REASON_USER_INPUT_INCORRECT)
            }
        }
    }

    override fun onBackPressed() {
        ThanosManager.from(this).activityStackSupervisor.setVerifyResult(requestCode,
            VerifyResult.IGNORE,
            VerifyResult.REASON_USER_CANCEL)
    }
}
