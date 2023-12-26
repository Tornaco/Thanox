import tornaco.project.android.thanox.Configs
import tornaco.project.android.thanox.Configs.keyStoreAlias
import tornaco.project.android.thanox.Configs.keyStorePassword
import tornaco.project.android.thanox.log

plugins {
    alias(libs.plugins.agp.app)
}

android {
    defaultConfig {
        namespace = "github.tornaco.android.thanox.shortcut.stub"
        vectorDrawables.useSupportLibrary = true
        versionName = Configs.thanoxVersionName
        versionCode = Configs.thanoxVersionCode

        minSdk = Configs.minSdkVersion
        compileSdk = 29
        // FIXME
        // Failure [-124: Failed parse during installPackageLI: Targeting R+ (version 30 and above)
        // requires the resources.arsc of installed APKs to be stored uncompressed and aligned on a 4-byte boundary]
        targetSdk = 29
        testInstrumentationRunner = Configs.testRunner
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
            isMinifyEnabled = true
            isShrinkResources = true
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

    applicationVariants.all {
        val versionName = versionName
        outputs.all {
            val impl =
                this as com.android.build.gradle.internal.api.ApkVariantOutputImpl
            impl.outputFileName =
                "shortcut_stub_template_${versionName}(${Configs.thanoxVersionCode}).apk"
            log("impl.outputFileName changed to:${impl.outputFileName}")
        }
    }
}

dependencies {
    implementation(files("libs/base.jar"))
}