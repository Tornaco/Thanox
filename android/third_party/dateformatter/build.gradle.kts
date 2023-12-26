plugins {
    alias(libs.plugins.agp.lib)
}

android {
    namespace = "si.virag.fuzzydateformatter"

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
}

dependencies {
}


