package tornaco.project.android.thanox

object ClassPaths {
    private const val gradleVersion = "7.2.1"

    const val gradlePlugin = "com.android.tools.build:gradle:$gradleVersion"
    const val gradleApi = "com.android.tools.build:gradle-api:$gradleVersion"
    const val gradleBuilderModel = "com.android.tools.build:builder-model:$gradleVersion"
    const val androidToolsCommon = "com.android.tools:common:30.0.1"

    const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0"
}

object Compose {
    const val composeVersion = "1.2.0"

    const val animation = "androidx.compose.animation:animation:$composeVersion"
    const val material = "androidx.compose.material:material:$composeVersion"
    const val material3 = "androidx.compose.material3:material3:1.0.0-alpha15"
    const val material3Adapter = "com.google.android.material:compose-theme-adapter-3:1.0.15"
    const val runtime = "androidx.compose.runtime:runtime:$composeVersion"
    const val runtimeSaveAble = "androidx.compose.runtime:runtime-saveable:$composeVersion"
    const val toolingPreview = "androidx.compose.ui:ui-tooling-preview:$composeVersion"
    const val tooling = "androidx.compose.ui:ui-tooling:$composeVersion"
    const val ui = "androidx.compose.ui:ui:$composeVersion"
    const val uiUtil = "androidx.compose.ui:ui-util:$composeVersion"
    const val uiTest = "androidx.compose.ui:ui-test-junit4:$composeVersion"
    const val activityCompose = "androidx.activity:activity-compose:1.5.1"
    const val navigationCompose = "androidx.navigation:navigation-compose:2.5.0"
    const val hiltNavigation = "androidx.hilt:hilt-navigation-compose:1.0.0"
    const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-compose:2.5.0"
    const val landscapistGlide = "com.github.skydoves:landscapist-glide:1.5.0"

    const val coil = "io.coil-kt:coil-compose:1.3.2"

    const val enro = "dev.enro:enro:1.15.1"
    const val enroApt = "dev.enro:enro-processor:1.15.1"

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
    const val androidXRunner = "androidx.test:runner:1.4.0"
    const val androidXTestJunit = "androidx.test.ext:junit:1.1.3"
    const val androidXTestEspresso = "androidx.test.espresso:espresso-core:3.4.0"

    // https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/
    const val ktxCoroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.1"

    const val uiAutomation = "androidx.test.uiautomator:uiautomator:2.2.0"
}

object Libs {

    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.0"
    }

    object Coroutines {
        private const val version = "1.5.0-RC"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Hilt {
        private const val version = "2.42"
        const val googleAndroidCompiler = "com.google.dagger:hilt-android-compiler:$version"
        const val googleCompiler = "com.google.dagger:hilt-compiler:$version"
        const val testing = "com.google.dagger:hilt-android-testing:$version"
        const val library = "com.google.dagger:hilt-android:$version"
        const val gradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
    }

    object AndroidX {
        const val androidXCore = "androidx.core:core-ktx:1.3.2"
        const val appCompat = "androidx.appcompat:appcompat:1.3.1"
        const val material = "com.google.android.material:material:1.5.0-rc01"
        const val annotations = "androidx.annotation:annotation:1.1.0"
        const val recyclerview = "androidx.recyclerview:recyclerview:1.1.0"
        const val cardview = "androidx.cardview:cardview:1.1.0"
        const val constraint = "com.android.support.constraint:constraint-layout:1.1.3"
        const val preference = "androidx.preference:preference:1.2.0-rc01"
        const val interpolator = "androidx.interpolator:interpolator:1.0.0"
        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
        const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:2.3.5"
        const val navigationUI = "androidx.navigation:navigation-ui:2.3.5"

        const val lifeCycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:2.4.1"
        const val lifeCycleRuntime = "androidx.lifecycle:lifecycle-runtime:2.4.1"
        const val lifeCycleCommon= "androidx.lifecycle:lifecycle-common:2.4.1"
        const val lifeCycleExt = "androidx.lifecycle:lifecycle-extensions:2.2.0"
        const val lifeCycleCompiler = "androidx.lifecycle:lifecycle-compiler:2.4.1"

        const val biometric = "androidx.biometric:biometric:1.2.0-alpha03"
        const val browser = "androidx.browser:browser:1.3.0"

        const val splash = "androidx.core:core-splashscreen:1.0.0-alpha01"

        const val paging3 = "androidx.paging:paging-runtime:3.1.0"

        const val workRuntimeKtx  = "androidx.work:work-runtime-ktx:2.7.1"
        const val workRuntime  = "androidx.work:work-runtime:2.7.1"
    }

    object Room {
        private const val roomVersion = "2.4.0"
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

        const val glide = "com.github.bumptech.glide:glide:4.13.0"
        const val glideCompiler = "com.github.bumptech.glide:compiler:4.13.0"

        const val xposedApi = "de.robv.android.xposed:api:82"

        const val lombok = "org.projectlombok:lombok:1.18.20"

        const val javapoet = "com.squareup:javapoet:1.8.0"

        const val guavaJre = "com.google.guava:guava:24.1-jre"
        const val guavaAndroid = "com.google.guava:guava:20.0@jar"

        const val gson = "com.google.code.gson:gson:2.8.5@jar"

        const val chucker = "com.github.chuckerteam.chucker:library:3.5.2"
        const val chuckerNoop = "com.github.chuckerteam.chucker:library-no-op:3.5.2"

        const val rxJava2 = "io.reactivex.rxjava2:rxjava:2.1.3"
        const val rxAndroid = "io.reactivex.rxjava2:rxandroid:2.0.1"

        const val apacheCommonIo = "commons-io:commons-io:2.6"
        const val apacheCommonCodec = "commons-codec:commons-codec:1.10"

        const val javassist = "org.javassist:javassist:3.26.0-GA"

        const val tinypinyin = "com.github.promeg:tinypinyin:2.0.3"

        const val listenablefutureEmpty =
            "com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava"

        // https://github.com/AmrDeveloper/CodeView
        const val codeView = "io.github.amrdeveloper:codeview:1.3.5"

        // https://github.com/zhaobozhen/LibChecker-Rules-Bundle
        const val libChecker = "com.github.zhaobozhen:LibChecker-Rules-Bundle:28"

        const val colorMath = "com.github.ajalt.colormath:colormath:3.2.1"
    }

    object Rules {
        private const val version = "4.1.0"
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
        const val insets =
            "com.google.accompanist:accompanist-insets:$version"
        const val insetsUi =
            "com.google.accompanist:accompanist-insets-ui:$version"
        const val swipeRefresh =
            "com.google.accompanist:accompanist-swiperefresh:$version"
        const val flowLayout =
            "com.google.accompanist:accompanist-flowlayout:$version"
    }

    object Lottie {
        private const val version = "4.1.0"
        const val composeLottie = "com.airbnb.android:lottie-compose:${version}"
        const val lottie = "com.airbnb.android:lottie:${version}"
    }

    object ThanoxInternal {
        const val version = "1.0.0-SNAPSHOT"

        const val groupRhino = "github.tornaco.android.thanos.rhino-verify"
        const val artifactRhinoAnnotations = "rhino_annotations"

        const val groupNitro = "github.tornaco.android.thanos.nitro"
        const val artifactNitro = "nitro_gradle_plugin"

        const val rhinoGradlePlugin = "$groupRhino:rhino_plugin_stub_gen:$version"

        const val nitroGradlePlugin = "$groupNitro:$artifactNitro:$version"
    }

    object RootLess {
        const val shizukuApi = "dev.rikka.shizuku:api:12.1.0"
        const val shizukuProvider = "dev.rikka.shizuku:provider:12.1.0"
    }
}
