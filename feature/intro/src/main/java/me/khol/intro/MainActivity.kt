package me.khol.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import me.khol.intro.login.LoginActivity
import me.khol.navigation.features.OnboardingNavigation

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btn_register).setOnClickListener {
            OnboardingNavigation.dynamicStart?.let { startActivity(it) }
        }

        findViewById<View>(R.id.btn_login).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
