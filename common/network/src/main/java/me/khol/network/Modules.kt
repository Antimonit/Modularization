package me.khol.network

import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

fun loadKoinNetwork() {
    loadKoinModules(
        networkModule
    )
}

val networkModule: Module = module {

    single { createApiInteractor(BASE_URL) }
}

private const val BASE_URL = "https://reqres.in/api/"