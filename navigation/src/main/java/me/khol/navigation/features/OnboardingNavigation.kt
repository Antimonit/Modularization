package me.khol.navigation.features

import android.content.Intent
import me.khol.navigation.loadIntentOrNull

object OnboardingNavigation : DynamicFeature<Intent> {

    private const val ONBOARDING = "me.khol.onboarding.OnboardingActivity"

    override val dynamicStart: Intent?
        get() = ONBOARDING.loadIntentOrNull()
}
