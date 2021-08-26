import tornaco.project.android.thanox.Libs

plugins {
    id("java")
    id("kotlin")
}

dependencies {
    compileOnly(Libs.Others.lombok)
    annotationProcessor(Libs.Others.lombok)
    compileOnly(Libs.Others.xposedApi)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}