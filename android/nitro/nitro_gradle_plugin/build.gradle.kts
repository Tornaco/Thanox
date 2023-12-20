import tornaco.project.android.thanox.ClassPaths.gradlePlugin
import tornaco.project.android.thanox.ClassPaths.gradleApi
import tornaco.project.android.thanox.ClassPaths.gradleBuilderModel
import tornaco.project.android.thanox.ClassPaths.androidToolsCommon
import tornaco.project.android.thanox.Libs
import tornaco.project.android.thanox.Libs.ThanoxInternal

plugins {
    id("java")
    id("groovy")
    id("maven-publish")
}

dependencies {
    implementation(gradleApi)
    implementation(gradlePlugin)
    implementation(gradleBuilderModel)
    implementation(androidToolsCommon)

    implementation("commons-io:commons-io:2.15.1")
    implementation("commons-codec:commons-codec:1.16.0")

    implementation(Libs.Others.guavaJre)
    implementation(Libs.Others.javassist)
    implementation(gradleApi())
    implementation(localGroovy())
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

publishing {
    repositories {
        maven {
            name = "ProjectRepo"
            url = uri(layout.projectDirectory.dir("prebuilt-repo"))
        }
    }

    publications {
        create<MavenPublication>("Local") {
            groupId = ThanoxInternal.groupNitro
            artifactId = ThanoxInternal.artifactNitro
            version = ThanoxInternal.version
            from(components["java"])
        }
    }
}