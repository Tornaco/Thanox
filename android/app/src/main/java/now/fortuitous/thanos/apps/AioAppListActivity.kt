package now.fortuitous.thanos.apps

import android.content.Context
import android.content.Intent
import github.tornaco.android.thanos.common.AppListItemDescriptionComposer
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.android.thanos.module.compose.common.infra.AppItemConfig
import github.tornaco.android.thanos.module.compose.common.infra.AppUiModel
import github.tornaco.android.thanos.module.compose.common.infra.BaseAppListFilterActivity
import github.tornaco.android.thanos.module.compose.common.infra.BaseAppListFilterContainerConfig
import github.tornaco.android.thanos.module.compose.common.infra.FabItemConfig
import github.tornaco.android.thanos.module.compose.common.infra.SwitchBarConfig
import github.tornaco.android.thanos.res.R
import github.tornaco.android.thanos.support.withThanos
import now.fortuitous.thanos.main.PrebuiltFeatureIds

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

            else -> error("Unknown feature id: $featureId")
        }
    }

    private val appsManagerConfig
        get() = BaseAppListFilterContainerConfig(
            title = {
                it.getString(R.string.activity_title_apps_manager)
            },
            featureId = "AppsManageActivity2",
            appItemConfig = AppItemConfig(
                loader = { context, pkgSetId ->
                    val composer = AppListItemDescriptionComposer(thisActivity())
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

    private val bgStartConfig
        get() = BaseAppListFilterContainerConfig(
            title = {
                it.getString(R.string.activity_title_start_restrict)
            },
            featureId = "bgStart",
            featureDescription = {
                it.getString(R.string.feature_desc_start_restrict)
            },
            switchBarConfig = SwitchBarConfig(
                title = { context, _ ->
                    context.getString(R.string.activity_title_start_restrict)
                },
                isChecked = ThanosManager.from(this).activityManager.isStartBlockEnabled,
                onCheckChanged = {
                    ThanosManager.from(this).activityManager.isStartBlockEnabled = it
                }
            ),
            appItemConfig = AppItemConfig(
                isCheckable = true,
                onCheckChanged = { app, isCheck ->
                    ThanosManager.from(this).activityManager.setPkgStartBlockEnabled(
                        Pkg.fromAppInfo(
                            app.appInfo
                        ),
                        !isCheck
                    )
                },
                loader = { context, pkgSetId ->
                    val composer = AppListItemDescriptionComposer(thisActivity())
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
                                    isChecked = !am.isPkgStartBlocking(Pkg.fromAppInfo(appInfo))
                                )
                            }
                    } ?: listOf(AppUiModel(AppInfo.dummy()))
                    res.sortedBy { it.appInfo }
                },
            ),
        )
}