package now.fortuitous.thanos.launchother

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import github.tornaco.android.thanos.core.T
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.android.thanos.core.util.ConfirmDeviceCredentialUtils

class LaunchOtherAppAskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showAlertDialog()
    }

    private fun showAlertDialog() {
        val callerPkg =
            intent.getParcelableExtra(T.Actions.ACTION_LAUNCH_OTHER_APP_EXTRA_CALLER_PKG) as Pkg?
        val targetPkg =
            intent.getParcelableExtra(T.Actions.ACTION_LAUNCH_OTHER_APP_EXTRA_TARGET_PKG) as Pkg?

        val thanox = ThanosManager.from(this)
        val callerAppName = callerPkg?.let {
            thanox.pkgManager.getAppInfo(it)?.appLabel
        }.orEmpty()
        val targetAppName = targetPkg?.let {
            thanox.pkgManager.getAppInfo(it)?.appLabel
        }.orEmpty()

        val root = LayoutInflater.from(this).inflate(
            github.tornaco.android.thanos.R.layout.activity_ask_launch_other_app,
            null,
            false
        )
        val rememberCheckBox =
            root.findViewById<MaterialCheckBox>(github.tornaco.android.thanos.R.id.remember)

        val launchApp = {
            ConfirmDeviceCredentialUtils.checkForPendingIntent(this)
            setResult(RESULT_OK)
            finish()
        }

        MaterialAlertDialogBuilder(this)
            .setIcon(github.tornaco.android.thanos.R.mipmap.ic_launcher_round)
            .setView(root)
            .setTitle(github.tornaco.android.thanos.res.R.string.launch_other_pkg_title)
            .setMessage(
                getString(
                    github.tornaco.android.thanos.res.R.string.launch_other_pkg_message,
                    callerAppName,
                    targetAppName
                )
            )
            .setOnDismissListener {
                onBackPressed()
            }
            .setPositiveButton(github.tornaco.android.thanos.res.R.string.launch_other_pkg_allow) { dialog, _ ->
                dialog.dismiss()
                launchApp()
                val isRemember = rememberCheckBox.isChecked
                if (isRemember) {
                    thanox.activityStackSupervisor.setLaunchOtherAppSetting(
                        callerPkg,
                        ActivityStackSupervisor.LaunchOtherAppPkgSetting.ALLOW
                    )
                }
            }
            .setNegativeButton(github.tornaco.android.thanos.res.R.string.launch_other_pkg_ignore) { dialog, _ ->
                dialog.dismiss()
                val isRemember = rememberCheckBox.isChecked
                if (isRemember) {
                    thanox.activityStackSupervisor.setLaunchOtherAppSetting(
                        callerPkg,
                        ActivityStackSupervisor.LaunchOtherAppPkgSetting.IGNORE
                    )
                }
            }
            .create().apply {
                window?.setGravity(Gravity.BOTTOM)
                show()
            }
    }
}