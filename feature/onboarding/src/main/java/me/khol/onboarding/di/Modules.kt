package me.khol.onboarding.di

import androidx.room.Room
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

fun loadKoinOnboarding() {
    loadKoinModules(
        persistenceModule
    )
}

val persistenceModule: Module = module {

    // TODO: Provide room
//    single { Room }
}
