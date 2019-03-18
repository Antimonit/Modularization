package me.khol.intro.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import me.khol.base.base.BaseActivity
import me.khol.intro.R
import me.khol.navigation.features.BorrowerNavigation
import me.khol.navigation.features.InvestorNavigation
import me.khol.network.apiInteractor

class LoginActivity : BaseActivity() {

    private val btnLogin by lazy { findViewById<Button>(R.id.btn_login) }
    private val progress by lazy { findViewById<View>(R.id.progress) }
    private val txtModules by lazy { findViewById<TextView>(R.id.txt_modules) }
    private val txtUsername by lazy { findViewById<EditText>(R.id.username) }
    private val txtPassword by lazy { findViewById<EditText>(R.id.password) }
    private val txtState by lazy { findViewById<TextView>(R.id.txt_state) }

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = LoginViewModel(this, apiInteractor)

        disposables += viewModel.observeLoading()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { loading ->
                btnLogin.visibility = if (loading) View.GONE else View.VISIBLE
                progress.visibility = if (loading) View.VISIBLE else View.GONE
            }

        disposables += viewModel.observeModules()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { modules ->
                txtModules.text = "Modules: ${modules.joinToString()}"
            }

        disposables += viewModel.observeProgress()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                when (result) {
                    is Result.Success -> {
                        txtState.text = txtState.text.toString() + "\n" + result.text
                        when (result) {
                            is Result.Success.Installed -> {
                                when (result.module) {
                                    Module.Borrower -> BorrowerNavigation.dynamicStart
                                    Module.Investor -> InvestorNavigation.dynamicStart
                                }?.let { startActivity(it) }
                            }
                            is Result.Success.RequiresUserConfirmation -> {
                                result.manager.startConfirmationDialogForResult(result.state, this, 1337)
                            }
                        }
                    }
                    is Result.Error -> {
                        Snackbar.make(
                            findViewById(android.R.id.content)!!,
                            "Error: ${result.text}",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }

        btnLogin.setOnClickListener {
            viewModel.login(
                txtUsername.text.toString(),
                txtPassword.text.toString()
            )
        }
    }
//
//    private fun open(module: String, continuation: () -> Unit) {
////        if (splitInstall.manager.installedModules.contains(module)) {
////            continuation()
////            return
////        }
//
//        disposables += splitInstall.downloadModule(module)
//            .toObservable()
//            .switchMap { sessionId ->
//                splitInstall.observeSessionState(sessionId)
//            }
//            .subscribe({ state ->
//                resolveSessionState(state, continuation)
//            }, ::resolveError)
//    }
//
//    private fun resolveSessionState(state: SplitInstallSessionState, continuation: () -> Unit) {
//        val text = when (state.status()) {
//            SplitInstallSessionStatus.UNKNOWN -> "Unknown"
//            SplitInstallSessionStatus.PENDING -> "Pending"
//            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
//                splitInstall.manager.startConfirmationDialogForResult(state, this, 1337)
//                "Requires user confirmation"
//            }
//            SplitInstallSessionStatus.DOWNLOADING -> {
//                val done = state.bytesDownloaded()
//                val total = state.totalBytesToDownload()
//                val progress = done * 100 / total
//                "Downloading $progress% | $done/$total"
//            }
//            SplitInstallSessionStatus.DOWNLOADED -> "Downloaded"
//            SplitInstallSessionStatus.INSTALLING -> "Installing"
//            SplitInstallSessionStatus.INSTALLED -> {
//                continuation()
//                "Installed"
//            }
//            SplitInstallSessionStatus.FAILED -> "Failed"
//            SplitInstallSessionStatus.CANCELING -> "Canceling"
//            SplitInstallSessionStatus.CANCELED -> "Canceled"
//            else -> "Unknown"
//        }
//        Log.i("LoginActivity", "Success: $text")
//        txtState.text = text
//        refreshAvailableModules()
//    }
//
//    private fun resolveError(throwable: Throwable) {
//        val text = if (throwable is SplitInstallException) {
//            when (throwable.errorCode) {
//                SplitInstallErrorCode.NO_ERROR -> "NO_ERROR"
//                SplitInstallErrorCode.ACTIVE_SESSIONS_LIMIT_EXCEEDED -> "ACTIVE_SESSIONS_LIMIT_EXCEEDED"
//                SplitInstallErrorCode.MODULE_UNAVAILABLE -> "MODULE_UNAVAILABLE"
//                SplitInstallErrorCode.INVALID_REQUEST -> "INVALID_REQUEST"
//                SplitInstallErrorCode.SESSION_NOT_FOUND -> "SESSION_NOT_FOUND"
//                SplitInstallErrorCode.API_NOT_AVAILABLE -> "API_NOT_AVAILABLE"
//                SplitInstallErrorCode.NETWORK_ERROR -> "NETWORK_ERROR"
//                SplitInstallErrorCode.ACCESS_DENIED -> "ACCESS_DENIED"
//                SplitInstallErrorCode.INCOMPATIBLE_WITH_EXISTING_SESSION -> "INCOMPATIBLE_WITH_EXISTING_SESSION"
//                SplitInstallErrorCode.SERVICE_DIED -> "SERVICE_DIED"
//                SplitInstallErrorCode.INSUFFICIENT_STORAGE -> "INSUFFICIENT_STORAGE"
//                SplitInstallErrorCode.SPLITCOMPAT_VERIFICATION_ERROR -> "SPLITCOMPAT_VERIFICATION_ERROR"
//                SplitInstallErrorCode.SPLITCOMPAT_EMULATION_ERROR -> "SPLITCOMPAT_EMULATION_ERROR"
//                SplitInstallErrorCode.SPLITCOMPAT_COPY_ERROR -> "SPLITCOMPAT_COPY_ERROR"
//                SplitInstallErrorCode.INTERNAL_ERROR -> "INTERNAL_ERROR"
//                else -> "Unknown SplitInstall error"
//            }
//        } else {
//            "Unknown error"
//        }
//        Log.i("LoginActivity", "Error: $text")
//        txtState.text = text
//        refreshAvailableModules()
//    }
}
