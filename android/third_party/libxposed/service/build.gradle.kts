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
    }

    buildTypes {
        release {
            isMinifyEnabled = true
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":third_party:libxposed:interface"))
    compileOnly("androidx.annotation:annotation:1.5.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.0")
}