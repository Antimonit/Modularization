package me.khol.network.model

internal data class LoginRequest(
    val email: String,
    val password: String
)