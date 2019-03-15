package me.khol.modularization

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import me.khol.onboarding.OnboardingActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btn_register).setOnClickListener {
            startActivity(Intent(this, OnboardingActivity::class.java))
//            OnboardingNavigation.dynamicStart?.let { startActivity(it) }
        }

        findViewById<View>(R.id.btn_login).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
