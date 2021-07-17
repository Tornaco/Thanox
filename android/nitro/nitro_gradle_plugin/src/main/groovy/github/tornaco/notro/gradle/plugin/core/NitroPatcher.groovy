package github.tornaco.notro.gradle.plugin.core

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.build.gradle.internal.plugins.AppPlugin
import com.android.build.gradle.internal.plugins.BasePlugin
import com.android.utils.FileUtils
import com.google.common.collect.Lists
import javassist.ClassPool
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.GradleException
import org.gradle.api.Project

import java.util.regex.Pattern

class NitroPatcher extends Transform {

    private static final String PLUGIN_ACTIVITY_CLASS = "github.tornaco.android.nitro.framework.plugin.PluginActivity"
    private static final List<String> ACTIVITY_CLASS_TO_REPLACE = Lists.newArrayList(
            "android.app.Activity",
            "androidx.appcompat.app.AppCompatActivity")

    private Project project

    private globalScope

    def includeJars = [] as Set
    def map = [:]

    NitroPatcher(Project project) {
        this.project = project
        def appPlugin = project.plugins.getPlugin(AppPlugin)
        def globalScope = BasePlugin.metaClass.getProperty(appPlugin, "globalScope")
        this.globalScope = globalScope
    }

    @Override
    String getName() {
        return getClass().getSimpleName()
    }

    @Override
    void transform(TransformInvocation transformInvocation)
            throws TransformException, InterruptedException, IOException {
        File rootLocation = transformInvocation.outputProvider.folderUtils.getRootFolder()
        if (rootLocation == null) {
            throw new GradleException("can't get transform root location")
        }
        Logger.log "rootLocation: ${rootLocation}"
        def variantDir = rootLocation.absolutePath.split(getName() + Pattern.quote(File.separator))[1]
        Logger.log "variantDir: ${variantDir}"
        def injectors = includedInjectors(variantDir)
        doTransform(transformInvocation.inputs, transformInvocation.outputProvider, injectors)
    }

    def doTransform(Collection<TransformInput> inputs,
                    TransformOutputProvider outputProvider,
                    def injectors) {

        Object pool = initClassPool(inputs)

        Injectors.values().each {
            if (it.nickName in injectors) {
                doInject(inputs, pool, it.injector)
            }
        }

        /* 重打包 */
        repackage()

        /* 拷贝 class 和 jar 包 */
        copyResult(inputs, outputProvider)
    }

    def doInject(Collection<TransformInput> inputs,
                 ClassPool pool,
                 IClassInjector injector) {
        try {
            inputs.each { TransformInput input ->
                input.directoryInputs.each {
                    handleDir(pool, it, injector)
                }
                input.jarInputs.each {
                    handleJar(pool, it, injector)
                }
            }
        } catch (Throwable t) {
            throw new GradleException("Inject error.", t)
        }
    }

    def includedInjectors(String variantDir) {
        def injectors = []
        Injectors.values().each {
            it.injector.setProject(project)
            it.injector.setVariantDir(variantDir)
            injectors << it.nickName
        }
        injectors
    }

    def repackage() {
        Logger.log 'Repackage...'
        includeJars.each {
            File jar = new File(it)
            String JarAfterZip = map.get(jar.getParent() + File.separatorChar + jar.getName())
            String dirAfterUnzip = JarAfterZip.replace('.jar', '')
            Util.zipDir(dirAfterUnzip, JarAfterZip)
            org.apache.commons.io.FileUtils.deleteDirectory(new File(dirAfterUnzip))
        }
    }

    def copyResult(def inputs, def outputs) {
        inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput dirInput ->
                copyDir(outputs, dirInput)
            }
            input.jarInputs.each { JarInput jarInput ->
                copyJar(outputs, jarInput)
            }
        }
    }

    def handleJar(ClassPool pool, JarInput input, IClassInjector injector) {
        File jar = input.file
        if (jar.absolutePath in includeJars) {
            Logger.log "Handle Jar: ${jar.absolutePath}"
            String dirAfterUnzip = map.get(jar.getParent() + File.separatorChar + jar.getName()).replace('.jar', '')
            injector.injectClass(pool, dirAfterUnzip)
        }
    }

    def handleDir(ClassPool pool, DirectoryInput input, IClassInjector injector) {
        Logger.log "Handle Dir: ${input.file.absolutePath}"
        injector.injectClass(pool, input.file.absolutePath)
    }

    def copyDir(TransformOutputProvider output, DirectoryInput input) {
        File dest = output.getContentLocation(input.name, input.contentTypes, input.scopes, Format.DIRECTORY)
        FileUtils.copyDirectory(input.file, dest)
    }

    def copyJar(TransformOutputProvider output, JarInput input) {
        File jar = input.file
        String jarPath = map.get(jar.absolutePath)
        if (jarPath != null) {
            jar = new File(jarPath)
        }

        String destName = input.name
        def hexName = DigestUtils.md5Hex(jar.absolutePath)
        if (destName.endsWith('.jar')) {
            destName = destName.substring(0, destName.length() - 4)
        }
        File dest = output.getContentLocation(destName + '_' + hexName, input.contentTypes, input.scopes, Format.JAR)
        FileUtils.copyFile(jar, dest)
    }

    def initClassPool(Collection<TransformInput> inputs) {
        def pool = new ClassPool(true)
        includeJars.clear()
        Util.getClassPaths(project, globalScope, inputs, includeJars, map).each {
            Logger.log "$it"
            pool.insertClassPath(it)
        }
        pool
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }
}