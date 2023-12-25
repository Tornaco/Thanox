package tornaco.project.android.thanox

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.util.*

private val props = Properties()

object Configs {
    const val compileSdkVersion = 33
    const val minSdkVersion = 24
    const val targetSdkVersion = 33

    // We use this value to find some build tools binaries.
    // consider to find the latest version more gracefully
    // currently we set to 30.0.3 to test circle ci.
    const val buildToolsVersion = "30.0.3"
    const val ndkVersion = "21.1.6352462"
    val cmakeVersion get() = cmake()
    const val testRunner = "androidx.test.runner.AndroidJUnitRunner"

    var thanoxVersionCode: Int? = 0
    var thanoxVersionName: String? = null

    var thanoxAppId: String? = null
    var thanoxBuildHostName: String? = null
    var thanoxBuildFlavor: String? = null
    var thanoxBuildVariant: String? = null
    var thanoxBuildIsDebug: Boolean? = false
    var thanoxBuildIsRow: Boolean? = false

    val thanoxAppIdPrefix: String get() = "github.tornaco.android.thanos"
    val thanoxBuildFP: String get() = "thanox@tornaco:${UUID.randomUUID().toString()}"
    val thanoxShortcutAppIdPrefix: String get() = "github.tornaco.android.thanos.shortcut"

    val Project.resPrefix: String get() = "${this.name}_"

    val Project.outDir: File get() = rootProject.file("out")
    val Project.magiskModuleBuildDir get() = File("${outDir}/magisk_module")

    const val KeyStorePath = "environment/keys/android.jks"

    operator fun get(key: String): String? {
        val v = props[key] as? String ?: return null
        return v.ifBlank { null }
    }

    fun Project.keyStoreAlias(): String {
        return Configs["keyAlias"] ?: this.findProperty("keyAlias")?.toString() ?: ""
    }

    fun Project.keyStorePassword(): String {
        return Configs["keyPassword"] ?: this.findProperty("keyPassword")?.toString() ?: ""
    }
}

enum class Flavors {
    ROW,
    PRC
}

enum class Variant {
    DEBUG,
    RELEASE
}

object MagiskModConfigs {
    /*
       This name will be used in the name of the so file ("lib${moduleLibraryName}.so").
       If this module need to support Riru pre-v24 (API < 24), moduleLibraryName must start with "riru_".
    */
    const val moduleLibraryName = "zygisk_thanox"

    /*
       Magisk module ID
       Since Magisk use it to distinguish different modules, you should never change it.
       Note, the older version of the template uses '-' instead of '_', if your are upgrading from
       the older version, please pay attention.
    */
    const val magiskModuleId = "zygisk_thanox"

    const val moduleName = "Zygisk - Thanox"
    const val moduleAuthor = "Tornaco"
    const val moduleDescription =
        """Provides hooks for Thanox. Support Android11 & Android12. Requires Magisk 24.0+ and Zygisk"""
    val moduleVersion = Configs.thanoxVersionName
    val moduleVersionCode = Configs.thanoxVersionCode
}

@ExperimentalStdlibApi
class ThanoxProjectBuildPlugin : Plugin<Project> {
    override fun apply(project: Project) = project.applyPlugin()

    private fun Project.applyPlugin() {
        props.clear()
        rootProject.file("gradle.properties").let { propFile ->
            if (propFile.exists()) {
                propFile.inputStream().use { props.load(it) }
            }
        }
        rootProject.file("local.properties").let { propFile ->
            if (propFile.exists()) {
                propFile.inputStream().use { props.load(it) }
            }
        }

        updateBuildConfigFields()
    }

    private fun Project.updateBuildConfigFields() {
        Configs.thanoxBuildHostName = System.getProperty("os.name", "UnKnown host")
        Configs.thanoxBuildFlavor = getBuildFlavor().name.lowercase()
        Configs.thanoxBuildVariant = getBuildVariant().name.lowercase()
        Configs.thanoxBuildIsDebug = Configs.thanoxBuildVariant == "debug"
        val isRow = Configs.thanoxBuildFlavor == "row"
        Configs.thanoxBuildIsRow = isRow
        Configs.thanoxAppId =
            if (isRow) "${Configs.thanoxAppIdPrefix}.pro" else Configs.thanoxAppIdPrefix

        log("thanoxBuildHostName: ${Configs.thanoxBuildHostName}")
        log("thanoxBuildFlavor: ${Configs.thanoxBuildFlavor}")
        log("thanoxBuildVariant: ${Configs.thanoxBuildVariant}")
        log("thanoxBuildIsDebug: ${Configs.thanoxBuildIsDebug}")
        log("thanoxAppId: ${Configs.thanoxAppId}")
    }

    private fun Project.getBuildFlavor(): Flavors {
        val tskReqStr = gradle.startParameter.taskRequests.map {
            it.args
        }.toString()
        log("tskReqStr: $tskReqStr")
        val isRow = tskReqStr.contains(Flavors.ROW.name, ignoreCase = true)
        return if (isRow) {
            Flavors.ROW
        } else {
            Flavors.PRC
        }
    }

    private fun Project.getBuildVariant(): Variant {
        val tskReqStr = gradle.startParameter.taskRequests.map {
            it.args
        }.toString()
        log("tskReqStr: $tskReqStr")
        val isDebug = tskReqStr.contains(Variant.DEBUG.name, ignoreCase = true)
        return if (isDebug) {
            Variant.DEBUG
        } else {
            Variant.RELEASE
        }
    }
}
