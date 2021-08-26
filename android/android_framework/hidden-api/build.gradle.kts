plugins {
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    api(files("${rootProject.rootDir.path}/android_sdk/30/android-30.jar"))
}