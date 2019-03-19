package me.khol.modularization

import android.app.Application
import android.content.Context
import com.google.android.play.core.splitcompat.SplitCompat
import me.khol.intro.loadKoinIntro
import me.khol.modularization.di.loadKoinApp
import me.khol.network.di.loadKoinNetwork
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun attachBaseContext(var1: Context) {
        super.attachBaseContext(var1)
        SplitCompat.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        startKoin { androidContext(this@App) }
        loadKoinApp()
        loadKoinIntro()
        loadKoinNetwork()
    }
}