package me.khol.intro.login

import android.content.Context
import com.google.android.play.core.splitinstall.SplitInstallException
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import me.khol.base.base.BaseViewModel
import me.khol.navigation.*
import me.khol.network.ApiInteractor

/**
 *
 */
class LoginViewModel(
    context: Context,
    private val apiInteractor: ApiInteractor
) : BaseViewModel() {

    private val manager: SplitInstallManager by lazy { SplitInstallManagerFactory.create(context) }

    private val loadingSubject = BehaviorSubject.create<Boolean>()
    private val modulesSubject = BehaviorSubject.create<List<String>>()
    private val progressSubject = BehaviorSubject.create<Result>()

    fun observeLoading(): Observable<Boolean> = loadingSubject
    fun observeModules(): Observable<List<String>> = modulesSubject
    fun observeProgress(): Observable<Result> = progressSubject

    private var currentSessionModule: SessionModule? = null

    init {
        disposables += manager.observeSessionStates()
            .subscribeOn(Schedulers.io())
            .subscribe { state ->
                currentSessionModule?.let { (session, module) ->
                    if (state.sessionId() == session) {
                        progressSubject.onNext(resolveSessionState(module, state))
                    }
                }
                refreshAvailableModules()
            }

        refreshAvailableModules()
    }

    private fun refreshAvailableModules() {
        modulesSubject.onNext(manager.installedModules.toList())
    }

    fun login(email: String, password: String) {
        disposables += apiInteractor.login(email, password)
            .doOnSubscribe { loadingSubject.onNext(true) }
            .doFinally { loadingSubject.onNext(false) }
            .subscribe({ user ->
                loadModule(if (email.hashCode() % 2 == 0) Module.Borrower else Module.Investor)
            }, {
                progressSubject.onNext(Result.Error.LoginError(it.toString()))
            })
    }

    private fun loadModule(module: Module) {
//        if (manager.hasModule(module.name)) {
//            progressSubject.onNext(Result.Success.Installed(module, "Installed"))
//
//        } else {
            val cancel = currentSessionModule?.let {
                manager.cancelDownload(it.session)
                    .doOnError { progressSubject.onNext(resolveSessionError(module, it)) }
                    .onErrorComplete()
            } ?: Completable.complete()

            val download = manager.downloadModule(module.name)

            disposables += cancel.andThen(download)
                .subscribe({ sessionId ->
                    currentSessionModule = SessionModule(sessionId, module)
                }, { throwable ->
                    progressSubject.onNext(resolveSessionError(module, throwable))
                })
//        }
    }

    private fun resolveSessionState(module: Module, state: SplitInstallSessionState): Result.Success {
        val text = when (state.status()) {
            SplitInstallSessionStatus.UNKNOWN -> "Unknown"
            SplitInstallSessionStatus.PENDING -> "Pending"
            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                return Result.Success.RequiresUserConfirmation(
                    module,
                    manager,
                    state,
                    "Requires user confirmation"
                )
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

private data class SessionModule(val session: Int, val module: Module)

sealed class Module(val name: String) {
    object Borrower : Module("borrower")
    object Investor : Module("investor")
}

sealed class Result {
    sealed class Success(val module: Module, val text: String) : Result() {
        class Simple(module: Module, text: String) : Success(module, text)
        class RequiresUserConfirmation(
            module: Module,
            val manager: SplitInstallManager,
            val state: SplitInstallSessionState,
            text: String
        ) : Success(module, text)

        class Installed(module: Module, text: String) : Success(module, text)
        class Downloading(module: Module, text: String) : Success(module, text)
    }

    sealed class Error(val text: String) : Result() {
        class LoginError(text: String) : Error(text)
        class ModuleError(val module: Module, text: String) : Error(text)
    }
}
