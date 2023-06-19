import tornaco.project.android.thanox.Libs
import tornaco.project.android.thanox.addAidlTask

plugins {
    id("java")
    id("kotlin")
}

dependencies {
    // Framework
    compileOnly(files("../../android_sdk/30/android-30.jar"))

    compileOnly(Libs.Others.lombok)
    annotationProcessor(Libs.Others.lombok)

    api(Libs.Others.rxJava2)
    api(Libs.Others.gson)
    api(Libs.Others.guavaAndroid)

    compileOnly(Libs.Others.xposedApi)

    implementation(Libs.Kotlin.stdlib)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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
