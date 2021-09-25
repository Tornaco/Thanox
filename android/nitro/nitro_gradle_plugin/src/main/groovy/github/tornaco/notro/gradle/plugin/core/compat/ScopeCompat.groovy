package github.tornaco.notro.gradle.plugin.core.compat

import com.android.build.gradle.internal.scope.GlobalScope

class ScopeCompat {
    static def getAndroidJar(GlobalScope scope) {
        scope.getVersionedSdkLoader().get().androidJarProvider.get().absolutePath
    }
}