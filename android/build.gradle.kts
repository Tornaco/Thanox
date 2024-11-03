import tornaco.project.android.thanox.Configs
import tornaco.project.android.thanox.Configs.outDir
import tornaco.project.android.thanox.Configs.thanoxVersionCode
import tornaco.project.android.thanox.Configs.thanoxVersionName
import tornaco.project.android.thanox.log

buildscript {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
}

plugins {
    alias(libs.plugins.agp.lib) apply false
    alias(libs.plugins.agp.app) apply false

    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.dagger.hilt.android) apply false

    alias(libs.plugins.protobuf.gradle.plugin) apply false
    alias(libs.plugins.gmazzo.buildconfig) apply false

    alias(libs.plugins.gladed.androidgitversion) apply true
    alias(libs.plugins.diffplug.spotless) apply true
    alias(libs.plugins.kover) apply false

    alias(libs.plugins.compose.compiler) apply false

    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.firebase.crashlytics") version "3.0.2" apply false

    id("thanox-proj")
}

androidGitVersion {
    prefix = "v"
    codeFormat = "MNPBB"
    baseCode = 3030001
}

thanoxVersionCode = androidGitVersion.code()
thanoxVersionName = androidGitVersion.name()

printVersions()

task("printVersions") {
    printVersions()
}

fun printVersions() {
    log("thanoxVersionCode: $thanoxVersionCode")
    log("thanoxVersionName: $thanoxVersionName")
}


val androidSourceCompatibility by extra(JavaVersion.VERSION_17)
val androidTargetCompatibility by extra(JavaVersion.VERSION_17)

subprojects {
    log("subprojects: ${this.name}")
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }

    apply(plugin = "com.diffplug.spotless")

    tasks.withType<JavaCompile> {
        options.compilerArgs.addAll(
            arrayOf(
                "-Xmaxerrs",
                "1000"
            )
        )
        options.encoding = "UTF-8"
    }

    plugins.withType(com.android.build.gradle.api.AndroidBasePlugin::class.java) {
        extensions.configure(com.android.build.api.dsl.CommonExtension::class.java) {
            compileSdk = Configs.compileSdkVersion
            ndkVersion = Configs.ndkVersion

            externalNativeBuild {
                cmake {
                    version = "3.22.1+"
                }
            }

            defaultConfig {
                minSdk = Configs.minSdkVersion
                if (this is com.android.build.api.dsl.ApplicationDefaultConfig) {
                    targetSdk = Configs.targetSdkVersion
                    versionCode = Configs.thanoxVersionCode
                    versionName = Configs.thanoxVersionName
                    testInstrumentationRunner = Configs.testRunner
                }
            }

            lint {
                abortOnError = true
                checkReleaseBuilds = false
            }

            compileOptions {
                sourceCompatibility = androidSourceCompatibility
                targetCompatibility = androidTargetCompatibility
            }

        }
    }

    plugins.withType(JavaPlugin::class.java) {
        extensions.configure(JavaPluginExtension::class.java) {
            sourceCompatibility = androidSourceCompatibility
            targetCompatibility = androidTargetCompatibility
        }
    }

    buildDir = file("${outDir}${path.replace(":", File.separator)}")
}

rootProject.buildDir = outDir
tasks["clean"].doLast {
    delete(outDir)
}

