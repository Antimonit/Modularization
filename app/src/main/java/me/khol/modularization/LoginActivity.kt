package me.khol.modularization

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.splitinstall.SplitInstallException
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import me.khol.navigation.SplitInstall
import me.khol.navigation.features.BorrowerNavigation
import me.khol.navigation.features.InvestorNavigation

class LoginActivity : AppCompatActivity() {

    private val splitInstall = SplitInstall(this)

    private val btnLogin by lazy { findViewById<Button>(R.id.btn_login) }
    private val txtModules by lazy { findViewById<TextView>(R.id.txt_modules) }
    private val txtUsername by lazy { findViewById<EditText>(R.id.username) }
    private val txtPassword by lazy { findViewById<EditText>(R.id.password) }

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        refreshAvailableModules()

        btnLogin.setOnClickListener {
            if (txtUsername.text.isEmpty() && txtPassword.text.isEmpty()) {
                openBorrower()
            } else {
                openInvestor()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    private fun openBorrower() {
        if (splitInstall.manager.installedModules.contains("borrower")) {
            BorrowerNavigation.dynamicStart?.let { startActivity(it) }
        } else {
            disposables += splitInstall.download("borrower")
                .subscribe(::resolveSessionState, ::resolveError)
        }
    }

    private fun openInvestor() {
        InvestorNavigation.dynamicStart?.let { startActivity(it) }
    }

    private fun refreshAvailableModules() {
        txtModules.text = splitInstall.manager.installedModules.joinToString()
    }

    private fun resolveSessionState(state: SplitInstallSessionState) {
        val text = when (state.status()) {
            SplitInstallSessionStatus.UNKNOWN -> "Unknown"
            SplitInstallSessionStatus.PENDING -> "Pending"
            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                splitInstall.manager.startConfirmationDialogForResult(state, this, 1337)
                "Requires user confirmation"
            }
            SplitInstallSessionStatus.DOWNLOADING -> {
                val done = state.bytesDownloaded()
                val total = state.totalBytesToDownload()
                val progress = done * 100 / total
                "Downloading $progress% | $done/$total"
            }
            SplitInstallSessionStatus.DOWNLOADED -> "Downloaded"
            SplitInstallSessionStatus.INSTALLING -> "Installing"
            SplitInstallSessionStatus.INSTALLED -> {
                BorrowerNavigation.dynamicStart?.let { startActivity(it) }
                "Installed"
            }
            SplitInstallSessionStatus.FAILED -> "Failed"
            SplitInstallSessionStatus.CANCELING -> "Canceling"
            SplitInstallSessionStatus.CANCELED -> "Canceled"
            else -> "Unknown"
        }
        Log.i("LoginActivity", "Success: $text")
        findViewById<TextView>(R.id.txt_state).text = text
        refreshAvailableModules()
    }

    private fun resolveError(throwable: Throwable) {
        val text = if (throwable is SplitInstallException) {
            when (throwable.errorCode) {
                SplitInstallErrorCode.NO_ERROR -> "NO_ERROR"
                SplitInstallErrorCode.ACTIVE_SESSIONS_LIMIT_EXCEEDED -> "ACTIVE_SESSIONS_LIMIT_EXCEEDED"
                SplitInstallErrorCode.MODULE_UNAVAILABLE -> "MODULE_UNAVAILABLE"
                SplitInstallErrorCode.INVALID_REQUEST -> "INVALID_REQUEST"
                SplitInstallErrorCode.SESSION_NOT_FOUND -> "SESSION_NOT_FOUND"
                SplitInstallErrorCode.API_NOT_AVAILABLE -> "API_NOT_AVAILABLE"
                SplitInstallErrorCode.NETWORK_ERROR -> "NETWORK_ERROR"
                SplitInstallErrorCode.ACCESS_DENIED -> "ACCESS_DENIED"
                SplitInstallErrorCode.INCOMPATIBLE_WITH_EXISTING_SESSION -> "INCOMPATIBLE_WITH_EXISTING_SESSION"
                SplitInstallErrorCode.SERVICE_DIED -> "SERVICE_DIED"
                SplitInstallErrorCode.INSUFFICIENT_STORAGE -> "INSUFFICIENT_STORAGE"
                SplitInstallErrorCode.SPLITCOMPAT_VERIFICATION_ERROR -> "SPLITCOMPAT_VERIFICATION_ERROR"
                SplitInstallErrorCode.SPLITCOMPAT_EMULATION_ERROR -> "SPLITCOMPAT_EMULATION_ERROR"
                SplitInstallErrorCode.SPLITCOMPAT_COPY_ERROR -> "SPLITCOMPAT_COPY_ERROR"
                SplitInstallErrorCode.INTERNAL_ERROR -> "INTERNAL_ERROR"
                else -> "Unknown SplitInstall error"
            }
        } else {
            "Unknown error"
        }
        Log.i("LoginActivity", "Error: $text")
        findViewById<TextView>(R.id.txt_state).text = text
        refreshAvailableModules()
    }
}
