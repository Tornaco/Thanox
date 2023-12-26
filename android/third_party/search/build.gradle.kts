plugins {
    alias(libs.plugins.agp.lib)
}

android {
    namespace = "com.miguelcatalan.materialsearchview"

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
    implementation(libs.appcompat)
    implementation(libs.material)
}


