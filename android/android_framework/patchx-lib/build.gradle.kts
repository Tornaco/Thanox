plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    compileOnly(files(project.rootProject.file("android_sdk/xposed-api-82.jar")))
}