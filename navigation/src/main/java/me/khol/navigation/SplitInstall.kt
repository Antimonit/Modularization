package me.khol.navigation

import android.content.Context
import com.google.android.play.core.splitinstall.SplitInstallException
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter

object SplitInstall {

    fun download(context: Context, dynamicModule: String): Flowable<SplitInstallState> {
        return Flowable.create<SplitInstallState>({ emitter: FlowableEmitter<SplitInstallState> ->
            val manager = SplitInstallManagerFactory.create(context)

            val listener = { state: SplitInstallSessionState ->
                val resolvedState = state.resolveState()
                emitter.onNext(resolvedState)

                if (resolvedState is SplitInstallState.Failed) {
//                    emitter.onError(Throwable("Failed"))
                    emitter.onComplete()
                }
                if (resolvedState is SplitInstallState.Installed) {
                    emitter.onComplete()
                }
            }

            manager.registerListener(listener)
            emitter.setCancellable {
                manager.unregisterListener(listener)
            }

            val request = SplitInstallRequest.newBuilder()
                .addModule(dynamicModule)
                .build()

            manager.startInstall(request)
                .addOnSuccessListener { sessionId: Int ->
                    emitter.onNext(SplitInstallState.Installed(-1))
                }
                .addOnFailureListener { exception: Exception ->
                    emitter.onNext(SplitInstallState.Failed(-1, (exception as SplitInstallException).errorCode))
                }
        }, BackpressureStrategy.LATEST)
    }

    private fun SplitInstallSessionState.resolveState(): SplitInstallState {
        return when (status()) {
            SplitInstallSessionStatus.PENDING -> SplitInstallState.Pending(sessionId())
            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> SplitInstallState.Downloading(sessionId(), bytesDownloaded().toDouble() / totalBytesToDownload())
            SplitInstallSessionStatus.DOWNLOADING -> SplitInstallState.Downloading(sessionId(), bytesDownloaded().toDouble() / totalBytesToDownload())
            SplitInstallSessionStatus.INSTALLING -> SplitInstallState.Instaling(sessionId())
            SplitInstallSessionStatus.INSTALLED -> SplitInstallState.Installed(sessionId())
            SplitInstallSessionStatus.FAILED -> SplitInstallState.Failed(sessionId(), errorCode())
            // TODO: add more
            else -> SplitInstallState.Unknown
        }

    }

    fun checkForActiveDownloads(context: Context) {
        SplitInstallManagerFactory.create(context)
            // Returns a SplitInstallSessionState object for each active session as a List.
            .sessionStates
            .addOnSuccessListener { states ->
                states.forEach { state: SplitInstallSessionState ->
                    state.resolveState()
                }
            }
    }
}

private fun resolveErrorCode(errorCode: Int) {
    when (errorCode) {
        SplitInstallErrorCode.ACTIVE_SESSIONS_LIMIT_EXCEEDED -> {}
        SplitInstallErrorCode.NETWORK_ERROR -> {}
        // TODO ...
    }
}

sealed class SplitInstallState(open val sessionId: Int) {
    data class Instaling(override val sessionId: Int): SplitInstallState(sessionId)
    data class Installed(override val sessionId: Int): SplitInstallState(sessionId)
    data class Pending(override val sessionId: Int) : SplitInstallState(sessionId)
    /**
     * In order to see this, the application has to be uploaded to the Play Store.
     */
    data class Downloading(override val sessionId: Int, val progress: Double) : SplitInstallState(sessionId)
    /**
     * This may occur when attempting to download a sufficiently large module.
     * In order to see this, the application has to be uploaded to the Play Store.
     * Then features can be requested until the confirmation path is triggered.
     */
    data class RequiresUserConfirmation(override val sessionId: Int) : SplitInstallState(sessionId)
    data class Failed(override val sessionId: Int, val errorCode: Int): SplitInstallState(sessionId)
    object Unknown: SplitInstallState(-1)
}
