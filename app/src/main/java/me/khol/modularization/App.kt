package me.khol.modularization

import android.app.Application
import android.content.Context
import android.content.Intent
import com.google.android.play.core.splitcompat.SplitCompat
import me.khol.intro.loadKoinIntro
import me.khol.navigation.Navigation
import me.khol.navigation.navigation
import me.khol.network.loadKoinNetwork
import me.khol.onboarding.OnboardingActivity
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun attachBaseContext(var1: Context) {
        super.attachBaseContext(var1)
        SplitCompat.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        navigation = object : Navigation {

            override fun onboardingIntent() = Intent(this@App, OnboardingActivity::class.java)
        }

        startKoin { androidContext(this@App) }
        loadKoinIntro()
        loadKoinNetwork()
    }
}