plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.rxjava)

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    implementation(project(":android_framework:base"))
    implementation(project(":android_framework:services"))
    implementation(project(":android_framework:patch-common"))
    compileOnly(project(":android_framework:hidden-api"))
    implementation(project(":android_framework:dex-maker"))
}