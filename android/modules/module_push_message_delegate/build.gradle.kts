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
        buildConfig = true
        aidl = true
        renderScript = false
        resValues = false
        shaders = false
        viewBinding = true
        dataBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = tornaco.project.android.thanox.Compose.composeVersion
    }

    resourcePrefix = resPrefix
}
dependencies {
    implementation(Libs.AndroidX.androidXCore)
    implementation(Libs.Kotlin.stdlib)

    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.material)
    implementation(Libs.AndroidX.recyclerview)
    implementation(Libs.AndroidX.preference)
    implementation(Libs.AndroidX.constraint)
    implementation(Libs.AndroidX.swipeRefreshLayout)
    implementation(Libs.AndroidX.navigationFragment)
    implementation(Libs.AndroidX.navigationUI)

    implementation(tornaco.project.android.thanox.Compose.runtimeSaveAble)
    implementation(tornaco.project.android.thanox.Compose.ui)
    implementation(tornaco.project.android.thanox.Compose.runtime)
    implementation(tornaco.project.android.thanox.Compose.material)
    implementation(tornaco.project.android.thanox.Compose.material3)
    implementation(tornaco.project.android.thanox.Compose.material3Adapter)
    implementation(tornaco.project.android.thanox.Compose.activityCompose)
    implementation(tornaco.project.android.thanox.Compose.viewmodel)
    implementation(tornaco.project.android.thanox.Compose.navigationCompose)
    implementation(tornaco.project.android.thanox.Compose.hiltNavigation)
    implementation(tornaco.project.android.thanox.Compose.tooling)
    implementation(tornaco.project.android.thanox.Compose.toolingPreview)
    implementation(tornaco.project.android.thanox.Compose.composeMaterialIconsExtended)
    implementation(Libs.Accompanist.appcompatTheme)
    implementation(Libs.Accompanist.systemUiController)
    implementation(Libs.Accompanist.pager)

    implementation(Libs.Others.guavaAndroid)
    implementation(Libs.Others.glide)
    annotationProcessor(Libs.Others.glideCompiler)

    implementation(project(":modules:module_common"))
    implementation(project(":modules:module_compose_common"))
    implementation(project(":android_framework:base"))

    compileOnly(project(":annotation_processors:permission-requester-annotation"))
    annotationProcessor(project(":annotation_processors:permission-requester-compiler"))
    add("kapt", project(":annotation_processors:permission-requester-compiler"))

    implementation(project(":third_party:recyclerview-fastscroll"))
    implementation(project(":third_party:search"))

}