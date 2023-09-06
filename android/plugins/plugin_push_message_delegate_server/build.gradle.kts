import tornaco.project.android.thanox.Libs
import tornaco.project.android.thanox.addAidlTask

plugins {
    id("java-library")
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-AModuleName=patchPushDelegate")
}

dependencies {
    // Framework
    compileOnly(files("../../android_sdk/27/android-27.jar"))
    compileOnly(files("../../android_sdk/27/services-27.jar"))

    compileOnly(Libs.Others.lombok)
    annotationProcessor(Libs.Others.lombok)

    api(Libs.Others.rxJava2)
    api(Libs.Others.gson)
    api(Libs.Others.guavaAndroid)

    compileOnly(Libs.Others.xposedApi)

    // Sdk
    implementation(project(":android_framework:base"))

    compileOnly(project(":annotation_processors:xposed_hook_annotation"))
    annotationProcessor(project(":annotation_processors:xposed_hook_compiler"))

    implementation(project(":android_framework:patchx-lib"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

addAidlTask()
