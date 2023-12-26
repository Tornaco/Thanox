plugins {
    alias(libs.plugins.agp.lib)
}

android {
    namespace = "io.github.libxposed.service"

    buildFeatures {
        buildConfig = false
        resValues = false
        aidl = true
    }
}