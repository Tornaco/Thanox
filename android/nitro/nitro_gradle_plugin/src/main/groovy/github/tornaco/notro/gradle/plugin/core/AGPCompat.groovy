/*
 * MIT License
 *
 * Copyright (c) 2019-present, iQIYI, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package github.tornaco.notro.gradle.plugin.core

import com.android.SdkConstants
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.internal.scope.GlobalScope
import com.android.utils.ILogger
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.util.VersionNumber

import java.lang.reflect.Constructor
import java.lang.reflect.Method

class AGPCompat {

    static String getMergedAssetsBaseDirCompat(Task mergeAssetsTask) {
        String mergedAssetsOutputDir
        try {
            mergedAssetsOutputDir = mergeAssetsTask.outputDir.asFile.get()
        } catch (Throwable ignored) {
            mergedAssetsOutputDir = mergeAssetsTask.outputDir
        }
        if (mergedAssetsOutputDir == null) {
            throw new GradleException("Can't read 'outputDir' from " + mergeAssetsTask == null ? null : mergeAssetsTask.class.name)
        }
        return mergedAssetsOutputDir
    }

    static File getAapt2FromMavenCompat(def variant) {
        try {
            Class class_Aapt2MavenUtils = Class.forName("com.android.build.gradle.internal.res.Aapt2MavenUtils")
            Method method_getAapt2FromMaven = class_Aapt2MavenUtils.getDeclaredMethod("getAapt2FromMaven", GlobalScope)
            method_getAapt2FromMaven.setAccessible(true)
            def versionAGP = VersionNumber.parse(getAndroidGradlePluginVersionCompat())
            def aapt2
            if(versionAGP >= VersionNumber.parse("4.1.0")){
                aapt2 = method_getAapt2FromMaven.invoke(null, variant.variantData.globalScope).singleFile
            }else{
                aapt2 = method_getAapt2FromMaven.invoke(null, variant.variantData.scope.globalScope).singleFile
            }
            return aapt2
        } catch (Throwable e) {
            throw new GradleException("Unable to obtain aapt2", e)
        }
    }

    static File getPackageApplicationDirCompat(Task packageApplicationTask) {
        try {
            return packageApplicationTask.outputDirectory
        } catch (Throwable ignored) {
            return packageApplicationTask.outputDirectory.asFile.get()
        }
    }

    private static String getMergedManifestDirCompat(Task processManifestTask) {
        def versionAGP = VersionNumber.parse(getAndroidGradlePluginVersionCompat())

        String manifestOutputBaseDir
        try {
            if (versionAGP >= VersionNumber.parse("4.1.0")) {
                manifestOutputBaseDir = processManifestTask.multiApkManifestOutputDirectory.get().getAsFile().getAbsolutePath()
            }else{
                manifestOutputBaseDir = processManifestTask.manifestOutputDirectory.asFile.get()
            }
        } catch (Throwable ignored) {
            manifestOutputBaseDir = processManifestTask.manifestOutputDirectory
        }
        if (manifestOutputBaseDir == null) {
            throw new GradleException("Can't read 'manifestOutputDirectory' form " + processManifestTask == null ? null : processManifestTask.class.name)
        }
        return manifestOutputBaseDir
    }

    static File getMergedManifestFileCompat(Task processManifestTask) {
        return new File(getMergedManifestDirCompat(processManifestTask), SdkConstants.ANDROID_MANIFEST_XML)
    }

    static File getBundleManifestDirCompat(Task processManifestTask, def versionAGP) {
        if (versionAGP < VersionNumber.parse("3.3.0")) {
            return null
        }

        File bundleManifestDir = null
        try {
            if (versionAGP >= VersionNumber.parse("4.1.0")) {
                bundleManifestDir = processManifestTask.bundleManifest.get().getAsFile()

            }else{
                bundleManifestDir = processManifestTask.bundleManifestOutputDirectory
            }
        } catch (Throwable e) {
            try {
                bundleManifestDir = processManifestTask.getBundleManifestOutputDirectory().get().getAsFile()
            } catch (Throwable e1) {

            }
        }
        if (bundleManifestDir == null) {
            throw new GradleException("Can't read 'bundleManifestOutputDirectory' form " + processManifestTask == null ? null : processManifestTask.class.name)
        }
        return bundleManifestDir
    }

    static File getMergeJniLibsBaseDirCompat(Task mergeJniLibsTask) {
        File mergeJniLibsDir
        try {
            mergeJniLibsDir = mergeJniLibsTask.outputStream.getRootLocation()
        } catch (Throwable e) {
            mergeJniLibsDir = mergeJniLibsTask.outputDir.asFile.get()
        }
        if (mergeJniLibsDir == null) {
            throw new GradleException("Can't read 'outputDir' form " + mergeJniLibsTask == null ? null : mergeJniLibsTask.class.name)
        }
        return mergeJniLibsDir
    }

    /**
     * get whether aapt2 is enabled
     */
    static boolean isAapt2EnabledCompat(Project project) {
        if (getAndroidGradlePluginVersionCompat() >= '3.3.0') {
            //when agp' version >= 3.3.0, use aapt2 default and no way to switch to aapt.
            return true
        }
        boolean aapt2Enabled = false
        try {
            def projectOptions = getProjectOptions(project)
            Object enumValue = resolveEnumValue("ENABLE_AAPT2", Class.forName("com.android.build.gradle.options.BooleanOption"))
            aapt2Enabled = projectOptions.get(enumValue)
        } catch (Exception e) {
            //ignored
        }
        return aapt2Enabled
    }

    /**
     * get android gradle plugin version by reflect
     */
    static String getAndroidGradlePluginVersionCompat() {
        String version = null
        try {
            Class versionModel = Class.forName("com.android.builder.model.Version")
            def versionFiled = versionModel.getDeclaredField("ANDROID_GRADLE_PLUGIN_VERSION")
            versionFiled.setAccessible(true)
            version = versionFiled.get(null)
        } catch (Exception ignored) {

        }
        return version
    }

    /**
     * get com.android.build.gradle.options.ProjectOptions obj by reflect
     */
    private static def getProjectOptions(Project project) {
        try {
            def basePlugin = project.getPlugins().hasPlugin('com.android.application') ? project.getPlugins().findPlugin('com.android.application') : project.getPlugins().findPlugin('com.android.library')
            return Class.forName("com.android.build.gradle.BasePlugin").getMetaClass().getProperty(basePlugin, 'projectOptions')
        } catch (Exception e) {
        }
        return null
    }

    /**
     * get enum obj by reflect
     */
    private static <T> T resolveEnumValue(String value, Class<T> type) {
        for (T constant : type.getEnumConstants()) {
            if (constant.toString().equalsIgnoreCase(value)) {
                return constant
            }
        }
        return null
    }

    static Task getProcessManifestTask(Project project, String variantName) {
        String mergeManifestTaskName = "process${variantName}Manifest"
        return project.tasks.findByName(mergeManifestTaskName)
    }

    static Task getProcessManifestForBundleTask(Project project, String variantName) {
        String mergeManifestTaskName = "processApplicationManifest${variantName}ForBundle"
        return project.tasks.findByName(mergeManifestTaskName)
    }

    static Task getAssemble(ApplicationVariant variant) {
        try {
            return variant.assembleProvider.get()
        } catch (Exception e) {
            return variant.assemble
        }
    }

    static Task getGenerateBuildConfigTask(Project project, String variantName) {
        String taskName = "generate${variantName}BuildConfig"
        return project.tasks.findByName(taskName)
    }

    static Task getR8Task(Project project, String variantName) {
        String r8TaskName = "transformClassesAndResourcesWithR8For${variantName}"
        def r8Task = project.tasks.findByName(r8TaskName)
        if (r8Task != null) {
            return r8Task
        }
        r8TaskName = "minify${variantName.capitalize()}WithR8"
        return project.tasks.findByName(r8TaskName)
    }

    static Task getMultiDexTask(Project project, String variantName) {
        String multiDexTaskName = "multiDexList${variantName}"
        String multiDexTaskTransformName = "transformClassesWithMultidexlistFor${variantName}"
        def multiDexTask = project.tasks.findByName(multiDexTaskName)
        if (multiDexTask == null) {
            multiDexTask = project.tasks.findByName(multiDexTaskTransformName)
        }
        return multiDexTask
    }

    static Task getProguardTask(Project project, String variantName) {
        String proguardTaskName = "transformClassesAndResourcesWithProguardFor${variantName}"
        def proguardTask = project.tasks.findByName(proguardTaskName)
        if (proguardTask != null) {
            return proguardTask
        }
        proguardTaskName = "minify${variantName.capitalize()}WithProguard"
        return project.tasks.findByName(proguardTaskName)
    }

    static Task getMergeAssetsTask(Project project, String variantName) {
        String mergeAssetsTaskName = "merge${variantName}Assets"
        return project.tasks.findByName(mergeAssetsTaskName)
    }

    static Task getPackageTask(Project project, String variantName) {
        String packageTaskName = "package${variantName}"
        return project.tasks.findByName(packageTaskName)
    }

    static Task getDexSplitterTask(Project project, String variantName) {
        String proguardTaskName = "transformDexWithDexSplitterFor${variantName}"
        return project.tasks.findByName(proguardTaskName)
    }

    static Task getMergeJniLibsTask(Project project, String variantName) {
        Task task = project.tasks.findByName("transformNativeLibsWithMergeJniLibsFor${variantName}")
        if (task == null) {
            task = project.tasks.findByName("merge${variantName}NativeLibs")
        }
        return task
    }

    /**
     * Create instance of class {@link com.android.builder.testing.api.DeviceProvider}
     */
    static Object createDeviceProviderCompat(File adbExecutable, int timeOutInMs, ILogger iLogger) {
        Class classDeviceProvider
        try {
            classDeviceProvider = Class.forName("com.android.builder.testing.ConnectedDeviceProvider")
        } catch (Throwable e) {
            classDeviceProvider = Class.forName("com.android.build.gradle.internal.testing.ConnectedDeviceProvider")
        }
        if (classDeviceProvider != null) {
            Constructor constructor = classDeviceProvider.getDeclaredConstructor(File.class, int.class, ILogger.class)
            constructor.setAccessible(true)
            return constructor.newInstance(adbExecutable, timeOutInMs, iLogger)
        }
        throw new GradleException("Qigsaw has not adapt this AGP version yet, class 'com.android.builder.testing.api.DeviceProvider' is not found!")
    }

}