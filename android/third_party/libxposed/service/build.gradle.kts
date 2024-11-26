plugins {
    alias(libs.plugins.agp.lib)
}

android {
    namespace = "io.github.libxposed.service"
}

dependencies {
    implementation(project(":third_party:libxposed:interface"))
    compileOnly("androidx.annotation:annotation:1.9.1")
}