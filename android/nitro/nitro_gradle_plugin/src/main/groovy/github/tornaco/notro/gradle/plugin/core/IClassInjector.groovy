package github.tornaco.notro.gradle.plugin.core


import javassist.ClassPool
import org.gradle.api.Project

interface IClassInjector {

    void setProject(Project project)

    void setVariantDir(String variantDir)

    def name()

    def injectClass(ClassPool pool, String dir)
}