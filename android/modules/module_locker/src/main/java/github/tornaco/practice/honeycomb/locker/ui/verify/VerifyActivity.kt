package github.tornaco.practice.honeycomb.locker.ui.verify

import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.core.T
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor
import github.tornaco.android.thanos.core.app.activity.VerifyResult
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.util.ConfirmDeviceCredentialUtils
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity

class VerifyActivity : ComposeThemeActivity() {
    private var requestCode = 0
    private var appInfo: AppInfo? = null
    private var biometricPrompt: BiometricPrompt? = null

    private val thanox by lazy { ThanosManager.from(thisActivity()) }
    private val lockMethod
        get() = thanox.activityStackSupervisor.lockMethod.also {
            XLog.d("lockMethod: $it")
        }
    private val lockPattern
        get() = thanox.activityStackSupervisor.lockPattern.also {
            XLog.d("lockPattern: $it")
        }

    private var misMatchTimes = 0

    private fun canUseLockPattern(): Boolean {
        return lockMethod == ActivityStackSupervisor.LockMethod.PATTERN && !lockPattern.isNullOrBlank()
    }

    @Composable
    override fun Content() {
        val isValidIntent = remember { resolveIntent() }
        if (isValidIntent) {
            if (canUseLockPattern()) {
                LockPatternContent(appInfo = appInfo!!, onResult = {
                    if (it == lockPattern) {
                        verifySuccess()
                    } else {
                        if (misMatchTimes > 3) {
                            verifyFail()
                        }
                        misMatchTimes += 1
                    }
                })
            } else {
                LaunchedEffect(Unit) {
                    startVerifyWithBiometrics(appInfo!!)
                }
            }
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
        return appInfo != null
    }

    private fun startVerifyWithBiometrics(appInfo: AppInfo) {
        if (!isBiometricReady(this)) {
            thanox.activityStackSupervisor.setVerifyResult(
                requestCode,
                VerifyResult.ALLOW,
                VerifyResult.REASON_USER_KEY_NOT_SET
            )
            setResult(RESULT_CANCELED)
            finish()
            // Disable app lock
            thanox.activityStackSupervisor.isAppLockEnabled = false
            return
        }

        biometricPrompt = authenticateWithBiometric(appInfo) { success: Boolean, message: String ->
            XLog.i("authenticateWithBiometric result: $success $message")
            if (success) {
                verifySuccess()
            } else {
                verifyFail()
            }
        }
    }

    private fun verifyFail() {
        XLog.d("setVerifyResult DENY")
        thanox.activityStackSupervisor.setVerifyResult(
            requestCode,
            VerifyResult.DENY,
            VerifyResult.REASON_USER_INPUT_INCORRECT
        )
        cancelVerifyAndFinish()
    }

    private fun verifySuccess() {
        XLog.d("setVerifyResult ALLOW")
        thanox.activityStackSupervisor.setVerifyResult(
            requestCode,
            VerifyResult.ALLOW,
            VerifyResult.REASON_USER_INPUT_CORRECT
        )
        ConfirmDeviceCredentialUtils.checkForPendingIntent(this@VerifyActivity)
        setResult(RESULT_OK)
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        thanox.activityStackSupervisor.setVerifyResult(
            requestCode,
            VerifyResult.DENY,
            VerifyResult.REASON_USER_CANCEL
        )
        cancelVerifyAndFinish()
    }

    private fun cancelVerifyAndFinish() {
        XLog.w("cancelVerifyAndFinish...")
        kotlin.runCatching { biometricPrompt?.cancelAuthentication() }
        setResult(RESULT_CANCELED)
        finish()
    }


    val EXTRA_TASK_ID = "android.intent.extra.TASK_ID"
}
