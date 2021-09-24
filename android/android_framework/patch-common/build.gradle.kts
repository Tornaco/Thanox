import tornaco.project.android.thanox.Libs

plugins {
    id("java")
    id("kotlin")
}

dependencies {
    compileOnly(project(":android_framework:hidden-api"))
    compileOnly(project(":android_framework:base"))
    implementation(Libs.Kotlin.stdlib)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}