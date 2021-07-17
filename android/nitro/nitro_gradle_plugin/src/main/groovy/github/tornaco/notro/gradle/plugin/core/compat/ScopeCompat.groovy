package github.tornaco.notro.gradle.plugin.core.compat

import com.android.sdklib.IAndroidTarget

/**
 * @author hyongbai
 */
class ScopeCompat {

    static def getAdbExecutable(def scope) {
        final MetaClass scopeClz = scope.metaClass
        if (scopeClz.hasProperty(scope, "androidBuilder")) {
            return scope.androidBuilder.sdkInfo.adb
        }
        if (scopeClz.hasProperty(scope, "sdkComponents")) {
            return scope.sdkComponents.adbExecutableProvider.get()
        }
    }

    static def getAndroidJar(def scope) {
        final MetaClass scopeClz = scope.metaClass

        if (scopeClz.hasProperty(scope, "androidBuilder")) {
            return scope.getAndroidBuilder().getTarget().getPath(IAndroidTarget.ANDROID_JAR)
        }
        if (scopeClz.hasProperty(scope, "sdkComponents")) {
            return scope.sdkComponents.get().androidJarProvider.get().getAbsolutePath()
        }
    }
}