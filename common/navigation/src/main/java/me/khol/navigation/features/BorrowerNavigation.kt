package me.khol.navigation.features

import android.content.Intent
import me.khol.navigation.loadIntentOrNull

object BorrowerNavigation : DynamicFeature<Intent> {

    private const val BORROWER = "me.khol.borrower.BorrowerActivity"

    override val dynamicStart: Intent?
        get() = BORROWER.loadIntentOrNull()
}
