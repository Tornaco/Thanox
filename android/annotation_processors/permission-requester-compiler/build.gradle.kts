plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":annotation_processors:permission-requester-annotation"))

    implementation("com.google.guava:guava:24.1-jre")
    implementation("com.squareup:javapoet:1.8.0")
}

