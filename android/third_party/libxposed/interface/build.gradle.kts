plugins {
    id("com.android.library")
}

android {
    namespace = "io.github.libxposed.service"
    compileSdk = 33
    buildToolsVersion = "33.0.1"

    defaultConfig {
        minSdk = 21
    }

    buildFeatures {
        buildConfig = false
        resValues = false
        aidl = true
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}