plugins {
    alias(libs.plugins.agp.lib)
}

android {
    namespace = "com.simplecityapps.recyclerview_fastscroll"

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
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.interpolator)
}


