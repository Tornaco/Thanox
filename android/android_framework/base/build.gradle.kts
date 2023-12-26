import tornaco.project.android.thanox.addAidlTask

plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    // Framework
    compileOnly(files("../../android_sdk/30/android-30.jar"))

    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")

    implementation(libs.rxjava)
    implementation(libs.gson)

    implementation(libs.guava.android)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.kotlin.reflect)

    implementation(libs.xposed.api)
}

tasks.withType<Checkstyle> {
    source("src")
    include("**/*.java")
    exclude("**/gen/**")

    ignoreFailures = false

    reports {
        html.stylesheet =
            resources.text.fromFile(rootProject.rootDir.path + "/checkstyle/xsl/checkstyle-custom.xsl")
    }

    // empty classpath
    classpath = files()
}

addAidlTask()
