plugins {
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    api(files("${rootProject.rootDir.path}/android_sdk/33/android-33.jar"))
}