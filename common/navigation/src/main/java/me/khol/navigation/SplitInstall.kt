package me.khol.navigation

import android.util.Log
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single


fun SplitInstallManager.hasModule(module: String): Boolean {
    return installedModules.contains(module)
}

fun SplitInstallManager.downloadModule(module: String): Single<Int> {
    return Single.create<Int> { emitter ->
        val request = SplitInstallRequest.newBuilder()
            .addModule(module)
            .build()

        startInstall(request)
            .addOnSuccessListener { emitter.onSuccess(it) }
            .addOnFailureListener { emitter.onError(it) }
    }
}

fun SplitInstallManager.cancelDownload(sessionId: Int): Completable {
    return Completable.create { emitter ->
        cancelInstall(sessionId)
            .addOnSuccessListener { emitter.onComplete() }
            .addOnFailureListener { emitter.onError(it) }
    }
}

/**
 * Observe state of all sessions.
 */
fun SplitInstallManager.observeSessionStates(): Observable<SplitInstallSessionState> {
    return Observable.create<SplitInstallSessionState> { emitter ->
        val listener = { state: SplitInstallSessionState ->
            emitter.onNext(state)
        }

        registerListener(listener)
        emitter.setCancellable {
            unregisterListener(listener)
        }
    }
}

/**
 * Gets all currently active sessions.
 */
fun SplitInstallManager.getSessionStates(): Single<List<SplitInstallSessionState>> {
    return Single.create<List<SplitInstallSessionState>> { emitter ->
        sessionStates
            .addOnSuccessListener {
                Log.d("SplitInstall", "SessionStates success: $it")
                emitter.onSuccess(it)
            }
            .addOnFailureListener {
                Log.d("SplitInstall", "SessionStates failure: $it")
                emitter.onError(it)
            }
    }
}
