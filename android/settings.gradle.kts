@file:Suppress("UnstableApiUsage")

rootProject.name = "Thanox"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        
        maven(url = "https://maven.aliyun.com/repository/public/")
        maven(url = "https://jitpack.io")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        jcenter()
    }
}



include(":app")
// include(":app_shizuku")
include(":shortcut_stub")

include(":annotation_processors:xposed_hook_annotation")
include(":annotation_processors:xposed_hook_compiler")
include(":annotation_processors:permission-requester-annotation")
include(":annotation_processors:permission-requester-compiler")

// Modules.
include(":modules:module_ops")
include(":modules:module_ops2")
include(":modules:module_profile")
include(":modules:module_locker")
include(":modules:module_common")
include(":modules:module_filepicker")
include(":modules:module_push_message_delegate")

include(":third_party:dateformatter")
include(":third_party:recyclerview-fastscroll")
include(":third_party:search")
include(":third_party:apkbuilder")
include(":third_party:time-duration-picker")
include(":third_party:remix")
include(":third_party:compose-color-picker")
include(":third_party:reorderable")

// TODO Change to mvn local deps
include(":third_party:libxposed:interface")
include(":third_party:libxposed:service")
include(":third_party:libxposed:api")

include(":android_framework:base")
include(":android_framework:res")
include(":android_framework:patchx-lib")
include(":android_framework:hidden-api")
include(":android_framework:dex-maker")
include(":android_framework:patch-common")

include(":android_framework:patch-magisk:module")
include(":android_framework:patch-magisk:bridge")
include(":android_framework:patch-magisk:bridge-dex-app")
include(":android_framework:patch-magisk:patch-framework")

//
//include(":shortcut_stub")

val internalProjects = listOf(
    ":modules:module_donate",
    ":modules:module_feature_launcher",
    ":third_party:sec_net",
    ":android_framework:tests",
    ":android_framework:db",
    ":android_framework:services",
    ":android_framework:services-29",
    ":android_framework:services-30",
    ":android_framework:services-31",
    ":android_framework:patchx-entry",
    ":android_framework:patchx",
    ":android_framework:patchx-29",
    ":android_framework:patchx-30",
    ":android_framework:patchx-31",
    // Shizuku
    ":shizuku:core",
    ":shizuku:services",
    ":shizuku:ui",
)

internalProjects.forEach {
    println("internalProject: $it")
    include(it)

    val dir = file("internal/Thanox-Internal${it.replace(":", File.separator)}")
    project(it).projectDir = dir

}
