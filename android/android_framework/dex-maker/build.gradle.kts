plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.guava.android)

    implementation(project(":android_framework:base"))
    compileOnly(project(":android_framework:hidden-api"))
}