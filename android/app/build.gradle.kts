import Build_gradle.*
import tornaco.project.android.thanox.*
import tornaco.project.android.thanox.Configs.magiskModuleBuildDir

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    defaultConfig {
        applicationId = Configs.thanoxAppId
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

    applicationVariants.all {
        val versionName = versionName
        outputs.all {
            val impl = this as com.android.build.gradle.internal.api.ApkVariantOutputImpl
            impl.outputFileName = "thanox_${versionName}(${Configs.thanoxVersionCode}).apk"
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
    implementation(Libs.AndroidX.splash)
    annotationProcessor(Libs.AndroidX.lifeCycleCompiler)
    kapt(Libs.AndroidX.lifeCycleCompiler)

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
    implementation(Compose.composeMaterialIconsExtended)
    implementation(Libs.Accompanist.appcompatTheme)
    implementation(Libs.Accompanist.systemUiController)
    implementation(Libs.Accompanist.pager)

    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.Coroutines.android)

    implementation(Libs.Others.timber)
    implementation(Libs.Others.coil)
    implementation(Libs.Others.chart)
    implementation(Libs.Others.licensesDialog)

    implementation(Libs.Others.glide)
    annotationProcessor(Libs.Others.glideCompiler)
    kapt(Libs.Others.glideCompiler)

    compileOnly(Libs.Others.xposedApi)

    implementation(Libs.Others.retrofit)
    implementation(Libs.Others.retrofitConverterGson)
    implementation(Libs.Others.retrofitAdapterRxJava2)

    implementation(Libs.Accompanist.navigationAnim)

    compileOnly(project(":annotation_processors:permission-requester-annotation"))
    kapt(project(":annotation_processors:permission-requester-compiler"))
    annotationProcessor(project(":annotation_processors:permission-requester-compiler"))

    implementation(project(":modules:module_common"))
    implementation(project(":modules:module_compose_common"))
    implementation(project(":modules:module_ops"))
    implementation(project(":modules:module_easteregg"))
    implementation(project(":modules:module_activity_trampoline"))
    implementation(project(":modules:module_component_manager"))
    implementation(project(":modules:module_notification_recorder"))
    implementation(project(":modules:module_locker"))
    implementation(project(":modules:module_filepicker"))
    implementation(project(":modules:module_profile"))
    implementation(project(":modules:module_donate"))

    implementation(project(":android_framework:base"))
    implementation(project(":android_framework:res"))
    implementation(project(":android_framework:patch-magisk:patch-framework"))
    implementation(project(":android_framework:services"))
    implementation(project(":android_framework:patchx-entry"))

    implementation(project(":nitro:nitro_framework"))
    implementation(project(":third_party:apkbuilder"))

    implementation(project(":third_party:time-duration-picker"))
    implementation(project(":third_party:dateformatter"))
    implementation(project(":third_party:recyclerview-fastscroll"))
    implementation(project(":third_party:search"))

    testImplementation(Tests.junit)
    testImplementation(Tests.junitKotlin)
}


typealias Properties = java.util.Properties
typealias UUID = java.util.UUID
typealias Date = java.util.Date

tasks.register("updateProps") {
    group = "prop"

    log("*** updateProps ***")
    val serviceProps = Properties()
    // Src.
    serviceProps.load(project.rootProject.file("thanos.properties").inputStream())

    // Write fields.
    serviceProps.setProperty("thanox.build.version.code", Configs.thanoxVersionCode.toString())
    serviceProps.setProperty("thanox.build.version.name", Configs.thanoxVersionName)
    serviceProps.setProperty("thanox.build.variant", Configs.thanoxBuildVariant)
    serviceProps.setProperty("thanox.build.debuggable", Configs.thanoxBuildIsDebug.toString())
    serviceProps.setProperty("thanox.build.flavor", Configs.thanoxBuildFlavor)
    serviceProps.setProperty("thanox.build.host", Configs.thanoxBuildHostName)
    serviceProps.setProperty("thanox.build.fp", Configs.thanoxBuildFP)
    serviceProps.setProperty("thanox.build.date", Date().toString())
    serviceProps.setProperty("thanox.build.app.package.name", Configs.thanoxAppId)
    serviceProps.setProperty("thanox.build.app.package.name.prefix",
        Configs.thanoxAppIdPrefix)
    serviceProps.setProperty("thanox.build.shortcut.package.name.prefix",
        Configs.thanoxShortcutAppIdPrefix)


    // Write to app resources.
    serviceProps.store(
        project.file("src/main/resources/META-INF/thanos.properties").outputStream(),
        "Auto Generated, Do Not Modify.")
    log("*** updateProps done***")
}

typealias GoogleFiles = com.google.common.io.Files
afterEvaluate {
    android.applicationVariants.forEach { variant ->
        variant.assembleProvider.get().finalizedBy("updateProps")

        // Package magisk mods.
        val variantCapped = variant.name.capitalize()
        log("variantCapped=${variantCapped}")
        val processResTaskName = "process${variantCapped}Resources"
        log("processResTaskName=${processResTaskName}")
        tasks.getByPath(processResTaskName).doLast {
            tasks.getByPath(processResTaskName).outputs.files.forEach { output ->
                output.listFiles()?.forEach { resFile ->
                    log("$processResTaskName output res file: ${resFile.absolutePath}")
                    val isResources = resFile.name.contains("resources-")
                    if (isResources) {

                        log("""
                            
                             __  __    _    ____ ___ ____  _  __
                            |  \/  |  / \  / ___|_ _/ ___|| |/ /
                            | |\/| | / _ \| |  _ | |\___ \| ' /
                            | |  | |/ ___ \ |_| || | ___) | . \
                            |_|  |_/_/   \_\____|___|____/|_|\_\
                            
                        """.trimIndent())

                        log("Will add mod files into: $resFile")
                        val aapt = aapt()

                        val modFiles =
                            GoogleFiles.fileTraverser().depthFirstPostOrder(magiskModuleBuildDir)
                                .filter { it.isFile }
                                // Ignore .xxx files.
                                .filter { !it.name.startsWith(".") }
                                .map { it.relativeTo(magiskModuleBuildDir) }

                        copy {
                            from(magiskModuleBuildDir)
                            into(resFile.parentFile)
                        }

                        modFiles.forEach { modFile ->
                            exec {
                                workingDir(resFile.parentFile)
                                commandLine(
                                    aapt,
                                    "add -v",
                                    resFile.name,
                                    modFile.path
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}