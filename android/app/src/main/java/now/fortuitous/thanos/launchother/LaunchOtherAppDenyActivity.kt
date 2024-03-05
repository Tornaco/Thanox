package now.fortuitous.thanos.launchother

import android.os.Bundle
import android.widget.Toast
import github.tornaco.android.thanos.core.T
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.android.thanos.theme.ThemeActivity

class LaunchOtherAppDenyActivity : ThemeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        Toast.makeText(
            this,
            getString(
                github.tornaco.android.thanos.R.string.launch_other_pkg_ignored_message,
                callerAppName,
                targetAppName
            ),
            Toast.LENGTH_SHORT
        ).show()


        setResult(RESULT_CANCELED)
        finish()
    }
}