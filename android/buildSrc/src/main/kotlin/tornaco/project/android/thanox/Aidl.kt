package tornaco.project.android.thanox

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.the
import java.io.File


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
                                aidlPath
                            )
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

private fun Project.aidl() = buildTools("aidl")