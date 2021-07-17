package github.tornaco.notro.gradle.plugin.core

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class NitroPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def android = project.extensions.findByType(AppExtension)
        if (android != null) {
            android.registerTransform(new NitroPatcher(project))
        }
    }
}
