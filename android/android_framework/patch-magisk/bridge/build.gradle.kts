plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    implementation(project(":android_framework:base"))
    compileOnly(project(":android_framework:hidden-api"))

    implementation(libs.guava.android)
}