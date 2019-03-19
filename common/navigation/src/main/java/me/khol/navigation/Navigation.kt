package me.khol.navigation

import android.content.Intent

/**
 * Provides navigation intents to various modules of the application.
 */
interface Navigation {

    fun onboardingIntent(): Intent

    fun borrowerIntent(): Intent? = "me.khol.borrower.BorrowerActivity".loadIntentOrNull()

    fun investorIntent(): Intent? = "me.khol.investor.InvestorActivity".loadIntentOrNull()
}

