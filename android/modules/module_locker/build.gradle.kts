import tornaco.project.android.thanox.Configs
import tornaco.project.android.thanox.Configs.resPrefix
import tornaco.project.android.thanox.Libs
import tornaco.project.android.thanox.*

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

    buildFeatures {
        compose = true
        buildConfig = true
        aidl = true
        renderScript = false
        resValues = false
        shaders = false
        viewBinding = true
        dataBinding = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Compose.composeVersion
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
    implementation(Libs.AndroidX.swipeRefreshLayout)
    implementation(Libs.AndroidX.biometric)

    implementation(Compose.runtimeSaveAble)
    implementation(Compose.ui)
    implementation(Compose.runtime)
    implementation(Compose.material)
    implementation(Compose.activityCompose)
    implementation(Compose.viewmodel)
    implementation(Compose.navigationCompose)
    implementation(Compose.hiltNavigation)
    implementation(Compose.tooling)
    implementation(Compose.toolingPreview)
    implementation(Compose.coil)

    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.Coroutines.android)

    implementation(Libs.Accompanist.appcompatTheme)

    implementation(Libs.Others.guavaAndroid)
    implementation(Libs.Others.glide)
    annotationProcessor(Libs.Others.glideCompiler)
    kapt(Libs.Others.glideCompiler)

    implementation(project(":modules:module_common"))
    implementation(project(":android_framework:base"))

    compileOnly(project(":annotation_processors:permission-requester-annotation"))
    annotationProcessor(project(":annotation_processors:permission-requester-compiler"))
    kapt(project(":annotation_processors:permission-requester-compiler"))

    implementation(project(":android_framework:base"))
    implementation(project(":third_party:recyclerview-fastscroll"))
    implementation(project(":third_party:search"))

}
