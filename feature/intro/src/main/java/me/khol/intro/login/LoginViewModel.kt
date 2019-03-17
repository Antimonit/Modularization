package me.khol.intro.login

import android.content.Context
import com.google.android.play.core.splitinstall.SplitInstallException
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import io.reactivex.Observable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import me.khol.base.base.BaseViewModel
import me.khol.navigation.SplitInstall
import me.khol.network.ApiInteractor
import me.khol.network.User
import kotlin.random.Random

/**
 *
 */
class LoginViewModel(
    context: Context,
    private val apiInteractor: ApiInteractor
) : BaseViewModel() {

    private val splitInstall = SplitInstall(context)

    private val loadingSubject = BehaviorSubject.create<Boolean>()
    private val progressSubject = BehaviorSubject.create<Result>()

    fun observeLoading(): Observable<Boolean> = loadingSubject
    fun observeProgress(): Observable<Result> = progressSubject

    fun login(email: String, password: String) {
        disposables += apiInteractor.login(email, password)
            .doOnSubscribe { loadingSubject.onNext(true) }
            .map<LoginResult>(LoginResult::Success)
            .onErrorReturn(LoginResult::Error)
            .flatMapObservable(::handleLoginResult)
            .doFinally { loadingSubject.onNext(false) }
            .subscribe(progressSubject::onNext)
    }

    private fun handleLoginResult(it: LoginResult): Observable<Result> {
        return when (it) {
            is LoginResult.Success -> if (Random.nextBoolean()) {
                loadModule(Module.Borrower)
            } else {
                loadModule(Module.Investor)
            }
            is LoginResult.Error -> {
                Observable.just(Result.Error.LoginError(it.throwable.toString()))
            }
        }
    }

    private fun loadModule(module: Module): Observable<Result> {
//        if (splitInstall.hasModule(module.name)) {
//            return Observable.just(Result.Success.Installed(module, "Installed"))
//        }

        return splitInstall.downloadModule(module.name)
            .toObservable()
            .flatMap(splitInstall::observeSessionStateWhileActive)
            .map<Result> { state -> resolveSessionState(module, state) }
            .onErrorReturn { throwable -> resolveSessionError(module, throwable) }
    }

    private fun resolveSessionState(module: Module, state: SplitInstallSessionState): Result.Success {
        val text = when (state.status()) {
            SplitInstallSessionStatus.UNKNOWN -> "Unknown"
            SplitInstallSessionStatus.PENDING -> "Pending"
            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                return Result.Success.RequiresUserConfirmation(module, splitInstall.manager, state, "Requires user confirmation")
            }
            SplitInstallSessionStatus.DOWNLOADING -> {
                val done = state.bytesDownloaded()
                val total = state.totalBytesToDownload()
                val progress = done * 100 / total
                return Result.Success.Downloading(module, "Downloading $progress% | $done/$total")
            }
            SplitInstallSessionStatus.DOWNLOADED -> "Downloaded"
            SplitInstallSessionStatus.INSTALLING -> "Installing"
            SplitInstallSessionStatus.INSTALLED -> {
                return Result.Success.Installed(module, "Installed")
            }
            SplitInstallSessionStatus.FAILED -> "Failed"
            SplitInstallSessionStatus.CANCELING -> "Canceling"
            SplitInstallSessionStatus.CANCELED -> "Canceled"
            else -> "Unknown"
        }
        return Result.Success.Simple(module, text)
    }

    private fun resolveSessionError(module: Module, throwable: Throwable): Result.Error {
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
        return Result.Error.ModuleError(module, text)
    }
}

sealed class Module(val name: String) {
    object Borrower : Module("borrower")
    object Investor : Module("investor")
}

sealed class LoginResult {
    class Success(val user: User) : LoginResult()
    class Error(val throwable: Throwable) : LoginResult()
}

sealed class Result {
    sealed class Success(val module: Module, val text: String) : Result() {
        class Simple(module: Module, text: String) : Success(module, text)
        class RequiresUserConfirmation(module: Module, val manager: SplitInstallManager, val state: SplitInstallSessionState, text: String) : Success(module, text)
        class Installed(module: Module, text: String) : Success(module, text)
        class Downloading(module: Module, text: String) : Success(module, text)
    }
    sealed class Error(val text: String) : Result() {
        class LoginError(text: String) : Error(text)
        class ModuleError(val module: Module, text: String) : Error(text)
    }
}
