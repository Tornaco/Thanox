import tornaco.project.android.thanox.Configs.outDir
import tornaco.project.android.thanox.Configs.thanoxVersionCode
import tornaco.project.android.thanox.Configs.thanoxVersionName
import tornaco.project.android.thanox.log

buildscript {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        classpath(tornaco.project.android.thanox.ClassPaths.gradlePlugin)
        classpath(tornaco.project.android.thanox.ClassPaths.kotlinPlugin)
        classpath(tornaco.project.android.thanox.Libs.Hilt.gradlePlugin)
        classpath(tornaco.project.android.thanox.Libs.ThanoxInternal.nitroGradlePlugin)
    }
}

plugins {
    id("com.diffplug.spotless").version("5.7.0")
    id("com.gladed.androidgitversion").version("0.4.10")
    id("thanox-proj")
}

androidGitVersion {
    prefix = "v"
    codeFormat = "MMNNPP"
    baseCode = 3030143
}

thanoxVersionCode = androidGitVersion.code()
thanoxVersionName = androidGitVersion.name()

log("thanoxVersionCode: $thanoxVersionCode")
log("thanoxVersionName: $thanoxVersionName")

subprojects {
    log("subprojects: ${this.name}")
    repositories {
        google()
        mavenCentral()
        mavenLocal()
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

