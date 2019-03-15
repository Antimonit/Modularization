package me.khol.modularization

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import me.khol.navigation.SplitInstall
import me.khol.navigation.SplitInstallState
import me.khol.navigation.features.BorrowerNavigation
import me.khol.navigation.features.InvestorNavigation
import kotlin.random.Random

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btn_login)
        val txtState = findViewById<TextView>(R.id.txt_state)

        btnLogin.setOnClickListener {
            if (Random.nextBoolean()) {
                txtState.text = ""
                val sub = SplitInstall.download(this, "borrower")
                    .subscribe { state ->
                        when (state) {
                            is SplitInstallState.Pending -> {
                                txtState.text = txtState.text.toString() + "\nPending ${state.sessionId}"
                            }
                            is SplitInstallState.Installed -> {
                                BorrowerNavigation.dynamicStart?.let { startActivity(it) }
                            }
                            is SplitInstallState.Downloading -> {
                                txtState.text = txtState.text.toString() + "\nDownloading ${state.sessionId} ${state.progress}"
                            }
                            is SplitInstallState.Failed -> {
                                txtState.text = txtState.text.toString() + "\nError ${state.sessionId} ${state.errorCode}"
                            }
                            is SplitInstallState.Unknown -> {
                                txtState.text = txtState.text.toString() + "\nUnknown"
                            }
                        }
                    }
            } else {
                InvestorNavigation.dynamicStart?.let { startActivity(it) }
            }
        }
    }
}
