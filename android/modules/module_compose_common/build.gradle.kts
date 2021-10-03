import tornaco.project.android.thanox.Configs
import tornaco.project.android.thanox.Configs.resPrefix
import tornaco.project.android.thanox.Libs

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        buildConfig = false
        aidl = false
        renderScript = false
        resValues = false
        shaders = false
        viewBinding = false
        dataBinding = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = tornaco.project.android.thanox.Compose.composeVersion
    }

    resourcePrefix = resPrefix
}

dependencies {
    implementation(Libs.AndroidX.androidXCore)
    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.material)
    implementation(Libs.AndroidX.recyclerview)
    implementation(Libs.AndroidX.preference)
    implementation(Libs.AndroidX.constraint)
    implementation(Libs.AndroidX.interpolator)
    implementation(Libs.AndroidX.swipeRefreshLayout)

    implementation(tornaco.project.android.thanox.Compose.runtimeSaveAble)
    implementation(tornaco.project.android.thanox.Compose.ui)
    implementation(tornaco.project.android.thanox.Compose.runtime)
    implementation(tornaco.project.android.thanox.Compose.material)
    implementation(tornaco.project.android.thanox.Compose.activityCompose)
    implementation(tornaco.project.android.thanox.Compose.viewmodel)
    implementation(tornaco.project.android.thanox.Compose.navigationCompose)
    implementation(tornaco.project.android.thanox.Compose.hiltNavigation)
    implementation(tornaco.project.android.thanox.Compose.tooling)
    implementation(tornaco.project.android.thanox.Compose.toolingPreview)
    implementation(Libs.Accompanist.appcompatTheme)
    implementation(Libs.Accompanist.systemUiController)
    implementation(Libs.Lottie.composeLottie)
    implementation(Libs.Lottie.lottie)

    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.Coroutines.android)

    implementation(project(":modules:module_common"))
}


