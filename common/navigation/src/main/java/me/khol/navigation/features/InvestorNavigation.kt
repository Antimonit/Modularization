package me.khol.navigation.features

import android.content.Intent
import me.khol.navigation.loadIntentOrNull

object InvestorNavigation : DynamicFeature<Intent> {

    private const val INVESTOR = "me.khol.investor.InvestorActivity"

    override val dynamicStart: Intent?
        get() = INVESTOR.loadIntentOrNull()
}
