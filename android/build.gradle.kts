import com.android.build.gradle.api.AndroidBasePlugin
import com.github.megatronking.stringfog.plugin.StringFogExtension
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
    dependencies {
        classpath(libs.stringfog.plugin)
        classpath(libs.stringfog.xor)
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
    id("com.google.firebase.crashlytics") version "3.0.3" apply false

    id("thanox-proj")
}

androidGitVersion {
    prefix = "v"
    codeFormat = "MNPBB"
}

// Make sure new code > released.
val legacyCodeBase = 2000000
// 2000.1.1 millis
val startOf2000 = 946684800000L
// +1 10min
val versionCodeByTime =
    ((System.currentTimeMillis() - startOf2000) / 1000 / 60 / 10).toInt() + legacyCodeBase
thanoxVersionCode = versionCodeByTime
thanoxVersionName = androidGitVersion.name()

printVersions()

task("printVersions") {
    printVersions()
}

fun printVersions() {
    log("thanoxVersionCode: $thanoxVersionCode")
    log("thanoxVersionName: $thanoxVersionName")
}


val androidSourceCompatibility by extra(JavaVersion.VERSION_21)
val androidTargetCompatibility by extra(JavaVersion.VERSION_21)

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

    project.afterEvaluate {
        val hasAndroidPlugin = project.plugins.hasPlugin(AndroidBasePlugin::class)
        log("Check publishing subproject: ${project.name}, hasAndroidPlugin: $hasAndroidPlugin")
        val fogModules = listOf("")
        if (fogModules.contains(project.name)) {
            log("Fogging: ${project.name}")
            project.plugins.apply("stringfog")

            project.configure<StringFogExtension> {
                implementation = "com.github.megatronking.stringfog.xor.StringFogImpl"
                enable = true
                kg = com.github.megatronking.stringfog.plugin.kg.RandomKeyGenerator()
                mode = com.github.megatronking.stringfog.plugin.StringFogMode.bytes
            }
            project.dependencies {
                add("implementation", libs.stringfog.xor)
            }
        }
    }

    buildDir = file("${outDir}${path.replace(":", File.separator)}")
}

rootProject.buildDir = outDir
tasks["clean"].doLast {
    delete(outDir)
}

