/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("UnstableApiUsage")

package tornaco.project.android.thanox.plugin

import com.google.common.io.ByteStreams
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.transform.*
import org.gradle.api.attributes.Attribute
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.registerTransform
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.zip.Deflater
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

const val DOT_CLASS = ".class"

@ExperimentalStdlibApi
class CodeInjectionPlugin : Plugin<Project> {
    override fun apply(project: Project) = project.applyPlugin()

    private fun Project.applyPlugin() {
        dependencies {
            attributesSchema {
                attribute(injected)
            }
            artifactTypes.getByName("jar") {
                attributes.attribute(injected, false)
            }
        }

        configurations.all {
            afterEvaluate {
                if (isCanBeResolved) {
                    attributes.attribute(injected, true)
                }
            }
        }

        dependencies {
            registerTransform(CodeInjectionTransform::class) {
                from.attribute(injected, false).attribute(artifactType, "jar")
                to.attribute(injected, true).attribute(artifactType, "jar")

                parameters {
                    jarFileNamesToInject = setOf("services.jar")
                }
            }
        }

    }
}

internal val artifactType: Attribute<String> = Attribute.of("artifactType", String::class.java)
internal val injected: Attribute<Boolean> =
    Attribute.of("codeInjected", Boolean::class.javaObjectType)

@CacheableTransform
abstract class CodeInjectionTransform : TransformAction<CodeInjectionTransform.Parameters> {

    interface Parameters : TransformParameters {
        @get:Input
        var jarFileNamesToInject: Set<String>
    }

    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    @get:InputArtifact
    abstract val inputArtifact: Provider<FileSystemLocation>

    override fun transform(outputs: TransformOutputs) {
        val fileName = inputArtifact.get().asFile.name
        println("CodeInjection: fileName: $fileName")
        if (parameters.jarFileNamesToInject.contains(fileName)) {
            val nameWithoutExtension = fileName.substring(0, fileName.length - 4)
            instrumentClassesFromJarToJar(
                inputArtifact.get().asFile,
                outputs.file("${nameWithoutExtension}-injected.jar"))
        }
    }

    private fun instrumentClassesFromJarToJar(inputJarFile: File, outputJarFile: File) {
        println("instrumentClassesFromJarToJar: $inputJarFile --> $outputJarFile")
        ZipOutputStream(BufferedOutputStream(FileOutputStream(outputJarFile))).use { outputJar ->
            outputJar.setLevel(Deflater.NO_COMPRESSION)
            ZipFile(inputJarFile).use { inputJar ->
                val entries = inputJar.entries()
                while (entries.hasMoreElements()) {
                    val entry = entries.nextElement()
                    instrumentClassToJar(entry, outputJar) {
                        inputJar.getInputStream(entry)
                    }
                }
            }
        }
    }

    private fun saveEntryToJar(
        entryName: String,
        byteArray: ByteArray,
        jarOutputStream: ZipOutputStream,
    ) {
        val entry = ZipEntry(entryName)
        entry.time = 0
        jarOutputStream.putNextEntry(entry)
        jarOutputStream.write(byteArray)
        jarOutputStream.closeEntry()
    }

    private fun instrumentClassToJar(
        entry: ZipEntry,
        jarOutputStream: ZipOutputStream,
        classInputStream: () -> InputStream,
    ) {
        val entryName = entry.name
        if (!entryName.endsWith(DOT_CLASS)) {
            classInputStream.invoke().use {
                saveEntryToJar(
                    entryName,
                    ByteStreams.toByteArray(it),
                    jarOutputStream
                )
            }
            return
        }

        val splitName = entryName.split("/")
        val packageName = splitName.subList(0, splitName.size - 1).joinToString(".")
        val className = splitName.last().removeSuffix(DOT_CLASS)

        var instrumentedByteArray = doInstrumentClass(
            packageName = packageName,
            className = className,
            classInputStream = classInputStream
        )
        if (instrumentedByteArray == null) {
            classInputStream.invoke().use {
                instrumentedByteArray = ByteStreams.toByteArray(it)
            }
        }
        saveEntryToJar(entryName, instrumentedByteArray!!, jarOutputStream)
    }

    private fun doInstrumentClass(
        packageName: String,
        className: String,
        classInputStream: () -> InputStream,
    ): ByteArray? {
        val classFullName = "$packageName.$className"
        val classInternalName = classFullName.replace('.', '/')
        println("doInstrumentClass: $classInternalName")
        return classInputStream().readAllBytes()
    }


}
