package tornaco.project.android.thanox

import org.gradle.api.Action
import org.gradle.api.Project

fun Project.applyAndroidModuleConfig() {
    apply {
        plugin("com.android.library")
    }


}