package me.khol.navigation

import android.content.Intent

/**
 * Static navigation.
 *
 * TODO: Provide this via DI.
 */
lateinit var navigation: Navigation

/**
 * Provides navigation intents to various modules of the application.
 */
interface Navigation {

    fun onboardingIntent(): Intent

    fun borrowerIntent(): Intent? = "me.khol.borrower.BorrowerActivity".loadIntentOrNull()

    fun investorIntent(): Intent? = "me.khol.investor.InvestorActivity".loadIntentOrNull()
}

