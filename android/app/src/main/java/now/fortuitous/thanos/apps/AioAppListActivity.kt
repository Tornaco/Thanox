package now.fortuitous.thanos.apps

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import github.tornaco.android.thanos.common.AppListItemDescriptionComposer
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.android.thanos.core.secure.PrivacyManager
import github.tornaco.android.thanos.module.compose.common.infra.AppBarConfig
import github.tornaco.android.thanos.module.compose.common.infra.AppItemConfig
import github.tornaco.android.thanos.module.compose.common.infra.AppUiModel
import github.tornaco.android.thanos.module.compose.common.infra.BaseAppListFilterActivity
import github.tornaco.android.thanos.module.compose.common.infra.BaseAppListFilterContainerConfig
import github.tornaco.android.thanos.module.compose.common.infra.BatchOperationConfig
import github.tornaco.android.thanos.module.compose.common.infra.FabItemConfig
import github.tornaco.android.thanos.module.compose.common.infra.SwitchBarConfig
import github.tornaco.android.thanos.res.R
import github.tornaco.android.thanos.support.AppFeatureManager.showSubscribeDialog
import github.tornaco.android.thanos.support.AppFeatureManager.withSubscriptionStatus
import github.tornaco.android.thanos.support.withThanos
import github.tornaco.practice.honeycomb.locker.ui.setup.LockSettingsActivity
import github.tornaco.practice.honeycomb.locker.ui.verify.isBiometricReady
import now.fortuitous.thanos.launchother.LaunchOtherAppRuleActivity
import now.fortuitous.thanos.main.PrebuiltFeatureIds
import now.fortuitous.thanos.main.SuggestedFeatEntries
import now.fortuitous.thanos.power.SmartStandbySettingsActivity
import now.fortuitous.thanos.power.StandByRuleActivity
import now.fortuitous.thanos.privacy.CheatRecordViewerActivity
import now.fortuitous.thanos.privacy.FieldsTemplateListActivity
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
            PrebuiltFeatureIds.ID_APPS_MANAGER_RECENT_USED -> appsManagerRecentUsedConfig
            PrebuiltFeatureIds.ID_BACKGROUND_START -> bgStartConfig
            PrebuiltFeatureIds.ID_BACKGROUND_RESTRICT -> bgRestrictConfig
            PrebuiltFeatureIds.ID_CLEAN_TASK_REMOVAL -> cleanTaskConfig
            PrebuiltFeatureIds.ID_APP_LOCK -> appLockConfig
            PrebuiltFeatureIds.ID_TASK_BLUR -> taskBlurConfig
            PrebuiltFeatureIds.ID_LAUNCH_OTHER_APP_BLOCKER -> launchOtherConfig
            PrebuiltFeatureIds.ID_PRIVACY_CHEAT -> dataCheatConfig
            PrebuiltFeatureIds.ID_OP_REMIND -> opRemindConfig
            PrebuiltFeatureIds.ID_SENSOR_OFF -> sensorOffConfig
            PrebuiltFeatureIds.ID_SMART_STANDBY -> smartStandbyConfig
            PrebuiltFeatureIds.ID_SCREEN_ON_NOTIFICATION -> screenOnNotiConfig
            PrebuiltFeatureIds.ID_RESIDENT -> residentConfig

            else -> error("Unknown feature id: $featureId")
        }.copy(
            footContent = {
                Spacer(modifier = Modifier.size(120.dp))
                SuggestedFeatEntries()
            }
        )
    }

    private val appsManagerConfig
        get() = BaseAppListFilterContainerConfig(
            featureId = "AppsManageActivity2",
            appBarConfig = AppBarConfig(
                title = {
                    it.getString(R.string.title_suggested_apps_view_all)
                },
            ),
            appItemConfig = AppItemConfig(
                itemType = AppItemConfig.ItemType.Plain(
                    onAppClick = {
                        AppDetailsActivity.start(this, it.appInfo)
                    },
                ),
                loader = { context, pkgSetId ->
                    commonTogglableAppLoader(context, pkgSetId) { false }
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

    private val appsManagerRecentUsedConfig
        get() = BaseAppListFilterContainerConfig(
            featureId = "appsManagerRecentUsedConfig",
            appBarConfig = AppBarConfig(
                title = {
                    it.getString(R.string.titile_suggested_apps_recent_used)
                },
            ),
            appItemConfig = AppItemConfig(
                itemType = AppItemConfig.ItemType.Plain(
                    onAppClick = {
                        AppDetailsActivity.start(this, it.appInfo)
                    },
                ),
                loader = { context, pkgSetId ->
                    recentAppsAppLoader(context, pkgSetId) { false }
                },
            ),
            fabs = listOf(
                FabItemConfig(
                    title = { it.getString(R.string.title_package_sets) },
                    onClick = {
                        PackageSetListActivity.start(this)
                    }),
                FabItemConfig(
                    title = { it.getString(R.string.title_suggested_apps_view_all) },
                    onClick = {
                        AioAppListActivity.start(this, PrebuiltFeatureIds.ID_APPS_MANAGER)
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
                                            showSubscribeDialog(this)
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
                                            showSubscribeDialog(this)
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
                    itemType = AppItemConfig.ItemType.Checkable(
                        onCheckChanged = { app, isCheck ->
                            am.setPkgStartBlockEnabled(
                                Pkg.fromAppInfo(
                                    app.appInfo
                                ),
                                !isCheck
                            )
                        },
                    ),
                    loader = { context, pkgSetId ->
                        commonTogglableAppLoader(context, pkgSetId) { !am.isPkgStartBlocking(it) }
                    },
                ),
                batchOperationConfig = commonToggleableAppListBatchOpsConfig(
                    toggle = { app, checked ->
                        am.setPkgStartBlockEnabled(
                            Pkg.fromAppInfo(
                                app.appInfo
                            ),
                            !checked
                        )
                    }
                )
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
                    itemType = AppItemConfig.ItemType.Checkable(
                        onCheckChanged = { app, isCheck ->
                            am.setPkgBgRestrictEnabled(
                                Pkg.fromAppInfo(
                                    app.appInfo
                                ),
                                !isCheck
                            )
                        },
                    ),
                    loader = { context, pkgSetId ->
                        commonTogglableAppLoader(context, pkgSetId) { !am.isPkgBgRestricted(it) }
                    },
                ),
                batchOperationConfig = commonToggleableAppListBatchOpsConfig(toggle = { app, checked ->
                    am.setPkgBgRestrictEnabled(
                        Pkg.fromAppInfo(
                            app.appInfo
                        ),
                        !checked
                    )
                })
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
                    itemType = AppItemConfig.ItemType.Checkable(
                        onCheckChanged = { app, isCheck ->
                            am.setPkgCleanUpOnTaskRemovalEnabled(
                                Pkg.fromAppInfo(
                                    app.appInfo
                                ),
                                isCheck
                            )
                        },
                    ),
                    loader = { context, pkgSetId ->
                        commonTogglableAppLoader(
                            context,
                            pkgSetId
                        ) { am.isPkgCleanUpOnTaskRemovalEnabled(it) }
                    },
                ),
                batchOperationConfig = commonToggleableAppListBatchOpsConfig(toggle = { app, isCheck ->
                    am.setPkgCleanUpOnTaskRemovalEnabled(
                        Pkg.fromAppInfo(
                            app.appInfo
                        ),
                        isCheck
                    )
                })
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
                    itemType = AppItemConfig.ItemType.Checkable(
                        onCheckChanged = { app, isCheck ->
                            am.setPkgRecentTaskBlurEnabled(
                                Pkg.fromAppInfo(app.appInfo),
                                isCheck
                            )
                        },
                    ),
                    loader = { context, pkgSetId ->
                        commonTogglableAppLoader(
                            context,
                            pkgSetId
                        ) { am.isPkgRecentTaskBlurEnabled(it) }
                    },
                ),
                batchOperationConfig = commonToggleableAppListBatchOpsConfig(toggle = { app, isCheck ->
                    am.setPkgRecentTaskBlurEnabled(
                        Pkg.fromAppInfo(app.appInfo),
                        isCheck
                    )
                })
            )
        }

    private val smartStandbyConfig: BaseAppListFilterContainerConfig
        get() {
            val am = ThanosManager.from(this).activityManager
            return BaseAppListFilterContainerConfig(
                featureId = "smartStandby",
                featureDescription = {
                    it.getString(R.string.feature_summary_smart_app_standby)
                },
                appBarConfig = AppBarConfig(
                    title = {
                        it.getString(R.string.feature_title_smart_app_standby)
                    },
                    actions = {
                        listOf(
                            AppBarConfig.AppBarAction(
                                title = it.getString(R.string.menu_title_rules),
                                icon = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_file_list_2_line,
                                onClick = {
                                    StandByRuleActivity.start(this)
                                }
                            ),
                            AppBarConfig.AppBarAction(
                                title = it.getString(R.string.nav_title_settings),
                                icon = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_settings_2_fill,
                                onClick = {
                                    SmartStandbySettingsActivity.start(this)
                                }
                            )
                        )
                    }
                ),
                switchBarConfig = SwitchBarConfig(
                    title = { context, _ ->
                        context.getString(R.string.feature_title_smart_app_standby)
                    },
                    isChecked = am.isSmartStandByEnabled,
                    onCheckChanged = { isChecked ->
                        am.isSmartStandByEnabled = isChecked
                        true
                    }
                ),
                appItemConfig = AppItemConfig(
                    itemType = AppItemConfig.ItemType.Checkable(
                        onCheckChanged = { app, isCheck ->
                            am.setPkgSmartStandByEnabled(
                                Pkg.fromAppInfo(app.appInfo),
                                isCheck
                            )
                        },
                    ),
                    loader = { context, pkgSetId ->
                        commonTogglableAppLoader(
                            context,
                            pkgSetId
                        ) { am.isPkgSmartStandByEnabled(it) }
                    },
                ),
                batchOperationConfig = commonToggleableAppListBatchOpsConfig(toggle = { app, isCheck ->
                    am.setPkgSmartStandByEnabled(
                        Pkg.fromAppInfo(app.appInfo),
                        isCheck
                    )
                })
            )
        }

    private val screenOnNotiConfig: BaseAppListFilterContainerConfig
        get() {
            val nm = ThanosManager.from(this).notificationManager
            return BaseAppListFilterContainerConfig(
                featureId = "screenOnNoti",
                appBarConfig = AppBarConfig(
                    title = {
                        it.getString(R.string.feature_title_light_on_notification)
                    },
                ),
                switchBarConfig = SwitchBarConfig(
                    title = { context, _ ->
                        context.getString(R.string.feature_title_light_on_notification)
                    },
                    isChecked = nm.isScreenOnNotificationEnabled,
                    onCheckChanged = { isChecked ->
                        nm.isScreenOnNotificationEnabled = isChecked
                        true
                    }
                ),
                appItemConfig = AppItemConfig(
                    itemType = AppItemConfig.ItemType.Checkable(
                        onCheckChanged = { app, isCheck ->
                            nm.setScreenOnNotificationEnabledForPkg(
                                app.appInfo.pkgName,
                                isCheck
                            )
                        },
                    ),
                    loader = { context, pkgSetId ->
                        commonTogglableAppLoader(
                            context,
                            pkgSetId
                        ) { nm.isScreenOnNotificationEnabledForPkg(it.pkgName) }
                    },
                ),
                batchOperationConfig = commonToggleableAppListBatchOpsConfig(toggle = { app, isCheck ->
                    nm.setScreenOnNotificationEnabledForPkg(
                        app.appInfo.pkgName,
                        isCheck
                    )
                })
            )
        }

    private val residentConfig: BaseAppListFilterContainerConfig
        get() {
            val am = ThanosManager.from(this).activityManager
            return BaseAppListFilterContainerConfig(
                featureId = "resident",
                appBarConfig = AppBarConfig(
                    title = {
                        it.getString(R.string.pre_title_resident)
                    },
                ),
                appItemConfig = AppItemConfig(
                    itemType = AppItemConfig.ItemType.Checkable(
                        onCheckChanged = { app, isCheck ->
                            am.setPkgResident(
                                Pkg.fromAppInfo(app.appInfo),
                                isCheck
                            )
                        },
                    ),
                    loader = { context, pkgSetId ->
                        commonTogglableAppLoader(
                            context,
                            pkgSetId
                        ) { am.isPkgResident(it) }
                    },
                ),
                batchOperationConfig = commonToggleableAppListBatchOpsConfig(toggle = { app, isCheck ->
                    am.setPkgResident(
                        Pkg.fromAppInfo(app.appInfo),
                        isCheck
                    )
                })
            )
        }

    private val opRemindConfig: BaseAppListFilterContainerConfig
        get() {
            val ops = ThanosManager.from(this).appOpsManager
            return BaseAppListFilterContainerConfig(
                featureId = "opRemind",
                appBarConfig = AppBarConfig(
                    title = {
                        it.getString(R.string.module_ops_title_op_remind_apps)
                    }
                ),
                appItemConfig = AppItemConfig(
                    itemType = AppItemConfig.ItemType.Checkable(
                        onCheckChanged = { app, isCheck ->
                            ops.setPkgOpRemindEnable(
                                app.appInfo.pkgName,
                                isCheck
                            )
                        },
                    ),
                    loader = { context, pkgSetId ->
                        commonTogglableAppLoader(
                            context,
                            pkgSetId
                        ) { ops.isPkgOpRemindEnable(it.pkgName) }
                    },
                ),
                batchOperationConfig = commonToggleableAppListBatchOpsConfig(toggle = { app, isCheck ->
                    ops.setPkgOpRemindEnable(
                        app.appInfo.pkgName,
                        isCheck
                    )
                })
            )
        }

    private val launchOtherConfig: BaseAppListFilterContainerConfig
        get() {
            val am = ThanosManager.from(this).activityStackSupervisor
            return BaseAppListFilterContainerConfig(
                featureId = "launchOther",
                appBarConfig = AppBarConfig(
                    title = {
                        it.getString(R.string.launch_other_app)
                    },
                    actions = {
                        listOf(
                            AppBarConfig.AppBarAction(
                                title = it.getString(R.string.menu_title_rules),
                                icon = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_file_list_2_line,
                                onClick = {
                                    LaunchOtherAppRuleActivity.start(this)
                                }
                            )
                        )
                    }
                ),
                switchBarConfig = SwitchBarConfig(
                    title = { context, _ ->
                        context.getString(R.string.launch_other_app)
                    },
                    isChecked = am.isLaunchOtherAppBlockerEnabled,
                    onCheckChanged = { isChecked ->
                        am.isLaunchOtherAppBlockerEnabled = isChecked
                        true
                    }
                ),
                batchOperationConfig = BatchOperationConfig(
                    operations = listOf(
                        BatchOperationConfig.Operation(
                            title = { it.getString(R.string.module_ops_mode_allow_all) },
                            onClick = { models ->
                                models.forEach {
                                    am.setLaunchOtherAppSetting(
                                        Pkg.fromAppInfo(it.appInfo),
                                        ActivityStackSupervisor.LaunchOtherAppPkgSetting.ALLOW
                                    )
                                }
                            }
                        ),
                        BatchOperationConfig.Operation(
                            title = { it.getString(R.string.module_ops_mode_ask_all) },
                            onClick = { models ->
                                models.forEach {
                                    am.setLaunchOtherAppSetting(
                                        Pkg.fromAppInfo(it.appInfo),
                                        ActivityStackSupervisor.LaunchOtherAppPkgSetting.ASK
                                    )
                                }
                            }
                        ),
                        BatchOperationConfig.Operation(
                            title = { it.getString(R.string.module_ops_mode_ignore_all) },
                            onClick = { models ->
                                models.forEach {
                                    am.setLaunchOtherAppSetting(
                                        Pkg.fromAppInfo(it.appInfo),
                                        ActivityStackSupervisor.LaunchOtherAppPkgSetting.IGNORE
                                    )
                                }
                            }
                        ),
                    )
                ),
                appItemConfig = AppItemConfig(
                    itemType = AppItemConfig.ItemType.OptionSelectable(
                        options = listOf(
                            AppItemConfig.ItemType.OptionSelectable.Option(
                                title = { it.getString(R.string.launch_other_app_options_allow) },
                                iconRes = github.tornaco.android.thanos.R.drawable.module_ops_ic_checkbox_circle_fill_green,
                                iconTintColor = Color.Unspecified,
                                id = ActivityStackSupervisor.LaunchOtherAppPkgSetting.ALLOW.toString(),
                            ),
                            AppItemConfig.ItemType.OptionSelectable.Option(
                                title = { it.getString(R.string.launch_other_app_options_ask) },
                                iconRes = github.tornaco.android.thanos.R.drawable.module_ops_ic_remix_question_fill_amber,
                                iconTintColor = Color.Unspecified,
                                id = ActivityStackSupervisor.LaunchOtherAppPkgSetting.ASK.toString(),
                            ),
                            AppItemConfig.ItemType.OptionSelectable.Option(
                                title = { it.getString(R.string.launch_other_app_options_ignore) },
                                iconRes = github.tornaco.android.thanos.R.drawable.module_ops_ic_forbid_2_fill_red,
                                iconTintColor = Color.Unspecified,
                                id = ActivityStackSupervisor.LaunchOtherAppPkgSetting.IGNORE.toString(),
                            ),
                            AppItemConfig.ItemType.OptionSelectable.Option(
                                title = { it.getString(R.string.launch_other_app_options_allow_listed) },
                                iconRes = github.tornaco.android.thanos.R.drawable.module_ops_ic_checkbox_circle_fill_light_green,
                                iconTintColor = Color.Unspecified,
                                id = ActivityStackSupervisor.LaunchOtherAppPkgSetting.ALLOW_LISTED.toString(),
                            )
                        ),
                        onSelected = { app, id ->
                            val mode = id.toIntOrNull()
                                ?: ActivityStackSupervisor.LaunchOtherAppPkgSetting.ALLOW
                            am.setLaunchOtherAppSetting(Pkg.fromAppInfo(app.appInfo), mode)
                        }
                    ),
                    loader = { context, pkgSetId ->
                        commonOptionsAppLoader(
                            context,
                            pkgSetId
                        ) { am.getLaunchOtherAppSetting(it).toString() }
                    },
                ),
            )
        }


    private val sensorOffConfig: BaseAppListFilterContainerConfig
        get() {
            val priv = ThanosManager.from(this).privacyManager
            return BaseAppListFilterContainerConfig(
                featureId = "sensorOff",
                featureDescription = { it.getString(R.string.sensor_off_summary) },
                appBarConfig = AppBarConfig(
                    title = {
                        it.getString(R.string.sensor_off)
                    }
                ),
                switchBarConfig = SwitchBarConfig(
                    title = { context, _ ->
                        context.getString(R.string.sensor_off)
                    },
                    isChecked = priv.isSensorOffEnabled,
                    onCheckChanged = { isChecked ->
                        priv.isSensorOffEnabled = isChecked
                        true
                    }
                ),
                batchOperationConfig = BatchOperationConfig(
                    operations = listOf(
                        BatchOperationConfig.Operation(
                            title = { it.getString(R.string.sensor_off_default) },
                            onClick = { models ->
                                models.forEach {
                                    priv.setSensorOffSettingsForPackage(
                                        Pkg.fromAppInfo(it.appInfo),
                                        PrivacyManager.SensorOffSettings.DEFAULT
                                    )
                                }
                            }
                        ),
                        BatchOperationConfig.Operation(
                            title = { it.getString(R.string.sensor_off_on_start) },
                            onClick = { models ->
                                models.forEach {
                                    priv.setSensorOffSettingsForPackage(
                                        Pkg.fromAppInfo(it.appInfo),
                                        PrivacyManager.SensorOffSettings.ON_START
                                    )
                                }
                            }
                        ),
                        BatchOperationConfig.Operation(
                            title = { it.getString(R.string.sensor_off_always) },
                            onClick = { models ->
                                models.forEach {
                                    priv.setSensorOffSettingsForPackage(
                                        Pkg.fromAppInfo(it.appInfo),
                                        PrivacyManager.SensorOffSettings.ALWAYS
                                    )
                                }
                            }
                        ),
                    )
                ),
                appItemConfig = AppItemConfig(
                    itemType = AppItemConfig.ItemType.OptionSelectable(
                        options = listOf(
                            AppItemConfig.ItemType.OptionSelectable.Option(
                                title = { it.getString(R.string.sensor_off_default) },
                                iconRes = github.tornaco.android.thanos.R.drawable.module_ops_ic_checkbox_circle_fill_green,
                                iconTintColor = Color.Unspecified,
                                id = PrivacyManager.SensorOffSettings.DEFAULT.toString(),
                            ),
                            AppItemConfig.ItemType.OptionSelectable.Option(
                                title = { it.getString(R.string.sensor_off_on_start) },
                                iconRes = github.tornaco.android.thanos.R.drawable.module_ops_ic_checkbox_circle_fill_grey,
                                iconTintColor = Color.Unspecified,
                                id = PrivacyManager.SensorOffSettings.ON_START.toString(),
                            ),
                            AppItemConfig.ItemType.OptionSelectable.Option(
                                title = { it.getString(R.string.sensor_off_always) },
                                iconRes = github.tornaco.android.thanos.R.drawable.module_ops_ic_forbid_2_fill_red,
                                iconTintColor = Color.Unspecified,
                                id = PrivacyManager.SensorOffSettings.ALWAYS.toString(),
                            )
                        ),
                        onSelected = { app, id ->
                            val mode = id.toIntOrNull()
                                ?: PrivacyManager.SensorOffSettings.DEFAULT
                            priv.setSensorOffSettingsForPackage(Pkg.fromAppInfo(app.appInfo), mode)
                        }
                    ),
                    loader = { context, pkgSetId ->
                        commonOptionsAppLoader(
                            context,
                            pkgSetId
                        ) { priv.getSensorOffSettingsForPackage(it).toString() }
                    },
                ),
            )
        }


    private val dataCheatConfig: BaseAppListFilterContainerConfig
        get() {
            val priv = ThanosManager.from(this).privacyManager
            return BaseAppListFilterContainerConfig(
                featureId = "dataCheat",
                featureDescription = {
                    it.getString(R.string.feature_desc_data_cheat)
                },
                appBarConfig = AppBarConfig(
                    title = {
                        it.getString(R.string.activity_title_data_cheat)
                    },
                    actions = {
                        listOf(
                            AppBarConfig.AppBarAction(
                                title = it.getString(R.string.priv_title_fields_template),
                                icon = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_file_list_2_line,
                                onClick = {
                                    FieldsTemplateListActivity.start(this, 10086)
                                }
                            ),
                            AppBarConfig.AppBarAction(
                                title = it.getString(R.string.privacy_record),
                                icon = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_history_line,
                                onClick = {
                                    withSubscriptionStatus(this) { isSubscribed: Boolean ->
                                        if (isSubscribed) {
                                            CheatRecordViewerActivity.start(this)
                                        } else {
                                            showSubscribeDialog(this)
                                        }
                                    }
                                }
                            )
                        )
                    }
                ),
                switchBarConfig = SwitchBarConfig(
                    title = { context, _ ->
                        context.getString(R.string.activity_title_data_cheat)
                    },
                    isChecked = priv.isPrivacyEnabled,
                    onCheckChanged = { isChecked ->
                        priv.isPrivacyEnabled = isChecked
                        true
                    }
                ),
                batchOperationConfig = BatchOperationConfig(
                    operations = priv.allFieldsProfiles.map { profile ->
                        BatchOperationConfig.Operation(
                            title = { profile.label },
                            onClick = { models ->
                                models.forEach {
                                    priv.selectFieldsProfileForPackage(
                                        it.appInfo.pkgName,
                                        profile.id
                                    )
                                }
                            }
                        )
                    } + listOf(
                        BatchOperationConfig.Operation(
                            title = { it.getString(R.string.common_text_value_not_set) },
                            onClick = { models ->
                                models.forEach {
                                    priv.selectFieldsProfileForPackage(
                                        it.appInfo.pkgName,
                                        null
                                    )
                                }
                            }
                        )
                    )
                ),
                appItemConfig = AppItemConfig(
                    itemType = AppItemConfig.ItemType.OptionSelectable(
                        options = priv.allFieldsProfiles.map { profile ->
                            AppItemConfig.ItemType.OptionSelectable.Option(
                                title = { profile.label },
                                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_file_list_2_line,
                                iconTintColor = Color.Unspecified,
                                id = profile.id
                            )
                        } + listOf(
                            AppItemConfig.ItemType.OptionSelectable.Option(
                                title = { it.getString(R.string.common_text_value_not_set) },
                                iconRes = github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_file_list_2_line,
                                iconTintColor = Color.Unspecified,
                                id = "",
                                showOnAppListItem = false
                            )
                        ),
                        onSelected = { app, id ->
                            priv.selectFieldsProfileForPackage(
                                app.appInfo.pkgName,
                                id.takeIf { it.isNotEmpty() })
                        }
                    ),
                    loader = { context, pkgSetId ->
                        commonOptionsAppLoader(
                            context,
                            pkgSetId
                        ) { priv.getSelectedFieldsProfileIdForPackage(it.pkgName).orEmpty() }
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
        val res: List<AppUiModel> = context.withThanos {
            val am = activityManager
            val audio = audioManager
            return@withThanos pkgManager.getInstalledPkgsByPackageSetId(pkgSetId)
                .distinct()
                .map { appInfo ->
                    val pkg = Pkg.fromAppInfo(appInfo)
                    AppUiModel(
                        appInfo = appInfo,
                        description = composer.getAppItemDescription(appInfo),
                        isRunning = am.isPackageRunning(pkg),
                        isIdle = am.isPackageIdle(pkg),
                        isPlayingSound = audio.hasAudioFocus(pkg),
                        isChecked = isChecked(pkg)
                    )
                }
        } ?: listOf(AppUiModel(AppInfo.dummy()))
        return res.sortedBy { it.appInfo }
    }

    private fun recentAppsAppLoader(
        context: Context,
        pkgSetId: String,
        isChecked: ThanosManager.(Pkg) -> Boolean
    ): List<AppUiModel> {
        val composer = AppListItemDescriptionComposer(this)
        val res: List<AppUiModel> = context.withThanos {
            val am = activityManager
            val audio = audioManager
            val pm = pkgManager
            val pkgSetPkgs = pm.getPackageSetById(
                pkgSetId, true, true
            )
            return@withThanos am.getLastRecentUsedPackages(100)
                .filter { pkgSetPkgs.pkgList.contains(it) }
                .distinct()
                .mapNotNull { pkg ->
                    val appInfo = pm.getAppInfo(pkg)
                    appInfo?.let {
                        AppUiModel(
                            appInfo = appInfo,
                            description = composer.getAppItemDescription(appInfo),
                            isRunning = am.isPackageRunning(pkg),
                            isIdle = am.isPackageIdle(pkg),
                            isPlayingSound = audio.hasAudioFocus(pkg),
                            isChecked = isChecked(pkg)
                        )
                    }
                }
        } ?: listOf(AppUiModel(AppInfo.dummy()))
        return res.sortedBy { it.appInfo }
    }

    private fun commonOptionsAppLoader(
        context: Context,
        pkgSetId: String,
        getSelectedOptionId: ThanosManager.(Pkg) -> String?
    ): List<AppUiModel> {
        val composer = AppListItemDescriptionComposer(this)
        val res: List<AppUiModel> = context.withThanos {
            val am = activityManager
            val audio = audioManager
            return@withThanos pkgManager.getInstalledPkgsByPackageSetId(pkgSetId)
                .distinct()
                .map { appInfo ->
                    val pkg = Pkg.fromAppInfo(appInfo)
                    AppUiModel(
                        appInfo = appInfo,
                        description = composer.getAppItemDescription(appInfo),
                        isRunning = am.isPackageRunning(pkg),
                        isIdle = am.isPackageIdle(pkg),
                        isPlayingSound = audio.hasAudioFocus(pkg),
                        selectedOptionId = getSelectedOptionId(Pkg.fromAppInfo(appInfo))
                    )
                }
        } ?: listOf(AppUiModel(AppInfo.dummy()))
        return res.sortedBy { it.appInfo }
    }

    private fun commonToggleableAppListBatchOpsConfig(
        toggle: (AppUiModel, Boolean) -> Unit
    ) = BatchOperationConfig(
        operations = listOf(
            BatchOperationConfig.Operation(
                title = { it.getString(R.string.on) },
                onClick = { models ->
                    models.forEach {
                        toggle(it, true)
                    }
                }
            ),
            BatchOperationConfig.Operation(
                title = { it.getString(R.string.off) },
                onClick = { models ->
                    models.forEach {
                        toggle(it, false)
                    }
                }
            ),
        )
    )
}