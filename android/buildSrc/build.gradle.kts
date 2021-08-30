import org.gradle.kotlin.dsl.`kotlin-dsl`

repositories {
    mavenCentral()
    google()
}

plugins {
    `kotlin-dsl`
}


gradlePlugin {
    plugins {
        register("ThanoxProjectBuildPlugin") {
            id = "ThanoxProjectBuildPlugin"
            implementationClass = "tornaco.project.android.thanox.ThanoxProjectBuildPlugin"
        }

        register("CodeInjectionPlugin") {
            id = "CodeInjectionPlugin"
            implementationClass = "tornaco.project.android.thanox.plugin.CodeInjectionPlugin"
        }
    }
}

dependencies {
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.10.0.202012080955-r")
    implementation("org.javassist:javassist:3.28.0-GA")
    implementation("com.google.guava:guava:24.1-jre")
}