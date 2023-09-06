plugins {
    id("java")
}

dependencies {
    compileOnly(tornaco.project.android.thanox.Libs.Others.xposedApi)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}