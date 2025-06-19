package now.fortuitous.thanos.apps

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import github.tornaco.android.thanos.common.AppListItemDescriptionComposer
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.android.thanos.module.compose.common.infra.AppBarConfig
import github.tornaco.android.thanos.module.compose.common.infra.AppItemConfig
import github.tornaco.android.thanos.module.compose.common.infra.AppUiModel
import github.tornaco.android.thanos.module.compose.common.infra.BaseAppListFilterActivity
import github.tornaco.android.thanos.module.compose.common.infra.BaseAppListFilterContainerConfig
import github.tornaco.android.thanos.module.compose.common.infra.BatchOperationConfig
import github.tornaco.android.thanos.module.compose.common.infra.FabItemConfig
import github.tornaco.android.thanos.module.compose.common.infra.SwitchBarConfig
import github.tornaco.android.thanos.res.R
import github.tornaco.android.thanos.support.AppFeatureManager.showDonateIntroDialog
import github.tornaco.android.thanos.support.AppFeatureManager.withSubscriptionStatus
import github.tornaco.android.thanos.support.withThanos
import github.tornaco.practice.honeycomb.locker.ui.setup.LockSettingsActivity
import github.tornaco.practice.honeycomb.locker.ui.verify.isBiometricReady
import now.fortuitous.thanos.launchother.LaunchOtherAppRuleActivity
import now.fortuitous.thanos.main.PrebuiltFeatureIds
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
            PrebuiltFeatureIds.ID_BACKGROUND_START -> bgStartConfig
            PrebuiltFeatureIds.ID_BACKGROUND_RESTRICT -> bgRestrictConfig
            PrebuiltFeatureIds.ID_CLEAN_TASK_REMOVAL -> cleanTaskConfig
            PrebuiltFeatureIds.ID_APP_LOCK -> appLockConfig
            PrebuiltFeatureIds.ID_TASK_BLUR -> taskBlurConfig
            PrebuiltFeatureIds.ID_LAUNCH_OTHER_APP_BLOCKER -> launchOtherConfig
            PrebuiltFeatureIds.ID_PRIVACY_CHEAT -> dataCheatConfig
            PrebuiltFeatureIds.ID_OP_REMIND -> opRemindConfig

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
                itemType = AppItemConfig.ItemType.Plain(
                    onAppClick = {
                        AppDetailsActivity.start(this, it.appInfo)
                    },
                ),
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
                            onClick = {
                                it.forEach {
                                    am.setLaunchOtherAppSetting(
                                        Pkg.fromAppInfo(it.appInfo),
                                        ActivityStackSupervisor.LaunchOtherAppPkgSetting.ALLOW
                                    )
                                }
                            }
                        ),
                        BatchOperationConfig.Operation(
                            title = { it.getString(R.string.module_ops_mode_ask_all) },
                            onClick = {
                                it.forEach {
                                    am.setLaunchOtherAppSetting(
                                        Pkg.fromAppInfo(it.appInfo),
                                        ActivityStackSupervisor.LaunchOtherAppPkgSetting.ASK
                                    )
                                }
                            }
                        ),
                        BatchOperationConfig.Operation(
                            title = { it.getString(R.string.module_ops_mode_ignore_all) },
                            onClick = {
                                it.forEach {
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
                                iconRes = github.tornaco.thanos.android.ops.R.drawable.module_ops_ic_checkbox_circle_fill_green,
                                iconTintColor = Color.Unspecified,
                                id = ActivityStackSupervisor.LaunchOtherAppPkgSetting.ALLOW.toString(),
                            ),
                            AppItemConfig.ItemType.OptionSelectable.Option(
                                title = { it.getString(R.string.launch_other_app_options_ask) },
                                iconRes = github.tornaco.thanos.android.ops.R.drawable.module_ops_ic_remix_question_fill_amber,
                                iconTintColor = Color.Unspecified,
                                id = ActivityStackSupervisor.LaunchOtherAppPkgSetting.ASK.toString(),
                            ),
                            AppItemConfig.ItemType.OptionSelectable.Option(
                                title = { it.getString(R.string.launch_other_app_options_ignore) },
                                iconRes = github.tornaco.thanos.android.ops.R.drawable.module_ops_ic_forbid_2_fill_red,
                                iconTintColor = Color.Unspecified,
                                id = ActivityStackSupervisor.LaunchOtherAppPkgSetting.IGNORE.toString(),
                            ),
                            AppItemConfig.ItemType.OptionSelectable.Option(
                                title = { it.getString(R.string.launch_other_app_options_allow_listed) },
                                iconRes = github.tornaco.thanos.android.ops.R.drawable.module_ops_ic_checkbox_circle_fill_light_green,
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
                                            showDonateIntroDialog(this)
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

    private fun commonOptionsAppLoader(
        context: Context,
        pkgSetId: String,
        getSelectedOptionId: ThanosManager.(Pkg) -> String?
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