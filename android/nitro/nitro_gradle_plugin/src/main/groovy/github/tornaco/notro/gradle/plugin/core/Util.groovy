package github.tornaco.notro.gradle.plugin.core

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.TransformInput
import com.android.build.gradle.internal.scope.GlobalScope
import com.android.builder.model.AndroidProject
import com.google.common.base.Charsets
import com.google.common.hash.Hashing
import github.tornaco.notro.gradle.plugin.core.compat.ScopeCompat
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.ZipFile

class Util {

    def static getClassPaths(Project project, GlobalScope globalScope,
                             Collection<TransformInput> inputs,
                             Set<String> includeJars,
                             Map<String, String> map) {
        def classpathList = []

        // android.jar
        classpathList.add(getAndroidJarPath(globalScope))

        getProjectClassPath(project, inputs, includeJars, map).each {
            classpathList.add(it)
        }

        Logger.log "Add ClassPath:"
        classpathList
    }

    /** 获取原始项目中的 ClassPath */
    def private static getProjectClassPath(Project project,
                                           Collection<TransformInput> inputs,
                                           Set<String> includeJars, Map<String, String> map) {
        def classPath = []
        def visitor = new ClassFileVisitor()
        def projectDir = project.getRootDir().absolutePath

        Logger.log "Unzip Jar ..."

        inputs.each { TransformInput input ->

            input.directoryInputs.each { DirectoryInput dirInput ->
                def dir = dirInput.file.absolutePath
                classPath << dir

                visitor.setBaseDir(dir)
                Files.walkFileTree(Paths.get(dir), visitor)
            }

            input.jarInputs.each { JarInput jarInput ->
                File jar = jarInput.file
                def jarPath = jar.absolutePath

                if (!jarPath.contains(projectDir)) {

                    String jarZipDir = project.getBuildDir().path +
                            File.separator + AndroidProject.FD_INTERMEDIATES + File.separator + "exploded-aar" +
                            File.separator + Hashing.sha1().hashString(jarPath, Charsets.UTF_16LE).toString() + File.separator + "class"
                    FileUtils.deleteDirectory(new File(jarZipDir))
                    if (unzip(jarPath, jarZipDir)) {
                        def jarZip = jarZipDir + ".jar"
                        includeJars << jarPath
                        classPath << jarZipDir
                        visitor.setBaseDir(jarZipDir)
                        Files.walkFileTree(Paths.get(jarZipDir), visitor)
                        map.put(jarPath, jarZip)
                    }

                } else {

                    includeJars << jarPath

                    String buildPath = project.getBuildDir().path +
                            File.separator + AndroidProject.FD_INTERMEDIATES + File.separator + "exploded-jar"
                    String buildJarPath = buildPath + jar.absolutePath.replace(projectDir, "")
                    File buildJarFile = new File(buildJarPath)
                    FileUtils.copyFile(new File(jarPath), buildJarFile)

                    map.put(jarPath, buildJarPath)

                    /* 将 jar 包解压，并将解压后的目录加入 classpath */
                    // println ">>> 解压Jar${jarPath}"
                    String jarZipDirPath = buildJarFile.getParent() + File.separatorChar + jar.getName().replace('.jar', '')
                    FileUtils.deleteDirectory(new File(jarZipDirPath))
                    if (unzip(buildJarPath, jarZipDirPath)) {
                        classPath << jarZipDirPath

                        visitor.setBaseDir(jarZipDirPath)
                        Files.walkFileTree(Paths.get(jarZipDirPath), visitor)
                    }
                    // 删除 jar
                    FileUtils.forceDelete(buildJarFile)
                }
            }
        }
        return classPath
    }

    /**
     * 编译环境中 android.jar 的路径
     */
    def static getAndroidJarPath(GlobalScope globalScope) {
        return ScopeCompat.getAndroidJar(globalScope)
    }

    /**
     * 压缩 dirPath 到 zipFilePath
     */
    def static zipDir(String dirPath, String zipFilePath) {
        new AntBuilder().zip(destfile: zipFilePath, basedir: dirPath)
    }

    /**
     * 解压 zipFilePath 到 目录 dirPath
     */
    private static boolean unzip(String zipFilePath, String dirPath) {
        // 若这个Zip包是空内容的（如引入了Bugly就会出现），则直接忽略
        if (isZipEmpty(zipFilePath)) {
            Logger.log "Zip file is empty! Ignore"
            return false
        }

        new AntBuilder().unzip(src: zipFilePath, dest: dirPath, overwrite: 'true')
        return true
    }

    def static lowerCaseAtIndex(String str, int index) {
        def len = str.length()
        if (index > -1 && index < len) {
            def arr = str.toCharArray()
            char c = arr[index]
            if (c >= 'A' && c <= 'Z') {
                c += 32
            }

            arr[index] = c
            arr.toString()
        } else {
            str
        }
    }

    static boolean isZipEmpty(String zipFilePath) {
        ZipFile z
        try {
            z = new ZipFile(zipFilePath)
            return z.size() == 0
        } finally {
            z.close()
        }
    }
}
