package me.khol.navigation

import android.content.Context
import android.util.Log
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import io.reactivex.Observable
import io.reactivex.Single

class SplitInstall(context: Context) {

    val manager: SplitInstallManager by lazy { SplitInstallManagerFactory.create(context) }

    init {
        manager.registerListener { state ->
            Log.d("SplitInstall init", "State: $state")
        }
    }

    fun hasModule(module: String): Boolean {
        return manager.installedModules.contains(module)
    }

    fun downloadModule(module: String): Single<Int> {
        return Single.create<Int> { emitter ->
            val request = SplitInstallRequest.newBuilder()
                .addModule(module)
                .build()

            // TODO:
//            emitter.setCancellable {
//                manager.cancelInstall(sessionId)
//            }

            manager.startInstall(request)
                .addOnSuccessListener {
                    Log.d("SplitInstall", "startInstall success: $it")
                    emitter.onSuccess(it)
                }
                .addOnFailureListener {
                    Log.d("SplitInstall", "startInstall failure: $it")
                    emitter.onError(it)
                }
        }
    }

    /**
     * Observe state of a sessions with [sessionId].
     *
     * Note: There is no clean way to find out whether a session is still active. When we receive
     * a session state we can't tell whether there will be more emission or not. The only way to
     * find it out is to call [SplitInstallManager.getSessionStates] and check whether our session
     * is in the list. Once the session is not in the list anymore, terminate the observable.
     */
    fun observeSessionStateWhileActive(sessionId: Int): Observable<SplitInstallSessionState> {
        return observeSessionState(sessionId)
            .takeWhile {
                Log.d("SplitInstall", "Blocked")
                // TODO: this does not work well....
                val blocked = hasSession(it.sessionId()).blockingGet()
                Log.d("SplitInstall", "Blocked result: $blocked")
                blocked
            }
    }

    /**
     * Observe state of a sessions with [sessionId].
     */
    fun observeSessionState(sessionId: Int): Observable<SplitInstallSessionState> {
        return observeSessionStates()
            .filter { it.sessionId() == sessionId }
    }

    /**
     * Observe state of all sessions.
     */
    fun observeSessionStates(): Observable<SplitInstallSessionState> {
        return Observable.create<SplitInstallSessionState> { emitter ->
            val listener = { state: SplitInstallSessionState ->
                emitter.onNext(state)
            }

            manager.registerListener(listener)
            emitter.setCancellable {
                manager.unregisterListener(listener)
            }
        }
    }

    /**
     * Checks whether a session with [sessionId] is currently active.
     */
    fun hasSession(sessionId: Int): Single<Boolean> {
        return getSessionStates()
            .map { it.any { it.sessionId() == sessionId } }
    }

    /**
     * Gets all currently active sessions.
     */
    fun getSessionStates(): Single<List<SplitInstallSessionState>> {
        return Single.create<List<SplitInstallSessionState>> { emitter ->
            manager.sessionStates
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
}
