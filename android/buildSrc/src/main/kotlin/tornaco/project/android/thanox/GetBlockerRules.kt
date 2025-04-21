package tornaco.project.android.thanox

import org.gradle.api.Project
import java.io.File
import java.net.URL


private const val taskName = "getBlockerRulesFromGithub"
private const val baseUrl =
    "https://raw.githubusercontent.com/tornaco/blocker-general-rules/main/rules"
private val allFiles = arrayOf(
    "zh-CN/general.json",
    "en/general.json",
)

fun Project.addGetBlockerRulesTask() {
    tasks.register(taskName) {
        group = "blockerRules"
        doLast {
            allFiles.forEach {
                val url = "$baseUrl/$it"
                val file =
                    File(
                        "${project.projectDir}",
                        "src/main/assets/blocker_rules/$it".replace("-", "_")
                    )
                file.parentFile?.mkdirs()
                log("Download from: $url")
                file.delete()
                file.writeText(URL(url).readText())
                log("Write to: $file")
            }
        }
    }

    tasks.named("build") {
        dependsOn(taskName)
    }
}