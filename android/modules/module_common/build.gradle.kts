import tornaco.project.android.thanox.Configs
import tornaco.project.android.thanox.Libs
import tornaco.project.android.thanox.Tests

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = false
        buildConfig = true
        aidl = true
        renderScript = false
        resValues = false
        shaders = false
        viewBinding = true
        dataBinding = true
    }
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
    implementation(Libs.AndroidX.browser) {
        exclude(group = "com.google.guava", module = "listenablefuture")
    }

    implementation(Libs.Others.glide)
    annotationProcessor(Libs.Others.glideCompiler)

    api(Libs.AndroidX.lifeCycleRuntime)
    api(Libs.AndroidX.lifeCycleExt)
    annotationProcessor(Libs.AndroidX.lifeCycleCompiler)

    compileOnly(Libs.Others.xposedApi)

    compileOnly(Libs.Others.lombok)
    annotationProcessor(Libs.Others.lombok)

    api(Libs.Others.androidCommon)
    api(Libs.ThanoxInternal.rhinoAnnotations)

    implementation(project(":android_framework:base"))
    implementation(project(":third_party:recyclerview-fastscroll"))
    implementation(project(":third_party:search"))

    testImplementation(Tests.junit)
    androidTestImplementation(Tests.androidXRunner)
}