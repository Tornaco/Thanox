package now.fortuitous.thanos.launchother

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.core.T
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.android.thanos.core.util.ClipboardUtils
import github.tornaco.android.thanos.util.GlideApp

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
        val intentUri =
            intent.getStringExtra(T.Actions.ACTION_LAUNCH_OTHER_APP_EXTRA_TARGET_INTENT_URI)

        val thanox = ThanosManager.from(this)

        val callerApp = callerPkg?.let { thanox.pkgManager.getAppInfo(it) }
        val targetApp = targetPkg?.let { thanox.pkgManager.getAppInfo(it) }

        val callerAppName = callerApp?.appLabel.orEmpty()
        val targetAppName = targetApp?.appLabel.orEmpty()

        val root = LayoutInflater.from(this).inflate(
            R.layout.activity_ask_launch_other_app,
            null,
            false
        )
        val rememberCheckBox =
            root.findViewById<MaterialCheckBox>(R.id.remember)

        val launchApp = {
            ConfirmDeviceCredentialUtils.checkForPendingIntent(this)
            setResult(RESULT_OK)
            finish()
        }

        // 加载自定义标题布局
        val customTitle =
            LayoutInflater.from(this).inflate(R.layout.launch_other_app_ask_title, null)
        customTitle.findViewById<TextView>(R.id.title)
            .setText(github.tornaco.android.thanos.res.R.string.launch_other_pkg_title)
        val leftImage = customTitle.findViewById<ImageView>(R.id.leftIcon)
        val rightImage = customTitle.findViewById<ImageView>(R.id.rightIcon)

        GlideApp.with(this).load(callerApp).transition(withCrossFade()).into(leftImage)
        GlideApp.with(this).load(targetApp).transition(withCrossFade()).into(rightImage)

        MaterialAlertDialogBuilder(this)
            .setView(root)
            .setCustomTitle(customTitle)
            .setMessage(
                getString(
                    github.tornaco.android.thanos.res.R.string.launch_other_pkg_message,
                    callerAppName,
                    targetAppName
                )
            )
            .setOnDismissListener {
                onBackPressedDispatcher.onBackPressed()
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
            .apply {
                intentUri?.let {
                    setNeutralButton(
                        "Intent Uri"
                    ) { dialog, which ->
                        ClipboardUtils.copyToClipboard(
                            this@LaunchOtherAppAskActivity,
                            "IntentUri",
                            intentUri
                        )
                    }
                }
            }
            .create().apply {
                window?.setGravity(Gravity.BOTTOM)
                show()
            }
    }
}