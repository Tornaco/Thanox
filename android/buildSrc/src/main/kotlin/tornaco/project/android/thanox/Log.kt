package tornaco.project.android.thanox

import org.gradle.api.Project


fun Project?.log(message: String) {
    if (this != null) {
        logger.quiet(">>>>>> $message")
    } else {
        println(">>>>>> $message")
    }
}