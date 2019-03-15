package me.khol.navigation

import android.content.Context
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

class SplitInstall(context: Context) {

    val manager: SplitInstallManager by lazy { SplitInstallManagerFactory.create(context) }

    fun download(module: String): Flowable<SplitInstallSessionState> {
        return downloadModule("borrower")
            .toFlowable()
            .flatMap(::observeSessionState)
            .startWith(getModuleState(module).toFlowable())
    }

    fun observeSessionState(sessionId: Int): Flowable<SplitInstallSessionState> {
        return Flowable.create<SplitInstallSessionState>({ emitter ->
            val listener = { state: SplitInstallSessionState ->
                if (state.sessionId() == sessionId) {
                    emitter.onNext(state)
                }
            }

            manager.registerListener(listener)
            emitter.setCancellable {
                manager.unregisterListener(listener)
            }
        }, BackpressureStrategy.LATEST)
    }

    fun downloadModule(module: String): Single<Int> {
        manager.sessionStates
        return Single.create<Int> { emitter ->
            val request = SplitInstallRequest.newBuilder()
                .addModule(module)
                .build()

            manager.startInstall(request)
                .addOnSuccessListener(emitter::onSuccess)
                .addOnFailureListener(emitter::onError)
        }
    }

    fun getModuleState(module: String): Maybe<SplitInstallSessionState> {
        return Maybe.create<SplitInstallSessionState> { emitter ->
            manager.sessionStates.addOnSuccessListener {
                it.forEach { state ->
                    if (state.moduleNames().contains(module)) {
                        emitter.onSuccess(state)
                    } else {
                        emitter.onComplete()
                    }
                }
            }
        }
    }
    
    fun checkForActiveDownloads(context: Context) {
        manager
            .sessionStates
            .addOnSuccessListener { states ->
                states.forEach { state: SplitInstallSessionState ->

                }
            }
    }
}
