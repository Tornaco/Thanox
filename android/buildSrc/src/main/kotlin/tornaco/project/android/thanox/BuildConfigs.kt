package tornaco.project.android.thanox

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.net.InetAddress
import java.util.*
import java.util.regex.Pattern

private val props = Properties()

object Configs {
    const val compileSdkVersion = 30
    const val minSdkVersion = 23
    const val targetSdkVersion = 30
    const val buildToolsVersion = "30.0.3"
    const val testRunner = "androidx.test.runner.AndroidJUnitRunner"

    var thanoxVersionCode: Int? = 0
    var thanoxVersionName: String? = null

    val Project.thanoxAppIdPrefix: String get() = "github.tornaco.android.thanos"
    val Project.thanoxShortcutAppIdPrefix: String get() = "github.tornaco.android.thanos.shortcut"
    val Project.thanoxAppId: String get() = if (thanoxBuildIsRow) "$thanoxAppIdPrefix.pro" else thanoxAppIdPrefix

    val Project.thanoxBuildHostName: String get() = InetAddress.getLocalHost().hostName
    val Project.thanoxBuildFlavor: String get() = getBuildTaskRequestPartInGroup(1)
    val Project.thanoxBuildVariant: String get() = getBuildTaskRequestPartInGroup(2)
    val Project.thanoxBuildIsDebug: Boolean get() = thanoxBuildVariant == "debug"
    val Project.thanoxBuildIsRow: Boolean get() = thanoxBuildFlavor == "row"


    val Project.resPrefix: String get() = "${this.name}_"

    val Project.outDir: File get() = rootProject.file("out")

    operator fun get(key: String): String? {
        val v = props[key] as? String ?: return null
        return if (v.isBlank()) null else v
    }

    private fun Project.getBuildTaskRequestPartInGroup(group: Int): String {
        val gradle = this.rootProject.gradle
        val tskReqStr = gradle.startParameter.taskRequests.toString()

        val pattern = when {
            tskReqStr.contains("assemble") -> {
                Pattern.compile("assemble(\\w+)(Release|Debug)")
            }
            tskReqStr.contains("install") -> {
                Pattern.compile("install(\\w+)(Release|Debug)")
            }
            else -> {
                System.err.println("Unable to detect variant...")
                return ""
            }
        }

        val matcher = pattern.matcher(tskReqStr)

        return if (matcher.find()) {
            matcher.group(2).toLowerCase(Locale.getDefault())
        } else {
            throw IllegalArgumentException("getBuildTaskRequestPartInGroup#$group, NO MATCH FOUND...")
        }
    }
}


object MagiskModConfigs {
    /*
       This name will be used in the name of the so file ("lib${moduleLibraryName}.so").
       If this module need to support Riru pre-v24 (API < 24), moduleLibraryName must start with "riru_".
    */
    const val moduleLibraryName = "riru_thanox"

    /* Minimal supported Riru API version, used in the version check of riru.sh */
    const val moduleMinRiruApiVersion = 25

    /* The version name of minimal supported Riru, used in the version check of riru.sh */
    const val moduleMinRiruVersionName = "v22.0"

    /* Maximum supported Riru API version, used in the version check of riru.sh */
    const val moduleRiruApiVersion = 25

    /*
       Magisk module ID
       Since Magisk use it to distinguish different modules, you should never change it.
       Note, the older version of the template uses '-' instead of '_', if your are upgrading from
       the older version, please pay attention.
    */
    const val magiskModuleId = "riru_thanox"

    const val moduleName = "Thanox-Core"
    const val moduleAuthor = "Tornaco"
    const val moduleDescription =
        "Provide android framework and app hooks for thanox, " +
                "requires Riru $moduleMinRiruVersionName or above(需要安装riru). " +
                "Only support Android11(只支持Android11)"
    val moduleVersion = Configs.thanoxVersionName
    val moduleVersionCode = Configs.thanoxVersionCode
}

class ThanoxProjectBuildPlugin : Plugin<Project> {
    override fun apply(project: Project) = project.applyPlugin()

    private fun Project.applyPlugin() {
        props.clear()
        rootProject.file("gradle.properties").inputStream().use { props.load(it) }
        rootProject.file("local.properties").inputStream().use { props.load(it) }
    }

}