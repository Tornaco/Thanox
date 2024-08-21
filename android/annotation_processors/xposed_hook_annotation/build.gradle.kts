plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    compileOnly(files(project.rootProject.file("android_sdk/xposed-api-82.jar")))
}

