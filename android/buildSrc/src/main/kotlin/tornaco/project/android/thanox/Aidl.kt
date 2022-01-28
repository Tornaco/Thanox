package tornaco.project.android.thanox

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.the
import java.io.File
import java.io.FileNotFoundException


fun Project.addAidlTask() {
    tasks.register("idlGen") {
        group = "idl"
        doLast {
            val projectPrebuiltAndroidSdkDir = File(rootProject.projectDir, "android_sdk")
            val javaSrcDirs = project.the<SourceSetContainer>()["main"].java.srcDirs
            println("aidlTask, javaSrcDirs: $javaSrcDirs")

            println("aidl executable file: ${aidl()}, is it exists? ${File(aidl()).exists()}")

            javaSrcDirs.forEach { srcDir ->
                val tree = fileTree(srcDir)
                tree.forEach { file ->
                    if (file.name.endsWith(".aidl")) {
                        val aidlPath = file.path
                        exec {
                            commandLine(
                                aidl(),
                                "-I$srcDir",
                                "-p$projectPrebuiltAndroidSdkDir/framework.aidl",
                                "-p$projectPrebuiltAndroidSdkDir/thanos.aidl",
                                aidlPath)
                        }
                    }
                }
            }
        }
    }

    tasks.named("compileJava") {
        dependsOn("idlGen")
    }
    tasks.named("build") {
        dependsOn("idlGen")
    }
}

private fun Project.aidl(): String {
    val sdkDir = this.sdkDir()

    val buildToolsDir = if (OperatingSystem.current().isWindows) {
        "$sdkDir\\build-tools\\"
    } else {
        "$sdkDir/build-tools/"
    }

    val preferredAidlFile = if (OperatingSystem.current().isWindows) {
        buildToolsDir + Configs.buildToolsVersion + "\\aidl.exe"
    } else {
        buildToolsDir + Configs.buildToolsVersion + "/aidl"
    }

    if (File(preferredAidlFile).exists()) {
        return preferredAidlFile
    }

    val latestBuildTools =
        File(buildToolsDir).listFiles()?.maxByOrNull { it.name.replace(".", "").toInt() }
            ?: throw FileNotFoundException("Can not find any build tools under: $buildToolsDir")

    return if (OperatingSystem.current().isWindows) {
        latestBuildTools.absolutePath + "\\aidl.exe"
    } else {
        latestBuildTools.absolutePath + "/aidl"
    }
}

private fun Project.sdkDir(): String {
    return Configs["sdk.dir"] ?: System.getenv("ANDROID_HOME")
}