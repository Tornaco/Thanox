package now.fortuitous.thanos.apps

import android.content.Context
import android.content.Intent
import android.widget.Toast
import github.tornaco.android.thanos.common.AppListItemDescriptionComposer
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.android.thanos.module.compose.common.infra.AppBarConfig
import github.tornaco.android.thanos.module.compose.common.infra.AppItemConfig
import github.tornaco.android.thanos.module.compose.common.infra.AppUiModel
import github.tornaco.android.thanos.module.compose.common.infra.BaseAppListFilterActivity
import github.tornaco.android.thanos.module.compose.common.infra.BaseAppListFilterContainerConfig
import github.tornaco.android.thanos.module.compose.common.infra.FabItemConfig
import github.tornaco.android.thanos.module.compose.common.infra.SwitchBarConfig
import github.tornaco.android.thanos.res.R
import github.tornaco.android.thanos.support.AppFeatureManager.showDonateIntroDialog
import github.tornaco.android.thanos.support.AppFeatureManager.withSubscriptionStatus
import github.tornaco.android.thanos.support.withThanos
import github.tornaco.practice.honeycomb.locker.ui.setup.LockSettingsActivity
import github.tornaco.practice.honeycomb.locker.ui.verify.isBiometricReady
import now.fortuitous.thanos.main.PrebuiltFeatureIds
import now.fortuitous.thanos.start.BgRestrictSettingsActivity
import now.fortuitous.thanos.start.StartRuleActivity
import now.fortuitous.thanos.start.chart.ComposeStartChartActivity
import now.fortuitous.thanos.task.RecentTaskBlurSettingsActivity

class AioAppListActivity : BaseAppListFilterActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context, featureId: Int) {
            val starter = Intent(context, AioAppListActivity::class.java)
                .putExtra(KEY_FEAT_ID, featureId)
            context.startActivity(starter)
        }
    }

    override fun getConfig(featureId: Int): BaseAppListFilterContainerConfig {
        return when (featureId) {
            PrebuiltFeatureIds.ID_APPS_MANAGER -> appsManagerConfig
            PrebuiltFeatureIds.ID_BACKGROUND_START -> bgStartConfig
            PrebuiltFeatureIds.ID_BACKGROUND_RESTRICT -> bgRestrictConfig
            PrebuiltFeatureIds.ID_CLEAN_TASK_REMOVAL -> cleanTaskConfig
            PrebuiltFeatureIds.ID_APP_LOCK -> appLockConfig
            PrebuiltFeatureIds.ID_TASK_BLUR -> taskBlurConfig

            else -> error("Unknown feature id: $featureId")
        }
    }

    private val appsManagerConfig
        get() = BaseAppListFilterContainerConfig(
            featureId = "AppsManageActivity2",
            appBarConfig = AppBarConfig(
                title = {
                    it.getString(R.string.activity_title_apps_manager)
                },
            ),
            appItemConfig = AppItemConfig(
                loader = { context, pkgSetId ->
                    val composer = AppListItemDescriptionComposer(this)
                    val runningBadge = context.getString(R.string.badge_app_running)
                    val idleBadge = context.getString(R.string.badge_app_idle)

                    val res: List<AppUiModel> = context.withThanos {
                        val am = activityManager
                        return@withThanos pkgManager.getInstalledPkgsByPackageSetId(pkgSetId)
                            .distinct()
                            .map { appInfo ->
                                AppUiModel(
                                    appInfo = appInfo,
                                    description = composer.getAppItemDescription(appInfo),
                                    badges = listOfNotNull(
                                        if (am.isPackageRunning(Pkg.fromAppInfo(appInfo))) {
                                            runningBadge
                                        } else {
                                            null
                                        },
                                        if (am.isPackageIdle(Pkg.fromAppInfo(appInfo))) idleBadge else null,
                                    )
                                )
                            }
                    } ?: listOf(AppUiModel(AppInfo.dummy()))
                    res
                },
                onAppClick = {
                    AppDetailsActivity.start(this, it.appInfo)
                },
            ),
            fabs = listOf(
                FabItemConfig(
                    title = { it.getString(R.string.title_package_sets) },
                    onClick = {
                        PackageSetListActivity.start(this)
                    })
            )
        )

    private val bgStartConfig: BaseAppListFilterContainerConfig
        get() {
            val am = ThanosManager.from(this).activityManager
            return BaseAppListFilterContainerConfig(
                featureId = "bgStart",
                featureDescription = {
                    it.getString(R.string.feature_desc_start_restrict)
                },
                appBarConfig = AppBarConfig(
                    title = {
                        it.getString(R.string.activity_title_start_restrict)
                    },
                    actions = { context ->
                        listOf(
                            AppBarConfig.AppBarAction(
                                title = context.getString(R.string.menu_title_start_restrict_charts),
                                icon = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_line_chart_fill,
                                onClick = {
                                    withSubscriptionStatus(this) { subscribed: Boolean ->
                                        if (subscribed) {
                                            ComposeStartChartActivity.Starter.start(this)
                                        } else {
                                            showDonateIntroDialog(this)
                                        }
                                    }
                                }
                            ),
                            AppBarConfig.AppBarAction(
                                title = context.getString(R.string.menu_title_rules),
                                icon = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_file_list_2_line,
                                onClick = {
                                    withSubscriptionStatus(this) { subscribed: Boolean ->
                                        if (subscribed) {
                                            StartRuleActivity.start(this)
                                        } else {
                                            showDonateIntroDialog(this)
                                        }
                                    }
                                },
                            )
                        )
                    }
                ),
                switchBarConfig = SwitchBarConfig(
                    title = { context, _ ->
                        context.getString(R.string.activity_title_start_restrict)
                    },
                    isChecked = am.isStartBlockEnabled,
                    onCheckChanged = {
                        am.isStartBlockEnabled = it
                        true
                    }
                ),
                appItemConfig = AppItemConfig(
                    isCheckable = true,
                    onCheckChanged = { app, isCheck ->
                        am.setPkgStartBlockEnabled(
                            Pkg.fromAppInfo(
                                app.appInfo
                            ),
                            !isCheck
                        )
                    },
                    loader = { context, pkgSetId ->
                        commonTogglableAppLoader(context, pkgSetId) { !am.isPkgStartBlocking(it) }
                    },
                ),
            )
        }


    private val bgRestrictConfig: BaseAppListFilterContainerConfig
        get() {
            val am = ThanosManager.from(this).activityManager
            return BaseAppListFilterContainerConfig(
                featureId = "bgRestrict",
                featureDescription = {
                    it.getString(R.string.feature_desc_bg_restrict)
                },
                appBarConfig = AppBarConfig(
                    title = {
                        it.getString(R.string.activity_title_bg_restrict)
                    },
                    actions = { context ->
                        listOf(
                            AppBarConfig.AppBarAction(
                                title = context.getString(R.string.nav_title_settings),
                                icon = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_settings_2_fill,
                                onClick = {
                                    BgRestrictSettingsActivity.start(this)
                                }
                            )
                        )
                    }
                ),
                switchBarConfig = SwitchBarConfig(
                    title = { context, _ ->
                        context.getString(R.string.activity_title_bg_restrict)
                    },
                    isChecked = am.isBgRestrictEnabled,
                    onCheckChanged = {
                        am.isBgRestrictEnabled = it
                        true
                    }
                ),
                appItemConfig = AppItemConfig(
                    isCheckable = true,
                    onCheckChanged = { app, isCheck ->
                        am.setPkgBgRestrictEnabled(
                            Pkg.fromAppInfo(
                                app.appInfo
                            ),
                            !isCheck
                        )
                    },
                    loader = { context, pkgSetId ->
                        commonTogglableAppLoader(context, pkgSetId) { !am.isPkgBgRestricted(it) }
                    },
                ),
            )
        }

    private val cleanTaskConfig: BaseAppListFilterContainerConfig
        get() {
            val am = ThanosManager.from(this).activityManager
            return BaseAppListFilterContainerConfig(
                featureId = "cleanTask",
                featureDescription = {
                    it.getString(R.string.feature_desc_clean_when_task_removed)
                },
                appBarConfig = AppBarConfig(
                    title = {
                        it.getString(R.string.activity_title_clean_when_task_removed)
                    },
                ),
                switchBarConfig = SwitchBarConfig(
                    title = { context, _ ->
                        context.getString(R.string.activity_title_clean_when_task_removed)
                    },
                    isChecked = am.isCleanUpOnTaskRemovalEnabled,
                    onCheckChanged = {
                        am.isCleanUpOnTaskRemovalEnabled = it
                        true
                    }
                ),
                appItemConfig = AppItemConfig(
                    isCheckable = true,
                    onCheckChanged = { app, isCheck ->
                        am.setPkgCleanUpOnTaskRemovalEnabled(
                            Pkg.fromAppInfo(
                                app.appInfo
                            ),
                            isCheck
                        )
                    },
                    loader = { context, pkgSetId ->
                        commonTogglableAppLoader(
                            context,
                            pkgSetId
                        ) { am.isPkgCleanUpOnTaskRemovalEnabled(it) }
                    },
                ),
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
                    isCheckable = true,
                    onCheckChanged = { app, isCheck ->
                        stack.setPackageLocked(
                            app.appInfo.pkgName,
                            isCheck
                        )
                    },
                    loader = { context, pkgSetId ->
                        commonTogglableAppLoader(
                            context,
                            pkgSetId
                        ) { stack.isPackageLocked(it.pkgName) }
                    },
                ),
            )
        }

    private val taskBlurConfig: BaseAppListFilterContainerConfig
        get() {
            val am = ThanosManager.from(this).activityManager
            return BaseAppListFilterContainerConfig(
                featureId = "appLock",
                appBarConfig = AppBarConfig(
                    title = {
                        it.getString(R.string.feature_title_recent_task_blur)
                    },
                    actions = {
                        listOf(
                            AppBarConfig.AppBarAction(
                                title = it.getString(R.string.nav_title_settings),
                                icon = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_settings_2_fill,
                                onClick = {
                                    RecentTaskBlurSettingsActivity.start(this)
                                }
                            )
                        )
                    }
                ),
                switchBarConfig = SwitchBarConfig(
                    title = { context, _ ->
                        context.getString(R.string.feature_title_recent_task_blur)
                    },
                    isChecked = am.isRecentTaskBlurEnabled,
                    onCheckChanged = { isChecked ->
                        am.isRecentTaskBlurEnabled = isChecked
                        true
                    }
                ),
                appItemConfig = AppItemConfig(
                    isCheckable = true,
                    onCheckChanged = { app, isCheck ->
                        am.setPkgRecentTaskBlurEnabled(
                            Pkg.fromAppInfo(app.appInfo),
                            isCheck
                        )
                    },
                    loader = { context, pkgSetId ->
                        commonTogglableAppLoader(
                            context,
                            pkgSetId
                        ) { am.isPkgRecentTaskBlurEnabled(it) }
                    },
                ),
            )
        }

    private fun commonTogglableAppLoader(
        context: Context,
        pkgSetId: String,
        isChecked: ThanosManager.(Pkg) -> Boolean
    ): List<AppUiModel> {
        val composer = AppListItemDescriptionComposer(this)
        val runningBadge = context.getString(R.string.badge_app_running)
        val idleBadge = context.getString(R.string.badge_app_idle)

        val res: List<AppUiModel> = context.withThanos {
            val am = activityManager
            return@withThanos pkgManager.getInstalledPkgsByPackageSetId(pkgSetId)
                .distinct()
                .map { appInfo ->
                    AppUiModel(
                        appInfo = appInfo,
                        description = composer.getAppItemDescription(appInfo),
                        badges = listOfNotNull(
                            if (am.isPackageRunning(Pkg.fromAppInfo(appInfo))) {
                                runningBadge
                            } else {
                                null
                            },
                            if (am.isPackageIdle(Pkg.fromAppInfo(appInfo))) idleBadge else null,
                        ),
                        isChecked = isChecked(Pkg.fromAppInfo(appInfo))
                    )
                }
        } ?: listOf(AppUiModel(AppInfo.dummy()))
        return res.sortedBy { it.appInfo }
    }
}