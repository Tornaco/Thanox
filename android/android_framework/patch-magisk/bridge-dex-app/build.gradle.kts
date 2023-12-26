plugins {
    alias(libs.plugins.agp.app)
}

android {
    defaultConfig {
        namespace = "github.tornaco.android.thanox.magisk.bridge"
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