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
        targetCompatibility = JavaVersion.VERSION_11
        sourceCompatibility = JavaVersion.VERSION_11
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}