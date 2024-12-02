package tornaco.project.android.thanox

import org.gradle.internal.os.OperatingSystem
import java.io.File
import java.io.FileNotFoundException

fun buildTools(name: String): String {
    val sdkDir = sdkDir()

    val buildToolsDir = if (OperatingSystem.current().isWindows) {
        "$sdkDir\\build-tools\\"
    } else {
        "$sdkDir/build-tools/"
    }

    val preferredAidlFile = if (OperatingSystem.current().isWindows) {
        buildToolsDir + Configs.buildToolsVersion + "\\${name}.exe"
    } else {
        buildToolsDir + Configs.buildToolsVersion + "/${name}"
    }

    if (File(preferredAidlFile).exists()) {
        return preferredAidlFile
    }

    val latestBuildTools =
        File(buildToolsDir).listFiles()?.maxByOrNull { it.name.replace(".", "").toIntOrNull() ?: 0 }
            ?: throw FileNotFoundException("Can not find any build tools under: $buildToolsDir")

    return if (OperatingSystem.current().isWindows) {
        latestBuildTools.absolutePath + "\\${name}.exe"
    } else {
        latestBuildTools.absolutePath + "/${name}"
    }
}

fun cmake(): String {
    val sdkDir = sdkDir()

    val cmakeDir = if (OperatingSystem.current().isWindows) {
        "$sdkDir\\cmake\\"
    } else {
        "$sdkDir/cmake/"
    }

    // Workaround for CI, prefer 3.16.3
    val is_3163Exists = File(cmakeDir, "3.16.3").exists()
    if (is_3163Exists) {
        return "3.16.3"
    }

    val latestCmake =
        File(cmakeDir).listFiles()
            ?.maxByOrNull { it.name.subSequence(0, 6).toString().replace(".", "").toInt() }
            ?: throw FileNotFoundException("Can not find any cmake under: $cmakeDir")

    return latestCmake.name.also {
        println("latestCmake: $latestCmake")
    }
}

private fun sdkDir(): String {
    return Configs["sdk.dir"] ?: System.getenv("ANDROID_HOME")
}