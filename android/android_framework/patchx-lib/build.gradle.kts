import tornaco.project.android.thanox.Libs

plugins {
    id("java")
}

dependencies {
    compileOnly(Libs.Others.lombok)
    annotationProcessor(Libs.Others.lombok)
    compileOnly(Libs.Others.xposedApi)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}