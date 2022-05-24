package github.tornaco.practice.honeycomb.locker.ui.verify

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.practice.honeycomb.locker.R


fun FragmentActivity.authenticateWithBiometric(appInfo: AppInfo, onResult: (Boolean, String) -> Unit) {
    if (isBiometricReady(this)) {
        val promptInfo = getPromptInfo(appInfo)
        val biometricPrompt = getBiometricPrompt(onResult)
        biometricPrompt.authenticate(promptInfo)
    } else {
        onResult(false, "Biometric not ready")
    }
}

fun isBiometricReady(context: Context) =
    hasBiometricCapability(context) == BiometricManager.BIOMETRIC_SUCCESS

private fun hasBiometricCapability(context: Context): Int {
    val biometricManager = BiometricManager.from(context)
    return biometricManager.canAuthenticate(
        BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
}

private fun FragmentActivity.getBiometricPrompt(onResult: (Boolean, String) -> Unit): BiometricPrompt {
    val executor = ContextCompat.getMainExecutor(this)

    val callback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            onResult(false, "$errorCode - $errString")
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            onResult(false, "Unknown reason")
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            onResult(true, "Success")
        }
    }

    return BiometricPrompt(this, executor, callback)
}

private fun FragmentActivity.getPromptInfo(appInfo: AppInfo): BiometricPrompt.PromptInfo {
    return BiometricPrompt.PromptInfo.Builder()
        .setTitle(getString(R.string.module_locker_app_name))
        .setSubtitle(getString(R.string.module_locker_verify_input_password, appInfo.appLabel))
        .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG
                or BiometricManager.Authenticators.BIOMETRIC_WEAK
                or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
        .build()
}