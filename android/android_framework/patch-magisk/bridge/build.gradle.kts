plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation(project(":android_framework:base"))
    compileOnly(project(":android_framework:hidden-api"))

    implementation(libs.guava.android)
}