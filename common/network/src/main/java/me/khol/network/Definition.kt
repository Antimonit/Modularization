package me.khol.network

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

internal object Definition {

    internal fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    internal fun provideApiDescription(okHttpClient: OkHttpClient): ApiDescription {
        return Retrofit.Builder()
            .baseUrl("https://reqres.in/api/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
            .client(okHttpClient)
            .build()
            .create(ApiDescription::class.java)
    }

    internal fun provideApiInteractor(apiDescription: ApiDescription): ApiInteractor {
        return ApiInteractorImpl(apiDescription)
    }
}

val apiInteractor: ApiInteractor by lazy {
    val okHttp = Definition.provideOkHttpClient()
    val description = Definition.provideApiDescription(okHttp)
    Definition.provideApiInteractor(description)
}