package me.khol.intro.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import me.khol.base.base.BaseActivity
import me.khol.intro.R
import me.khol.navigation.navigation
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
                                    Module.Borrower -> navigation.borrowerIntent()
                                    Module.Investor -> navigation.investorIntent()
                                }?.let { startActivity(it) }
                            }
                            is Result.Success.RequiresUserConfirmation -> {
                                result.manager.startConfirmationDialogForResult(result.state, this, 1337)
                            }
                        }
                    }
                    is Result.Error -> {
                        txtState.text = txtState.text.toString() + "\n" + result.text
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
}
