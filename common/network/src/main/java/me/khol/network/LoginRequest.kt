package me.khol.network

internal data class LoginRequest(
    val email: String,
    val password: String
)