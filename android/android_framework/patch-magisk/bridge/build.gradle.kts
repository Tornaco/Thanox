plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    implementation(project(":android_framework:base"))
    compileOnly(project(":android_framework:hidden-api"))

    implementation(libs.guava.android)
}