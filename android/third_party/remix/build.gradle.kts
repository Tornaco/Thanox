plugins {
    alias(libs.plugins.agp.lib)
}

android {
    namespace = "github.tornaco.android.thanos.icon.remix"

    buildFeatures {
        compose = false
        buildConfig = false
        aidl = false
        renderScript = false
        resValues = false
        shaders = false
        viewBinding = false
        dataBinding = false
    }
}