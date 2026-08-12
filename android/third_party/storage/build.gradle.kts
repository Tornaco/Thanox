import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  id("com.android.library")
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "com.anggrayudi.storage"
  compileSdk = 36
  resourcePrefix = "ss_"

  defaultConfig {
    minSdk = 21
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  kotlin {
    compilerOptions {
      jvmTarget = JvmTarget.JVM_11
      // Support @JvmDefault
      freeCompilerArgs = listOf("-Xjvm-default=all", "-opt-in=kotlin.RequiresOptIn")
    }
  }
}

dependencies {
  api(libs.core.ktx)
  api(libs.appcompat)
  api(libs.androidx.activity)
  api(libs.androidx.fragment)
  api(libs.androidx.document.file)
  implementation(libs.lifecycle.runtime.ktx)

  implementation(libs.kotlinx.coroutines.android)
}
