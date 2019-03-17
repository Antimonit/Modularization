package me.khol.network

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

internal interface ApiDescription {

    @POST("login")
    fun login(@Body body: LoginRequest, @Query("delay") delay: Int): Single<User>
}
