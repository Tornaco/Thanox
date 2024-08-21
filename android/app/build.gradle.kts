import Build_gradle.GoogleFiles
import tornaco.project.android.thanox.Configs
import tornaco.project.android.thanox.Configs.keyStoreAlias
import tornaco.project.android.thanox.Configs.keyStorePassword
import tornaco.project.android.thanox.Configs.magiskModuleBuildDir
import tornaco.project.android.thanox.aapt
import tornaco.project.android.thanox.log
import kotlin.random.Random

plugins {
    alias(libs.plugins.agp.app)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.dagger.hilt.android)
}

if (Configs.thanoxBuildIsRow == true) {
    plugins.apply("com.google.gms.google-services")
    plugins.apply("com.google.firebase.crashlytics")
}

android {
    defaultConfig {
        namespace = "github.tornaco.android.thanos"
        applicationId = Configs.thanoxAppId
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
        val noMinify = project.findProperty("no-minify")?.toString()
            .toBoolean() || project.findProperty("noMinify")?.toString().toBoolean()
        log("noMinify: $noMinify")

        getByName("release") {
            isMinifyEnabled = !noMinify
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
                "proguard-rules-dynamic.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }

        getByName("debug") {
            isMinifyEnabled = !noMinify
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
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    applicationVariants.all {
        val versionName = versionName
        outputs.all {
            val impl =
                this as com.android.build.gradle.internal.api.ApkVariantOutputImpl
            impl.outputFileName =
                "thanox_${versionName}(${Configs.thanoxVersionCode}).apk"
            log("impl.outputFileName changed to:${impl.outputFileName}")
        }
    }

    packaging {
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
    implementation(libs.compose.theme.adapter)
    implementation(libs.accompanist.appcompat.theme)

    implementation(libs.dagger.hilt.android)
    implementation(libs.dagger.hilt.navigation.compose)
    kapt(libs.dagger.hilt.android.compiler)
    implementation(libs.dagger.hilt.android)

    implementation(libs.glide.landscapist)
    implementation(libs.glide)
    kapt(libs.glide.compiler)

    implementation(libs.kotlinx.serialization.json.jvm)
    implementation(libs.kotlin.reflect)

    implementation(libs.navigation.compose)
    implementation(libs.accompanist.placeholder.material)

    implementation(libs.core.splashscreen)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.core.ktx)
    // Note: Do not upgrade this lib, ComposeOverlay.
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.swiperefreshlayout)
    implementation(libs.datastore.preferences)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.interpolator)
    implementation(libs.androidx.preference)
    implementation(libs.constraint.layout)
    implementation(libs.browser)
    implementation(libs.androidx.biometric)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.extensions)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.viewbinding)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material)
    implementation(libs.navigation.compose)
    implementation(libs.compose.material.icons.core)
    implementation(libs.compose.material.icons.extended)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.gson)

    releaseImplementation(libs.chucker.no.op)
    debugImplementation(libs.chucker)

    implementation(libs.codeview)

    // https://github.com/LibChecker/LibChecker-Rules-Bundle
    implementation(libs.libchecker.rules.bundle)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)

    implementation(libs.lottie.compose)
    implementation(libs.lottie)

    implementation(libs.glide.landscapist)

    implementation(libs.accompanist.appcompat.theme)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.swiperefresh)
    implementation(libs.accompanist.flowlayout)
    implementation(libs.accompanist.navigation.animation)

    implementation(libs.timber)
    implementation(libs.coil.compose)

    implementation(libs.mpandroidchart)
    implementation(libs.licensesdialog)

    implementation(libs.listenablefuture)

    compileOnly(files(project.rootProject.file("android_sdk/xposed-api-82.jar")))

    implementation(libs.hiddenapibypass)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.adapter.rxjava2)

    implementation(libs.guava.android)

    implementation(libs.libsu.core)

    compileOnly(project(":annotation_processors:permission-requester-annotation"))
    kapt(project(":annotation_processors:permission-requester-compiler"))
    annotationProcessor(project(":annotation_processors:permission-requester-compiler"))

    implementation(libs.lottie.compose)
    implementation(libs.lottie)

    implementation(libs.xcrash.android.lib)

    implementation(project(":modules:module_common"))
    implementation(project(":modules:module_ops"))
    implementation(project(":modules:module_ops2"))
    implementation(project(":modules:module_activity_trampoline"))
    implementation(project(":modules:module_component_manager"))
    implementation(project(":modules:module_notification_recorder"))
    implementation(project(":modules:module_locker"))
    implementation(project(":modules:module_filepicker"))
    implementation(project(":modules:module_profile"))
    implementation(project(":modules:module_push_message_delegate"))
    implementation(project(":modules:module_donate"))
    implementation(project(":modules:module_feature_access"))

    implementation(project(":android_framework:base"))
    implementation(project(":android_framework:res"))
    implementation(project(":android_framework:patch-magisk:patch-framework"))
    implementation(project(":android_framework:services"))
    implementation(project(":android_framework:db"))
    implementation(project(":android_framework:patchx-entry"))

    implementation(project(":shizuku:ui"))

    implementation(project(":third_party:apkbuilder"))

    implementation(project(":third_party:time-duration-picker"))
    implementation(project(":third_party:dateformatter"))
    implementation(project(":third_party:recyclerview-fastscroll"))
    implementation(project(":third_party:search"))
    implementation(project(":third_party:remix"))
    implementation(project(":third_party:reorderable"))

    implementation(project("::third_party:libxposed:service"))

    val rowImplementation by configurations
    // Import the BoM for the Firebase platform
    rowImplementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    // Add the dependencies for the Crashlytics and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    rowImplementation("com.google.firebase:firebase-crashlytics")
    rowImplementation("com.google.firebase:firebase-analytics")
}

val generateProguardRules by tasks.registering {
    doLast {
        val ruleFile = file("proguard-rules-dynamic.pro")
        ruleFile.writeText("")

        ruleFile.apply {
            appendText("-repackageclasses ${generateRandomPackageName()}")
            appendText(System.lineSeparator())
        }
    }
}

fun generateRandomPackageName(): String {
    val packageParts = mutableListOf<String>()
    val partCount = Random.nextInt(2, 5)

    for (i in 1..partCount) {
        val partLength = Random.nextInt(3, 8)
        val part = (1..partLength)
            .map { ('a'..'z').random() } // 随机生成小写字母
            .joinToString("")
        packageParts.add(part)
    }

    return packageParts.joinToString(".")
}

tasks.named("preBuild") {
    dependsOn(generateProguardRules)
}

typealias Properties = java.util.Properties
typealias UUID = java.util.UUID
typealias Date = java.util.Date

typealias GoogleFiles = com.google.common.io.Files
afterEvaluate {
    android.applicationVariants.forEach { variant ->
        // Package magisk mods.
        val variantCapped = variant.name.capitalize()
        log("variantCapped=${variantCapped}")
        val processResTaskName = "process${variantCapped}Resources"
        log("processResTaskName=${processResTaskName}")
        val task: Task? = runCatching {
            tasks.getByPath(processResTaskName)
        }.getOrNull()
        log("processResTask=${task}")
        task?.doLast {
            packageMagiskRes(processResTaskName)
        }
    }
}

fun Project.packageMagiskRes(processResTaskName: String) {
    tasks.getByPath(processResTaskName).outputs.files.forEach { output ->
        output.listFiles()?.forEach { resFile ->
            log("$processResTaskName output res file: ${resFile.absolutePath}")
            val isResources = resFile.name.contains("resources-")
            if (isResources) {

                log(
                    """
                            
                             __  __    _    ____ ___ ____  _  __
                            |  \/  |  / \  / ___|_ _/ ___|| |/ /
                            | |\/| | / _ \| |  _ | |\___ \| ' /
                            | |  | |/ ___ \ |_| || | ___) | . \
                            |_|  |_/_/   \_\____|___|____/|_|\_\
                            
                        """.trimIndent()
                )

                log("Will add mod files into: $resFile")
                val aapt = aapt()

                val modFiles =
                    GoogleFiles.fileTraverser()
                        .depthFirstPostOrder(magiskModuleBuildDir)
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