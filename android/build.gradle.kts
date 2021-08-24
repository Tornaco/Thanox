import tornaco.project.android.thanox.Configs.outDir
import tornaco.project.android.thanox.Configs.thanoxVersionCode
import tornaco.project.android.thanox.Configs.thanoxVersionName
import tornaco.project.android.thanox.log

buildscript {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        jcenter()
        maven(url = "https://jitpack.io")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    }

    dependencies {
        classpath(tornaco.project.android.thanox.ClassPaths.gradlePlugin)
        classpath(tornaco.project.android.thanox.ClassPaths.kotlinPlugin)
        classpath(tornaco.project.android.thanox.Libs.Hilt.gradlePlugin)
        classpath(tornaco.project.android.thanox.Libs.ThanoxInternal.rhinoGradlePlugin)
        classpath(tornaco.project.android.thanox.Libs.ThanoxInternal.nitroGradlePlugin)
    }
}

plugins {
    id("com.diffplug.spotless").version("5.7.0")
    id("com.gladed.androidgitversion").version("0.4.10")
    id("ThanoxProjectBuildPlugin")
}

androidGitVersion {
    prefix = "v"
    codeFormat = "MMNNPP"
    baseCode = 3000000
    // 1000000
    // 191011
    // First rel version
    // 1191011
    // 2041540
}

thanoxVersionCode = androidGitVersion.code()
thanoxVersionName = androidGitVersion.name()

log("thanoxVersionCode: $thanoxVersionCode")
log("thanoxVersionName: $thanoxVersionName")

subprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        jcenter()
        maven(url = "https://jitpack.io")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    }

    apply(plugin = "com.diffplug.spotless")

    tasks.withType<JavaCompile> {
        options.compilerArgs.addAll(
            arrayOf(
                "-Xmaxerrs",
                "1000"))
        options.encoding = "UTF-8"
    }

    buildDir = file("${outDir}${path.replace(":", File.separator)}")
}

rootProject.buildDir = outDir
tasks["clean"].doLast {
    delete(outDir)
}

