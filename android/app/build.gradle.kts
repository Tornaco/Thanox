import tornaco.project.android.thanox.Compose
import tornaco.project.android.thanox.Configs
import tornaco.project.android.thanox.Configs.thanoxAppId
import tornaco.project.android.thanox.Libs
import tornaco.project.android.thanox.Tests

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("github.tornaco.rhino.plugin.stub_gen")
}

android {
    defaultConfig {
        applicationId = thanoxAppId
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
            Configs["keyStore"]?.also {
                storeFile = rootProject.file(it)
                storePassword = Configs["storePassword"]
                keyAlias = Configs["keyAlias"]
                keyPassword = Configs["keyPassword"]
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
    }

    flavorDimensions.add("market")
    productFlavors {
        create("row") {
            dimension = "market"
            versionNameSuffix = "-row"
        }
        create("prc") {
            dimension = "market"
            versionNameSuffix = "-prc"
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
        kotlinCompilerExtensionVersion = Compose.composeVersion
    }

    applicationVariants.forEach { variant ->
        println("applicationVariants@${project.name}-> $variant")
        variant.outputs.forEach { output ->
            val flavor = variant.name
            val versionName = variant.versionName
            //FIXME
            // outputFileName = "thanox_${versionName}.apk"
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
    implementation(Libs.AndroidX.lifeCycleRuntimeKtx)
    annotationProcessor(Libs.AndroidX.lifeCycleCompiler)

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

    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.Coroutines.android)

    implementation(Libs.Others.timber)
    implementation(Libs.Others.coil)
    implementation(Libs.Others.chart)
    implementation(Libs.Others.licensesDialog)

    implementation(Libs.Others.glide)
    annotationProcessor(Libs.Others.glideCompiler)

    implementation(Libs.Others.xposedApi)

    compileOnly(Libs.Others.lombok)
    annotationProcessor(Libs.Others.lombok)

    implementation(Libs.Others.retrofit)
    implementation(Libs.Others.retrofitConverterGson)
    implementation(Libs.Others.retrofitAdapterRxJava2)

    implementation(Libs.Accompanist.navigationAnim)

    compileOnly(project(":annotation_processors:permission-requester-annotation"))
    add("kapt", project(":annotation_processors:permission-requester-compiler"))
    annotationProcessor(project(":annotation_processors:permission-requester-compiler"))

    implementation(Libs.ThanoxInternal.moduleDonate)
    implementation(Libs.ThanoxInternal.frameworkServices)
    implementation(Libs.ThanoxInternal.frameworkDB)
    implementation(Libs.ThanoxInternal.frameworkPatchxEntry)

    implementation(project(":modules:module_common"))
    implementation(project(":modules:module_ops"))
    implementation(project(":modules:module_easteregg"))
    implementation(project(":modules:module_activity_trampoline"))
    implementation(project(":modules:module_component_manager"))
    implementation(project(":modules:module_notification_recorder"))
    implementation(project(":modules:module_locker"))
    implementation(project(":modules:module_filepicker"))
    implementation(project(":modules:module_profile"))

    implementation(project(":android_framework:base"))
    implementation(project(":android_framework:res"))
    implementation(project(":android_framework:patch-magisk:patch-framework"))

    implementation(project(":nitro:nitro_framework"))
    implementation(project(":third_party:apkbuilder"))
    implementation(project(":third_party:badge"))
    implementation(project(":third_party:time-duration-picker"))
    implementation(project(":third_party:dateformatter"))
    implementation(project(":third_party:recyclerview-fastscroll"))
    implementation(project(":third_party:search"))

    testImplementation(Tests.junit)
    testImplementation(Tests.junitKotlin)
}