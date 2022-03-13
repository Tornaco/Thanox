import tornaco.project.android.thanox.Compose
import tornaco.project.android.thanox.Configs
import tornaco.project.android.thanox.Configs.keyStoreAlias
import tornaco.project.android.thanox.Configs.keyStorePassword
import tornaco.project.android.thanox.Libs
import tornaco.project.android.thanox.log

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    defaultConfig {
        applicationId = "${Configs.thanoxAppId}.sdk.demo"
        vectorDrawables.useSupportLibrary = true
        versionName = Configs.thanoxVersionName
        versionCode = Configs.thanoxVersionCode

        minSdk = Configs.minSdkVersion
        compileSdk = Configs.compileSdkVersion
        targetSdk = Configs.targetSdkVersion
        testInstrumentationRunner = Configs.testRunner
        multiDexEnabled = true
    }

    signingConfigs {
        create("release") {
            Configs.KeyStorePath.also {
                storeFile = rootProject.file(it)
                storePassword = keyStorePassword()
                keyAlias = keyStoreAlias()
                keyPassword = keyStorePassword()
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }

        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
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

    composeOptions {
        kotlinCompilerExtensionVersion = Compose.composeVersion
    }

    applicationVariants.all {
        val versionName = versionName
        outputs.all {
            val impl = this as com.android.build.gradle.internal.api.ApkVariantOutputImpl
            impl.outputFileName = "sdk-demo_${versionName}(${Configs.thanoxVersionCode}).apk"
            log("impl.outputFileName changed to:${impl.outputFileName}")
        }
    }

    packagingOptions {
        resources.excludes.add("META-INF/rxjava.properties")
        resources.excludes.add("META-INF/androidx.room_room-runtime.version")
        resources.excludes.add("META-INF/LICENSE-LGPL-2.1.txt")
        resources.excludes.add("META-INF/LICENSE-LGPL-3.txt")
        resources.excludes.add("META-INF/LICENSE-W3C-TEST")
        resources.excludes.add("META-INF/DEPENDENCIES")
        resources.excludes.add("META-INF/base.kotlin_module")
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
    }
}

dependencies {
    implementation(Libs.AndroidX.androidXCore)
    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.material)
    implementation(Libs.AndroidX.recyclerview)
    implementation(Libs.AndroidX.preference)
    implementation(Libs.AndroidX.constraint)
    implementation(Libs.AndroidX.swipeRefreshLayout)

    implementation(project(":modules:module_common"))
    implementation(project(":android_framework:base"))


    testImplementation(tornaco.project.android.thanox.Tests.junit)
    testImplementation(tornaco.project.android.thanox.Tests.junitKotlin)
    implementation(Libs.Coroutines.test)
    // UiAutomator Testing
    androidTestImplementation(tornaco.project.android.thanox.Tests.uiAutomation)
    androidTestImplementation(tornaco.project.android.thanox.Tests.androidXTestJunit)
    androidTestImplementation(tornaco.project.android.thanox.Tests.androidXTestEspresso)
    androidTestImplementation(Libs.Coroutines.test)
}