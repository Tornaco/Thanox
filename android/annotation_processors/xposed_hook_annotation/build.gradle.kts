plugins {
    id("java")
}

dependencies {
    compileOnly(tornaco.project.android.thanox.Libs.Others.xposedApi)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

apply(from = "../publish_packages.gradle")
