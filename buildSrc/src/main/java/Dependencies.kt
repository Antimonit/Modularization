object ApplicationId {
    const val id = "me.khol.modularization"
}

object Releases {
    const val versionCode = 12
    const val versionName = "1.0"
}

object Versions {
    const val gradle = "3.3.2"

    const val compileSdk = 28
    const val minSdk = 21
    const val targetSdk = 28

    const val kotlin = "1.3.21"
}

object Libraries {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.21"

    const val ktx = "androidx.core:core-ktx:1.0.0-alpha1"

    const val timber = "com.jakewharton.timber:timber:4.7.1"

    const val rxjava = "io.reactivex.rxjava2:rxjava:2.2.6"
    const val rxkotlin = "io.reactivex.rxjava2:rxkotlin:2.3.0"
    const val rxandroid = "io.reactivex.rxjava2:rxandroid:2.1.0"

    const val retrofit = "com.squareup.retrofit2:retrofit:2.5.0"
    const val rxjavaAdapter = "com.squareup.retrofit2:adapter-rxjava2:2.5.0"
    const val moshiConverter = "com.squareup.retrofit2:converter-moshi:2.5.0"
    const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:3.12.1"

    const val moshi = "com.squareup.moshi:moshi:1.8.0"

    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime:2.1.0-alpha02"
    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:2.1.0-alpha02"
    const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:2.1.0-alpha02"
    
    const val koinAndroid = "org.koin:koin-android:2.0.0-beta-1"
    const val koinViewModel = "org.koin:koin-androidx-viewmodel:2.0.0-beta-1"
}

object SupportLibraries {
    const val appcompat = "androidx.appcompat:appcompat:1.0.2"
    const val design = "com.google.android.material:material:1.0.0"
    const val cardview = "androidx.cardview:cardview:1.0.0"
    const val recyclerview = "androidx.recyclerview:recyclerview:1.0.0"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:1.1.3"
}

object GoogleLibraries {
    const val auth = "com.google.android.gms:play-services-auth:16.0.1"
    const val playCore = "com.google.android.play:core:1.3.7"
}

object TestLibraries {
    const val junit = "junit:junit:4.12"
    const val assertjCore = "org.assertj:assertj-core:3.12.0"

    const val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0"
    const val mockitoInline = "org.mockito:mockito-inline:2.24.5"

    const val runner = "androidx.test:runner:1.1.2-alpha02"
    const val espresso = "androidx.test.espresso:espresso-core:3.2.0-alpha02"
}
