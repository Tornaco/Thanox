plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    api(files("${rootProject.rootDir.path}/android_sdk/30/android-30.jar"))
}