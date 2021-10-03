package tornaco.project.android.thanox

object ClassPaths {
    const val gradlePlugin = "com.android.tools.build:gradle:7.0.1"
    const val gradleApi = "com.android.tools.build:gradle-api:7.0.1"
    const val gradleBuilderModel = "com.android.tools.build:builder-model:7.0.1"
    const val androidToolsCommon = "com.android.tools:common:30.0.1"

    const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21"
}

object Compose {
    const val composeVersion = "1.1.0-alpha01"

    const val animation = "androidx.compose.animation:animation:$composeVersion"
    const val iconsExtended = "androidx.compose.material:material-icons-extended:$composeVersion"
    const val material = "androidx.compose.material:material:$composeVersion"
    const val runtime = "androidx.compose.runtime:runtime:$composeVersion"
    const val runtimeSaveAble = "androidx.compose.runtime:runtime-saveable:$composeVersion"
    const val toolingPreview = "androidx.compose.ui:ui-tooling-preview:$composeVersion"
    const val tooling = "androidx.compose.ui:ui-tooling:$composeVersion"
    const val ui = "androidx.compose.ui:ui:$composeVersion"
    const val uiUtil = "androidx.compose.ui:ui-util:$composeVersion"
    const val uiTest = "androidx.compose.ui:ui-test-junit4:$composeVersion"
    const val activityCompose = "androidx.activity:activity-compose:1.3.1"
    const val navigationCompose = "androidx.navigation:navigation-compose:2.4.0-alpha03"
    const val hiltNavigation = "androidx.hilt:hilt-navigation-compose:1.0.0-alpha03"
    const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07"

    const val coil = "io.coil-kt:coil-compose:1.3.2"

    const val composeMaterialIconsExtended =
        "androidx.compose.material:material-icons-extended:${composeVersion}"
}

object Tests {
    private const val junitVersion = "4.13.2"
    private const val junitKtx = "1.1.2"

    const val junit = "junit:junit:$junitVersion"
    const val junitKotlin = "androidx.test.ext:junit-ktx:$junitKtx"
    const val mockk = "io.mockk:mockk:1.12.0"
    const val mockito = "org.mockito:mockito-core:3.3.3"
    const val androidXCoreTest = "androidx.arch.core:core-testing:2.1.0"

    // https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/
    const val ktxCoroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.1"
}

object Libs {

    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.0"
    }

    object Coroutines {
        private const val version = "1.5.0-RC"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Hilt {
        private const val version = "2.37"
        const val library = "com.google.dagger:hilt-android:$version"
        const val googleAndroidCompiler = "com.google.dagger:hilt-android-compiler:$version"
        const val googleCompiler = "com.google.dagger:hilt-compiler:$version"
        const val testing = "com.google.dagger:hilt-android-testing:$version"
        const val gradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
    }

    object AndroidX {
        const val androidXCore = "androidx.core:core-ktx:1.3.2"
        const val appCompat = "androidx.appcompat:appcompat:1.3.1"
        const val material = "com.google.android.material:material:1.4.0"
        const val annotations = "androidx.annotation:annotation:1.1.0"
        const val recyclerview = "androidx.recyclerview:recyclerview:1.1.0"
        const val cardview = "androidx.cardview:cardview:1.1.0"
        const val v4 = "androidx.legacy:legacy-support-v4:1.1.0"
        const val constraint = "com.android.support.constraint:constraint-layout:1.1.3"
        const val coreUtils = "androidx.legacy:legacy-support-core-utils:1.1.0"
        const val preference = "androidx.preference:preference:1.1.0"
        const val interpolator = "androidx.interpolator:interpolator:1.0.0"
        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"


        const val lifeCycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:2.3.1"
        const val lifeCycleRuntime = "androidx.lifecycle:lifecycle-runtime:2.3.1"
        const val lifeCycleExt = "androidx.lifecycle:lifecycle-extensions:2.2.0"
        const val lifeCycleCommonJava8 = "androidx.lifecycle:lifecycle-common-java8:2.3.1"
        const val lifeCycleCompiler = "androidx.lifecycle:lifecycle-compiler:2.3.1"

        const val biometric = "androidx.biometric:biometric:1.2.0-alpha03"
        const val browser = "androidx.browser:browser:1.3.0"
    }

    object Room {
        private const val roomVersion = "2.4.0-alpha04"
        const val runtime = "androidx.room:room-runtime:$roomVersion"
        const val annotationProcessor = "androidx.room:room-compiler:$roomVersion"
        const val ktx = "androidx.room:room-ktx:$roomVersion"
        const val testing = "androidx.room:room-testing:$roomVersion"
    }

    object Others {
        const val timber = "com.jakewharton.timber:timber:4.7.1"
        const val coil = "io.coil-kt:coil-compose:1.3.2"
        const val retrofit = "com.squareup.retrofit2:retrofit:2.7.1"
        const val retrofitConverterGson = "com.squareup.retrofit2:converter-gson:2.7.1"
        const val retrofitAdapterRxJava2 = "com.squareup.retrofit2:adapter-rxjava2:2.7.1"
        const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:4.9.1"

        // for testing retrofit.
        const val mockWebServer = "com.squareup.okhttp3:mockwebserver:4.9.1"


        const val chart = "com.github.PhilJay:MPAndroidChart:v3.1.0"
        const val licensesDialog = "de.psdev.licensesdialog:licensesdialog:2.1.0"

        const val glide = "com.github.bumptech.glide:glide:4.9.0"
        const val glideCompiler = "com.github.bumptech.glide:compiler:4.9.0"

        const val xposedApi = "de.robv.android.xposed:api:82"

        const val lombok = "org.projectlombok:lombok:1.18.20"

        const val javapoet = "com.squareup:javapoet:1.8.0"

        const val guavaJre = "com.google.guava:guava:24.1-jre"
        const val guavaAndroid = "com.google.guava:guava:20.0@jar"

        const val gson = "com.google.code.gson:gson:2.8.5@jar"

        const val androidCommon = "com.github.Tornaco:AndroidCommon:v1.0"

        const val rxJava2 = "io.reactivex.rxjava2:rxjava:2.1.3"
        const val rxAndroid = "io.reactivex.rxjava2:rxandroid:2.0.1"

        const val apacheCommonIo = "commons-io:commons-io:2.6"
        const val apacheCommonCodec = "commons-codec:commons-codec:1.10"

        const val javassist = "org.javassist:javassist:3.26.0-GA"
    }

    object Rules {
        private const val version = "3.3.0"
        const val core = "org.jeasy:easy-rules-core:$version"
        const val mvel = "org.jeasy:easy-rules-mvel:$version"
        const val support = "org.jeasy:easy-rules-support:$version"
    }

    object Accompanist {
        private const val version = "0.18.0"
        const val navigationAnim =
            "com.google.accompanist:accompanist-navigation-animation:$version"

        const val appcompatTheme = "com.google.accompanist:accompanist-appcompat-theme:$version"
        const val systemUiController =
            "com.google.accompanist:accompanist-systemuicontroller:$version"
        const val pager =
            "com.google.accompanist:accompanist-pager:$version"
    }

    object Lottie {
        private const val version = "4.1.0"
        const val composeLottie = "com.airbnb.android:lottie-compose:${version}"
        const val lottie = "com.airbnb.android:lottie:${version}"
    }

    object ThanoxInternal {
        const val version = "1.0.0-SNAPSHOT"

        private const val groupRhino = "github.tornaco.android.thanos.rhino-verify"
        const val groupNitro = "github.tornaco.android.thanos.nitro"
        const val artifactNitro = "nitro_gradle_plugin"

        const val rhinoAnnotations = "$groupRhino:rhino_annotations:$version"
        const val rhinoGradlePlugin = "$groupRhino:rhino_plugin_stub_gen:$version"
        const val nitroGradlePlugin = "$groupNitro:$artifactNitro:$version"
    }
}
