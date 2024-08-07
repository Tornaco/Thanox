package github.tornaco.practice.honeycomb.locker.ui.verify

import android.os.Bundle
import androidx.biometric.BiometricPrompt
import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.core.T
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.app.activity.VerifyResult
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.util.ConfirmDeviceCredentialUtils
import github.tornaco.android.thanos.theme.ThemeActivity

class VerifyActivity : ThemeActivity() {
    private var requestCode = 0
    private var appInfo: AppInfo? = null
    private var biometricPrompt: BiometricPrompt? = null

    private val thanox get() = ThanosManager.from(thisActivity())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (resolveIntent()) {
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
                XLog.d("setVerifyResult ALLOW")
                thanox.activityStackSupervisor.setVerifyResult(
                    requestCode,
                    VerifyResult.ALLOW,
                    VerifyResult.REASON_USER_INPUT_CORRECT
                )
                ConfirmDeviceCredentialUtils.checkForPendingIntent(this@VerifyActivity)
                setResult(RESULT_OK)
                finish()
            } else {
                XLog.d("setVerifyResult DENY")
                thanox.activityStackSupervisor.setVerifyResult(
                    requestCode,
                    VerifyResult.DENY,
                    VerifyResult.REASON_USER_INPUT_INCORRECT
                )
                cancelVerifyAndFinish()
            }
        }
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
