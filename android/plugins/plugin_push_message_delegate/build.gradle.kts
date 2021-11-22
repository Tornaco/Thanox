import tornaco.project.android.thanox.Compose
import tornaco.project.android.thanox.Configs
import tornaco.project.android.thanox.Libs
import tornaco.project.android.thanox.log

plugins {
    id("com.android.application")
    id("nitro.gradle.plugin")
}

android {
    defaultConfig {
        applicationId = "github.tornaco.android.plugin.push.message.delegate"
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
                storePassword = Configs.keyStorePassword()
                keyAlias = Configs.keyStoreAlias()
                keyPassword = Configs.keyStorePassword()
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }

        getByName("debug") {
            isMinifyEnabled = true
            isShrinkResources = true
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
            impl.outputFileName =
                "plugin_wechat_push_${versionName}(${Configs.thanoxVersionCode}).apk"
            log("impl.outputFileName changed to:${impl.outputFileName}")
        }
    }

    packagingOptions {
        resources.excludes.add("META-INF/rxjava.properties")
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

    compileOnly(Libs.Others.xposedApi)

    compileOnly(Libs.Others.lombok)
    annotationProcessor(Libs.Others.lombok)

    implementation(project(":nitro:nitro_plugin"))
    implementation(project(":plugins:plugin_push_message_delegate_server"))

    compileOnly(project(":annotation_processors:permission-requester-annotation"))
    annotationProcessor(project(":annotation_processors:permission-requester-compiler"))

    implementation(project(":modules:module_common"))

    implementation(project(":android_framework:base"))
}