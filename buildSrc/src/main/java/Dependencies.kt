object ApplicationId {
    const val id = "me.khol.namingishard"
}

object Modules {
    const val app = ":app"
    const val navigation = ":navigation"

    const val onboarding = ":onboarding"
    const val borrower = ":borrower"
    const val investor = ":investor"

    const val cache = ":common:cache"
    const val network = ":common:network"

    const val presentation = ":common:presentation"

    const val home = ":features:home"
    const val login = ":features:login"
    const val sample = ":sample"
}

object Releases {
    const val versionCode = 1
    const val versionName = "1.0"
}

object Versions {
    const val gradle = "3.3.2"

    const val compileSdk = 28
    const val minSdk = 21
    const val targetSdk = 28

    const val googleAuth = "16.0.1"

    const val googleServices = "4.2.0"

    const val firebaseAuth = "16.0.4"
    const val firebaseCore = "16.0.4"

    const val fabric = "1.27.1"

    const val appcompat = "1.0.2"
    const val design = "1.0.0"
    const val cardview = "1.0.0"
    const val recyclerview = "1.0.0"
    const val constraintlayout = "1.1.3"
    const val maps = "15.0.1"

    const val ktx = "1.0.0-alpha1"

    const val kotlin = "1.3.21"
    const val timber = "4.7.1"
    const val rxjava = "2.2.6"
    const val rxkotlin = "2.3.0"
    const val retrofit = "2.5.0"
    const val loggingInterceptor = "3.12.1"
    const val rxpaper = "1.4.0"
    const val paperdb = "2.6"
    const val moshi = "1.8.0"
    const val lifecycle = "2.0.0"
    const val leakCanary = "1.6.3"
    const val crashlytics = "2.9.9"
    const val koin = "2.0.0-beta-1"

    const val playCore = "1.3.7"

    const val junit = "4.12"
    const val assertjCore = "3.12.0"
    const val mockitoKotlin = "2.1.0"
    const val mockitoInline = "2.24.5"
}

object Libraries {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    const val ktx = "androidx.core:core-ktx:${Versions.ktx}"

    const val maps = "com.google.android.gms:play-services-maps:${Versions.maps}"

    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    const val rxjava = "io.reactivex.rxjava2:rxjava:${Versions.rxjava}"
    const val rxkotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxkotlin}"

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val rxjavaAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    const val moshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.loggingInterceptor}"

    const val paperdb = "io.paperdb:paperdb:${Versions.paperdb}"
    const val rxpaper = "com.github.pakoito:RxPaper2:${Versions.rxpaper}"
    const val moshi = "com.squareup.moshi:moshi:${Versions.moshi}"

    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"

    const val leakCanaryAndroid = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"
    const val leakCanaryAndroidNoOp = "com.squareup.leakcanary:leakcanary-android-no-op:${Versions.leakCanary}"
    const val leakCanaryAndroidSupportFragment = "com.squareup.leakcanary:leakcanary-support-fragment:${Versions.leakCanary}"

    const val crashlytics = "com.crashlytics.sdk.android:crashlytics:${Versions.crashlytics}"

    const val koinAndroid = "org.koin:koin-android:${Versions.koin}"
    const val koinViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
}

object SupportLibraries {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val design = "com.google.android.material:material:${Versions.design}"
    const val cardview = "androidx.cardview:cardview:${Versions.cardview}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
}

object GoogleLibraries {
    const val auth = "com.google.android.gms:play-services-auth:${Versions.googleAuth}"
    const val playCore = "com.google.android.play:core:${Versions.playCore}"
}

object FirebaseLibraries {
    const val auth = "com.google.firebase:firebase-auth:${Versions.firebaseAuth}"
    const val core = "com.google.firebase:firebase-core:${Versions.firebaseCore}"
}

object TestLibraries {
    const val junit = "junit:junit:${Versions.junit}"
    const val assertjCore = "org.assertj:assertj-core:${Versions.assertjCore}"
    const val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockitoKotlin}"
    const val mockitoInline = "org.mockito:mockito-inline:${Versions.mockitoInline}"
    const val lifecycleTesting = "androidx.arch.core:core-testing:${Versions.lifecycle}"
    const val runner = "androidx.test:runner:1.1.2-alpha02"
    const val espresso = "androidx.test.espresso:espresso-core:3.2.0-alpha02"
}
