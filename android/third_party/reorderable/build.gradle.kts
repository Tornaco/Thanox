import tornaco.project.android.thanox.Configs.resPrefix


plugins {
    alias(libs.plugins.agp.lib)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "github.tornaco.thanos.android.third.party.reorderable"

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
        implementation(libs.kotlin.stdlib.jdk8)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.viewbinding)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material)
    implementation(libs.compose.ui.util)
    implementation(libs.navigation.compose)

}
