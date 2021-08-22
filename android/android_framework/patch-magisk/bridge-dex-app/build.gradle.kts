import tornaco.project.android.thanox.Configs
import tornaco.project.android.thanox.Configs.thanoxAppId
import tornaco.project.android.thanox.Configs.thanoxVersionCode
import tornaco.project.android.thanox.Configs.thanoxVersionName

plugins {
    id("com.android.application")
}

android {
    defaultConfig {
        applicationId = thanoxAppId
        vectorDrawables.useSupportLibrary = true
        versionName = thanoxVersionName
        versionCode = thanoxVersionCode

        minSdk = Configs.minSdkVersion
        compileSdk = Configs.compileSdkVersion
        targetSdk = Configs.targetSdkVersion
        testInstrumentationRunner = Configs.testRunner
        multiDexEnabled = false
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = false
        buildConfig = false
        aidl = false
        renderScript = false
        resValues = false
        shaders = false
        viewBinding = false
        dataBinding = false
    }
}

dependencies {
    implementation(project(":android_framework:patch-magisk:bridge"))
}

tasks.register("extractBridgeJar", Jar::class.java) {
    group = "dex"
    from(zipTree("$buildDir/outputs/apk/release/bridge-dex-app-release-unsigned.apk"))
    include("classes.dex")
    destinationDirectory.set(file("$buildDir").resolve("outputs"))
    archiveFileName.set("thanox-bridge.jar")
}

afterEvaluate {
    tasks.findByName("extractBridgeJar")?.dependsOn("assembleRelease")
}