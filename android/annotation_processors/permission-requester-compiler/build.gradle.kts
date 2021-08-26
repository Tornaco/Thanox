import tornaco.project.android.thanox.Libs

plugins {
    id("java")
}

dependencies {
    implementation(project(":annotation_processors:permission-requester-annotation"))

    implementation(Libs.Others.guavaJre)
    implementation(Libs.Others.javapoet)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}