package me.khol.network

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

internal fun okHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
}

internal fun apiDescription(baseUrl: String, okHttpClient: OkHttpClient): ApiDescription {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .client(okHttpClient)
        .build()
        .create(ApiDescription::class.java)
}

internal fun apiInteractor(apiDescription: ApiDescription): ApiInteractor {
    return ApiInteractorImpl(apiDescription)
}

internal fun createApiInteractor(baseUrl: String): ApiInteractor {
    return apiInteractor(apiDescription(baseUrl, okHttpClient()))
}
