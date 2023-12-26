plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    api(files("${rootProject.rootDir.path}/android_sdk/33/android-33.jar"))
}