plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    compileOnly(libs.xposed.api)
}