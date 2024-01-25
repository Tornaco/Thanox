import tornaco.project.android.thanox.Configs.resPrefix

plugins {
    alias(libs.plugins.agp.lib)
}

android {
    namespace = "github.tornaco.android.thanox.module.activity.trampoline"
    buildFeatures {
        compose = false
        buildConfig = true
        aidl = true
        renderScript = false
        resValues = false
        shaders = false
        viewBinding = true
        dataBinding = true
    }

    resourcePrefix = resPrefix
}
dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.swiperefreshlayout)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.extensions)

    implementation(libs.guava.android)
    implementation(libs.gson)
    implementation(libs.rxjava)

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation(project(":modules:module_common"))
    implementation(project(":android_framework:base"))

    compileOnly(project(":annotation_processors:permission-requester-annotation"))
    annotationProcessor(project(":annotation_processors:permission-requester-compiler"))

    implementation(project(":modules:module_filepicker"))
    implementation(project(":third_party:recyclerview-fastscroll"))
    implementation(project(":third_party:search"))

}