import org.jetbrains.kotlin.daemon.common.toHexString
import tornaco.project.android.thanox.Configs
import tornaco.project.android.thanox.Configs.magiskModuleBuildDir
import tornaco.project.android.thanox.Configs.outDir
import tornaco.project.android.thanox.MagiskModConfigs.magiskModuleId
import tornaco.project.android.thanox.MagiskModConfigs.moduleAuthor
import tornaco.project.android.thanox.MagiskModConfigs.moduleDescription
import tornaco.project.android.thanox.MagiskModConfigs.moduleName
import tornaco.project.android.thanox.MagiskModConfigs.moduleVersion
import tornaco.project.android.thanox.MagiskModConfigs.moduleVersionCode
import tornaco.project.android.thanox.log
import java.nio.file.Files
import java.security.MessageDigest

plugins {
    alias(libs.plugins.agp.lib)
}

val libZygiskAbiFilters = listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")

android {
    namespace = "zygisk.thanox"

    defaultConfig {
        ndk {
            abiFilters.addAll(libZygiskAbiFilters)
        }

        externalNativeBuild {
            cmake {
                cppFlags.add("-std=c++17 -fno-exceptions -fno-rtti -fvisibility=hidden -fvisibility-inlines-hidden")
                arguments("-DMODULE_NAME:STRING=${tornaco.project.android.thanox.MagiskModConfigs.moduleLibraryName}")
            }
        }
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = Configs.cmakeVersion
        }

        log("Using cmake: ${Configs.cmakeVersion}")
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
        prefab = false
    }
    ndkVersion = Configs.ndkVersion
}

val magiskDir = magiskModuleBuildDir
val isWindows = org.gradle.internal.os.OperatingSystem.current().isWindows

fun calcSha256(file: File): String {
    val md = MessageDigest.getInstance("SHA-256")
    file.forEachBlock(4096) { buffer, bytesRead ->
        md.update(buffer, 0, bytesRead)
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
                }

                // Copy .git files manually since gradle exclude it by default
                Files.copy(
                    file("../template/magisk_module/.gitattributes").toPath(),
                    file("${magiskDir.path}/.gitattributes").toPath()
                )

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

            val nativeOutDir =
                file(layout.buildDirectory.file("intermediates/library_and_local_jars_jni/${variant.name}/copyReleaseJniLibsProjectAndLocalJars/jni"))
            log("nativeOutDir: $nativeOutDir")
            log("nativeOutDir list: ${nativeOutDir.list()?.map { it }}")

            doLast {
                // copy libs
                libZygiskAbiFilters.forEach { abi ->
                    log("Copying lib for abi: $abi")
                    copy {
                        from("$nativeOutDir/$abi")
                        into("$magiskDir/libzygisk")
                        exclude("**/*.txt")
                        rename { name ->
                            val destLibName = "$abi.so"
                            log("copy libzygisk file name: $name to: $destLibName")
                            destLibName
                        }
                    }
                }
                // copy jars
                copy {
                    val dexOutDir = file("${magiskDir}/system/framework/")
                    from(file("$outDir/android_framework/patch-magisk/bridge-dex-app/outputs/thanox-bridge.jar"))
                    into(file("$dexOutDir"))
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