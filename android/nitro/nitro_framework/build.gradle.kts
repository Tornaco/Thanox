import tornaco.project.android.thanox.Configs
import tornaco.project.android.thanox.Configs.resPrefix
import tornaco.project.android.thanox.Libs

plugins {
    id("com.android.library")
}

android {
    defaultConfig {
        vectorDrawables.useSupportLibrary = true
        minSdk = Configs.minSdkVersion
        compileSdk = Configs.compileSdkVersion
        targetSdk = Configs.targetSdkVersion
        testInstrumentationRunner = Configs.testRunner
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = false
        buildConfig = true
        aidl = true
        renderScript = false
        resValues = false
        shaders = false
        viewBinding = false
        dataBinding = false
    }

    resourcePrefix = resPrefix
}

dependencies {
    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.Room.runtime)
    annotationProcessor(Libs.Room.annotationProcessor)
    annotationProcessor(Libs.Others.guavaAndroid)


    compileOnly(Libs.Others.lombok)
    annotationProcessor(Libs.Others.lombok)

    implementation(project(":android_framework:base"))
}