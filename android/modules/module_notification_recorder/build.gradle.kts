import tornaco.project.android.thanox.Configs.resPrefix

plugins {
    alias(libs.plugins.agp.lib)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "github.tornaco.android.thanox.module.notification.recorder"

    buildFeatures {
        compose = true
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
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.constraint.layout)

    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.extensions)

    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.guava.android)
    implementation(libs.gson)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.viewbinding)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material)
    implementation(libs.navigation.compose)

    implementation(libs.glide.landscapist)
    implementation(libs.glide)
    kapt(libs.glide.compiler)

    implementation(libs.dagger.hilt.android)
    implementation(libs.dagger.hilt.navigation.compose)
    kapt(libs.dagger.hilt.android.compiler)
    implementation(libs.dagger.hilt.android)

    implementation(project(":modules:module_common"))
    implementation(project(":android_framework:base"))
    implementation(project(":android_framework:res"))

    compileOnly(project(":annotation_processors:permission-requester-annotation"))
    annotationProcessor(project(":annotation_processors:permission-requester-compiler"))
    add("kapt", project(":annotation_processors:permission-requester-compiler"))

    implementation(project(":modules:module_filepicker"))
    implementation(project(":third_party:recyclerview-fastscroll"))
    implementation(project(":third_party:search"))

    implementation(project(":third_party:dateformatter"))
}