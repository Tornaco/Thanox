package tornaco.project.android.thanox

import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem

fun Project.aapt(): String {
    val sdkDir = this.sdkDir()
    return if (OperatingSystem.current().isWindows) {
        sdkDir + "\\build-tools\\" + Configs.buildToolsVersion + "\\aapt.exe"
    } else {
        sdkDir + "/build-tools/" + Configs.buildToolsVersion + "/aapt"
    }
}

private fun Project.sdkDir(): String {
    return Configs["sdk.dir"] ?: System.getenv("ANDROID_HOME")
}