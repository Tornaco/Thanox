package github.tornaco.practice.honeycomb.locker.ui.setup

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.module.compose.common.infra.AppBarConfig
import github.tornaco.android.thanos.module.compose.common.infra.AppItemConfig
import github.tornaco.android.thanos.module.compose.common.infra.BaseAppListFilterActivity
import github.tornaco.android.thanos.module.compose.common.infra.BaseAppListFilterContainerConfig
import github.tornaco.android.thanos.module.compose.common.infra.SwitchBarConfig
import github.tornaco.android.thanos.res.R
import github.tornaco.practice.honeycomb.locker.ui.verify.isBiometricReady
import now.fortuitous.thanos.apps.commonTogglableAppLoader
import now.fortuitous.thanos.apps.commonToggleableAppListBatchOpsConfig
import now.fortuitous.thanos.main.SuggestedFeatEntries

class AppLockListActivity : BaseAppListFilterActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, AppLockListActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getConfig(featureId: Int): BaseAppListFilterContainerConfig {
        return appLockConfig.copy(
            footContent = {
                Spacer(modifier = Modifier.size(150.dp))
                SuggestedFeatEntries()
            }
        )
    }

    private val appLockConfig: BaseAppListFilterContainerConfig
        get() {
            val stack = ThanosManager.from(this).activityStackSupervisor
            return BaseAppListFilterContainerConfig(
                featureId = "appLock",
                appBarConfig = AppBarConfig(
                    title = {
                        it.getString(R.string.module_locker_app_name)
                    },
                    actions = {
                        listOf(
                            AppBarConfig.AppBarAction(
                                title = it.getString(R.string.nav_title_settings),
                                icon = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_settings_2_fill,
                                onClick = {
                                    LockSettingsActivity.start(this)
                                }
                            )
                        )
                    }
                ),
                switchBarConfig = SwitchBarConfig(
                    title = { context, _ ->
                        context.getString(R.string.module_locker_app_name)
                    },
                    isChecked = stack.isAppLockEnabled,
                    onCheckChanged = { isChecked ->
                        if (isChecked && !isBiometricReady(this)) {
                            Toast.makeText(
                                this,
                                R.string.module_locker_biometric_not_set_dialog_message,
                                Toast.LENGTH_LONG
                            ).show()
                            stack.isAppLockEnabled = false
                            false
                        } else {
                            stack.isAppLockEnabled = isChecked
                            true
                        }
                    }
                ),
                appItemConfig = AppItemConfig(
                    itemType = AppItemConfig.ItemType.Checkable(
                        onCheckChanged = { app, isCheck ->
                            stack.setPackageLocked(
                                app.appInfo.pkgName,
                                isCheck
                            )
                        },
                    ),
                    loader = { context, pkgSetId ->
                        commonTogglableAppLoader(
                            context,
                            pkgSetId
                        ) { stack.isPackageLocked(it.pkgName) }
                    },
                ),
                batchOperationConfig = commonToggleableAppListBatchOpsConfig(toggle = { app, isCheck ->
                    stack.setPackageLocked(
                        app.appInfo.pkgName,
                        isCheck
                    )
                })
            )
        }
}