import tornaco.project.android.thanox.Libs

plugins {
    id("java")
    id("groovy")
}

dependencies {
    // https://mvnrepository.com/artifact/com.android.tools.build/gradle/
    implementation("com.android.tools.build:gradle:4.1.3")
    implementation("commons-io:commons-io:2.6")

    implementation(Libs.Others.javassist)
    implementation(gradleApi())
    implementation(localGroovy())
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

apply(from = "../publish_packages.gradle")