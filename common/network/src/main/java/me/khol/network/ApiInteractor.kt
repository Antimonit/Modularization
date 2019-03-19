package me.khol.network

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import me.khol.network.model.LoginRequest
import me.khol.network.model.User

interface ApiInteractor {

    fun login(email: String, password: String): Single<User>
}

internal class ApiInteractorImpl(
    private val apiDescription: ApiDescription
): ApiInteractor {

    override fun login(email: String, password: String): Single<User> {
        return apiDescription.login(LoginRequest(email, password), 1)
            .subscribeOn(Schedulers.io())
    }
}
