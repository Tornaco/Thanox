package tornaco.project.android.thanox

import org.gradle.api.Project


fun Project.log(message: String) {
    logger.quiet(">>>>>> $message")
}