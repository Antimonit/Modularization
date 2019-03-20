package me.khol.modularization.di

import android.content.Context
import android.content.Intent
import me.khol.navigation.Navigation
import me.khol.onboarding.screen.OnboardingActivity
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

fun loadKoinApp() {
    loadKoinModules(
        navigationModule
    )
}

val navigationModule: Module = module {

    single<Navigation> { NavigationImpl(context = get()) }
}

class NavigationImpl(val context: Context) : Navigation {

    override fun onboardingIntent() = Intent(context, OnboardingActivity::class.java)
}