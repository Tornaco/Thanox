import tornaco.project.android.thanox.Libs

plugins {
    id("java")
}

dependencies {
    implementation(project(":android_framework:base"))
    compileOnly(project(":android_framework:hidden-api"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}