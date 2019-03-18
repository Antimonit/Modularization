package me.khol.intro

import me.khol.intro.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

fun loadKoinIntro() {
    loadKoinModules(
        viewModelModule
    )
}

val viewModelModule: Module = module {

    viewModel { LoginViewModel(context = get(), apiInteractor = get()) }
}
