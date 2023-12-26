plugins {
    alias(libs.plugins.agp.lib)
}

android {
    namespace = "com.stardust.autojs.apkbuilder"

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
    implementation(files("libs/tiny-sign-0.9.jar"))
    implementation(libs.commons.io)
    implementation(libs.commons.codec)
}

