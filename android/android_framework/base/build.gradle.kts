import tornaco.project.android.thanox.Configs
import tornaco.project.android.thanox.addAidlTask
import java.util.Date
import java.util.UUID

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.gmazzo.buildconfig)
}

dependencies {
    // Framework
    compileOnly(files("../../android_sdk/30/android-30.jar"))

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    implementation(libs.rxjava)
    implementation(libs.gson)

    implementation(libs.guava.android)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.kotlin.reflect)

    compileOnly(libs.xposed.api)
}


buildConfig {
    packageName("github.tornaco.android.thanos")
    className("BuildProp")
    useKotlinOutput { internalVisibility = false }

    buildConfigField("String", "THANOS_APP_PKG_NAME", provider { "\"${Configs.thanoxAppId}\"" })
    buildConfigField("String", "THANOS_APP_PKG_NAME_PREFIX", provider { "\"${Configs.thanoxAppIdPrefix}\"" })
    buildConfigField("String", "THANOS_SHORTCUT_PKG_NAME_PREFIX", provider { "\"${Configs.thanoxShortcutAppIdPrefix}\"" })
    buildConfigField("String", "SHORTX_APP_PKG_NAME", provider { "\"tornaco.apps.shortx\"" })
    buildConfigField("String", "THANOS_VERSION_NAME", provider { "\"${Configs.thanoxVersionName}\"" })
    buildConfigField("int", "THANOS_VERSION_CODE", provider { "${Configs.thanoxVersionCode}" })
    buildConfigField("String", "THANOS_BUILD_VARIANT", provider { "\"${Configs.thanoxBuildVariant}\"" })
    buildConfigField("String", "THANOS_BUILD_FLAVOR", provider { "\"${Configs.thanoxBuildFlavor}\"" })
    buildConfigField("String", "THANOS_BUILD_HOST", provider { "\"${Configs.thanoxBuildHostName}\"" })
    buildConfigField("String", "THANOS_BUILD_FINGERPRINT", provider { "\"${Configs.thanoxBuildFP}\"" })
    buildConfigField("String", "THANOS_BUILD_DATE", provider { "\"${Date()}\"" })
    buildConfigField("boolean", "THANOS_BUILD_DEBUG", provider { "${Configs.thanoxBuildIsDebug}" })


    buildConfigField("String", "ACTIVITY_APP_LOCK_VERIFIER", provider { "\"github.tornaco.practice.honeycomb.locker.ui.verify.VerifyActivity\"" })
    buildConfigField("String", "ACTIVITY_APP_LOCK_SETTINGS", provider { "\"github.tornaco.practice.honeycomb.locker.ui.start.LockerStartActivity\"" })
    buildConfigField("String", "ACTION_APP_LOCK", provider { "\"github.tornaco.practice.honeycomb.bee.action.START\"" })
    buildConfigField("String", "ACTIVITY_APP_DETAILS", provider { "\"now.fortuitous.thanos.apps.AppDetailsActivity\"" })
    buildConfigField("String", "SU_SERVICE_CLASS", provider { "\"now.fortuitous.thanos.service.SuSupportService\"" })
    buildConfigField("String", "THANOX_URL_DOCS_PROFILE", provider { "\"https://tornaco.github.io/Thanox-Docs/zh/guide/profile.html\"" })
    buildConfigField("String", "THANOX_URL_DOCS_START_RULES", provider { "\"https://tornaco.github.io/Thanox-Docs/zh/guide/bg_start.html\"" })
    buildConfigField("String", "THANOX_URL_DOCS_LAUNCH_OTHER_APP_RULES", provider { "\"https://tornaco.github.io/Thanox-Docs/zh/guide/launch_other_app.html\"" })
    buildConfigField("String", "THANOX_URL_PLAY_VERSION", provider { "\"https://play.google.com/store/apps/details?id=github.tornaco.android.thanos.pro\"" })
    buildConfigField("String", "THANOX_URL_DOCS_HOME", provider { "\"https://tornaco.github.io/Thanox-Docs\"" })


    buildConfigField("String", "THANOX_FEATURE_PROFILE_A11Y", provider { "\"thanox.feature.profile.accessibility\"" });
    buildConfigField("String", "THANOX_FEATURE_PROFILE", provider { "\"thanox.feature.profile\"" });
    buildConfigField("String", "THANOX_FEATURE_APP_SMART_STAND_BY", provider { "\"thanox.feature.app.smart_standby\"" });
    buildConfigField("String", "THANOX_FEATURE_APP_TRAMPOLINE", provider { "\"thanox.feature.app.trampoline\"" });
    buildConfigField("String", "THANOX_FEATURE_APP_SMART_SERVICE_STOPPER", provider { "\"thanox.feature.app.smart_service_stopper\"" });
    buildConfigField("String", "THANOX_FEATURE_PUSH_DELEGATE", provider { "\"thanox.feature.push.delegate\"" });
    buildConfigField("String", "THANOX_FEATURE_PRIVACY_FIELD_IMEI", provider { "\"thanox.feature.privacy.field.imei\"" });
    buildConfigField("String", "THANOX_FEATURE_PRIVACY_FIELD_MEID", provider { "\"thanox.feature.privacy.field.meid\"" });
    buildConfigField("String", "THANOX_FEATURE_COMPONENT_MANAGER", provider { "\"thanox.feature.component.manager\"" });
    buildConfigField("String", "THANOX_FEATURE_START_BLOCKER", provider { "\"thanox.feature.start.blocker\"" });
    buildConfigField("String", "THANOX_FEATURE_RECENT_TASK_REMOVAL", provider { "\"thanox.feature.recent.task.removal\"" });
    buildConfigField("String", "THANOX_FEATURE_RECENT_TASK_FORCE_EXCLUDE", provider { "\"thanox.feature.recent.force.exclude\"" });
    buildConfigField("String", "THANOX_FEATURE_RECENT_TASK_FORCE_INCLUDE", provider { "\"thanox.feature.recent.force.include\"" });
    buildConfigField("String", "THANOX_FEATURE_BG_TASK_CLEAN", provider { "\"thanox.feature.background.task.clean\"" });
    buildConfigField("String", "THANOX_FEATURE_PRIVACY_DATA_CHEAT", provider { "\"thanox.feature.privacy.data.cheat\"" });
    buildConfigField("String", "THANOX_FEATURE_PRIVACY_OPS", provider { "\"thanox.feature.privacy.ops\"" });
    buildConfigField("String", "THANOX_FEATURE_PRIVACY_APPLOCK", provider { "\"thanox.feature.privacy.applock\"" });
    buildConfigField("String", "THANOX_FEATURE_PRIVACY_OPS_REMINDER", provider { "\"thanox.feature.privacy.ops.reminder\"" });
    buildConfigField("String", "THANOX_FEATURE_PRIVACY_TASK_BLUR", provider { "\"thanox.feature.privacy.task.blur\"" });
    buildConfigField("String", "THANOX_FEATURE_EXT_N_UP", provider { "\"thanox.feature.ext.n.up\"" });
    buildConfigField("String", "THANOX_FEATURE_EXT_N_RECORDER", provider { "\"thanox.feature.ext.n.recorder\"" });
    buildConfigField("String", "THANOX_FEATURE_EXT_N_RECORDER_CLIPBOARD", provider { "\"thanox.feature.ext.n.recorder.clipboard\"" });
    buildConfigField("String", "THANOX_FEATURE_EXT_APP_SMART_FREEZE", provider { "\"thanox.feature.ext.app.smart.freeze\"" });
    buildConfigField("String", "THANOX_FEATURE_PLUGIN_SUPPORT", provider { "\"thanox.feature.plugin.support\"" });
    buildConfigField("String", "THANOX_FEATURE_PREVENT_UNINSTALL", provider { "\"thanox.feature.prevent.uninstall\"" });
    buildConfigField("String", "THANOX_FEATURE_PREVENT_CLEAR_DATA", provider { "\"thanox.feature.prevent.clear.data\"" });
    buildConfigField("String", "THANOX_FEATURE_DIALOG_FORCE_CANCELABLE", provider { "\"thanox.feature.dialog.force.cancelable\"" });
    buildConfigField("String", "THANOX_FEATURE_WAKELOCK_REMOVER", provider { "\"thanox.feature.dialog.wakelock.remover\"" });
    buildConfigField("String", "THANOX_FEATURE_IZ", provider { "\"thanox.feature.iz\"" });
    buildConfigField("String", "THANOX_FEATURE_RESIDENT", provider { "\"thanox.feature.resident\"" });


    buildConfigField("String", "THANOX_CONTACT_EMAIL", provider { "\"thanox@163.com\"" })
    buildConfigField("String", "THANOX_TG_CHANNEL", provider { "\"https://t.me/thanox_mod\"" })
    buildConfigField("String", "THANOX_QQ_PRIMARY", provider { "\"482221916\"" })
    buildConfigField("String", "THANOX_SERVER_BASE_URL", provider { "\"http://thanox.emui.tech/api/\"" })
    buildConfigField("String", "THANOX_SERVER_GITHUB_BASE_URL", provider { "\"https://raw.githubusercontent.com/Tornaco/Thanox/master/\"" })

    buildConfigField("String", "THANOX_APP_PRC_SIGN_SEC", provider { "\"A92E8A9A743FE6648E2E4743FDAC89E9EB4A568F\"" })
    buildConfigField("String", "THANOX_APP_ROW_PLAY_SIGN_SEC", provider { "\"58BCDEF326ABAF65A98AF7B881FA9C059752002E\"" })
    buildConfigField("String", "THANOX_APP_ROW_PLAY_PUBLIC_KEY", provider { "\"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmbQreguSE0X2zYOGeKzeNutbc7J5oYNFoOf3EKpYyli11M4/DhZ7+XJeYTMCKfskyF9va3cus2CrNNdgnoj074sVqE1MAPhPrH2t57eblJecb99xTwB071lQTkuj3HLteToCPjZbLN/PZMJ8LBCFraEyXZ8v/tH4yClf9XlTdAGPOU9xCsz9J1Jx/OMgcqBhvXDdKgtTjJ9Q5nvnQWX5Hcf+++LsZdC7xgnsKeEaa+TK39+2oA4659kSVcXptFfoCa/FOp7YF5cWDjazUTdA2LmO0bgnlUweOP0y10E23FcdkBvI+YsqsraxF1EZ+eu0BnnUkUp3a1TmfBDzAswFMQIDAQAB\"" })

}

tasks.withType<Checkstyle> {
    source("src")
    include("**/*.java")
    exclude("**/gen/**")

    ignoreFailures = false

    reports {
        html.stylesheet =
            resources.text.fromFile(rootProject.rootDir.path + "/checkstyle/xsl/checkstyle-custom.xsl")
    }

    // empty classpath
    classpath = files()
}

addAidlTask()
