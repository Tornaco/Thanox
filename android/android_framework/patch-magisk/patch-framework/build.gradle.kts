import tornaco.project.android.thanox.Libs

plugins {
    id("java")
    id("kotlin")
}

dependencies {
    compileOnly(Libs.Others.lombok)
    annotationProcessor(Libs.Others.lombok)

    implementation(project(":android_framework:base"))
    implementation(project(":android_framework:services"))
    implementation(project(":android_framework:patch-common"))
    compileOnly(project(":android_framework:hidden-api"))
    implementation(project(":android_framework:dex-maker"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}