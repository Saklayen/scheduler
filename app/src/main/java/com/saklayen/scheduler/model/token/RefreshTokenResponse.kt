package com.saklayen.scheduler.model.token

data class RefreshTokenResponse(
    val refreshToken: String,
    val token: String
)