import org.jetbrains.kotlin.daemon.common.toHexString
import tornaco.project.android.thanox.Configs
import tornaco.project.android.thanox.Configs.outDir
import tornaco.project.android.thanox.MagiskModConfigs
import tornaco.project.android.thanox.MagiskModConfigs.magiskModuleId
import tornaco.project.android.thanox.MagiskModConfigs.moduleAuthor
import tornaco.project.android.thanox.MagiskModConfigs.moduleDescription
import tornaco.project.android.thanox.MagiskModConfigs.moduleLibraryName
import tornaco.project.android.thanox.MagiskModConfigs.moduleMinRiruApiVersion
import tornaco.project.android.thanox.MagiskModConfigs.moduleMinRiruVersionName
import tornaco.project.android.thanox.MagiskModConfigs.moduleName
import tornaco.project.android.thanox.MagiskModConfigs.moduleRiruApiVersion
import tornaco.project.android.thanox.MagiskModConfigs.moduleVersion
import tornaco.project.android.thanox.MagiskModConfigs.moduleVersionCode
import tornaco.project.android.thanox.log
import java.nio.file.Files
import java.security.MessageDigest

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

        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
        }

        externalNativeBuild {
            cmake {
                cppFlags.add("-std=c++11")
                arguments("-DMODULE_NAME:STRING=${MagiskModConfigs.moduleLibraryName}",
                    "-DRIRU_MODULE_API_VERSION=${MagiskModConfigs.moduleRiruApiVersion}",
                    "-DRIRU_MODULE_VERSION=${MagiskModConfigs.moduleVersionCode}",
                    "-DRIRU_MODULE_VERSION_NAME:STRING=\"${MagiskModConfigs.moduleVersion.toString()}\"",
                    "-DRIRU_MODULE_MIN_API_VERSION=${MagiskModConfigs.moduleMinRiruApiVersion}")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.10.2"
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
        prefab = true
    }
}

repositories {
    mavenLocal()
    jcenter()
    maven(url = "https://dl.bintray.com/rikkaw/Libraries")
}


dependencies {
    // This is prefab aar which contains "riru.h"
    // If you want to use older versions of AGP,
    // you can copy this file from https://github.com/RikkaApps/Riru/blob/master/riru/src/main/cpp/include_riru/riru.h

    // The default version of prefab in AGP has problem to process header only package,
    // you may have to add android.prefabVersion=1.1.2 in your gradle.properties.
    // See https://github.com/google/prefab/issues/122

    implementation("dev.rikka.ndk:riru:25.0.0")
}


val magiskDir = file("$outDir/magisk_module")
val isWindows = org.gradle.internal.os.OperatingSystem.current().isWindows

fun calcSha256(file: File): String {
    val md = MessageDigest.getInstance("SHA-256")
    file.forEachBlock(4096) { buffer, bytesRead ->
        md.update(buffer, 0, bytesRead);
    }
    return md.digest().toHexString()
}

afterEvaluate {
    android.libraryVariants.forEach { variant ->
        val variantCapped = variant.name.capitalize()
        val variantLowered = variant.name.toLowerCase()
        val zipName = "${magiskModuleId.replace('_', '-')}-${moduleVersion}-${variantLowered}.zip"

        task("prepareMagiskFiles${variantCapped}") {
            group = "magisk"
            dependsOn("assemble$variantCapped")

            doFirst {
                delete { delete(magiskDir) }

                copy {
                    from("../template/magisk_module")
                    into(magiskDir.path)
                    exclude("riru.sh")
                }

                copy {
                    from("../template/magisk_module")
                    into(magiskDir.path)
                    include("riru.sh")
                    filter { line ->
                        line.replace("%%%RIRU_MODULE_API_VERSION%%%",
                            moduleRiruApiVersion.toString())
                            .replace("%%%RIRU_MODULE_MIN_API_VERSION%%%",
                                moduleMinRiruApiVersion.toString())
                            .replace("%%%RIRU_MODULE_MIN_RIRU_VERSION_NAME%%%",
                                moduleMinRiruVersionName)
                            .replace("%%%RIRU_MODULE_LIB_NAME%%%", moduleLibraryName)
                    }
                }

                // Copy .git files manually since gradle exclude it by default
                Files.copy(file("../template/magisk_module/.gitattributes").toPath(),
                    file("${magiskDir.path}/.gitattributes").toPath())

                val moduleProp = mapOf(
                    "id" to magiskModuleId,
                    "name" to moduleName,
                    "version" to moduleVersion,
                    "versionCode" to moduleVersionCode.toString(),
                    "author" to moduleAuthor,
                    "description" to moduleDescription,
                )

                var modulePropText = ""
                moduleProp.forEach { (k, v) -> modulePropText += "$k=$v\n" }
                modulePropText = modulePropText.trim()
                file("$magiskDir/module.prop").writeText(modulePropText)
            }

            val nativeOutDir = file("$buildDir/intermediates/cmake/${variant.name}/obj")
            log("nativeOutDir: $nativeOutDir")
            log("nativeOutDir: ${nativeOutDir.list()?.map { it }}")

            doLast {
                copy {
                    from("$nativeOutDir")
                    into("$magiskDir/lib")
                    exclude("**/*.txt")
                }

                copy {
                    val dexOutDir = file("${magiskDir}/system/framework/")
                    from(file("$outDir/android_framework/patch-magisk/bridge-dex-app/outputs/thanox-bridge.jar"))
                    into(file("${dexOutDir}"))
                }

                // generate sha1sum
                fileTree("$magiskDir").matching {
                    exclude("README.md", "META-INF")
                }.visit {
                    if (isDirectory.not()) {
                        file(file.path + ".sha256sum").writeText(calcSha256(file))
                    }
                }
            }
        }


        task("zip${variantCapped}", type = Zip::class) {
            group = "magisk"
            dependsOn("prepareMagiskFiles${variantCapped}")
            from(magiskDir)
            archiveFileName.set(zipName)
            destinationDirectory.set(outDir)
        }

        variant.assembleProvider.get().finalizedBy("zip${variantCapped}")
    }

}