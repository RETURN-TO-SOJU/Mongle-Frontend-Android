package com.won983212.mongle.data.source.remote.model.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class LoginResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("accessExpiredAt")
    val accessTokenExpiresAt: LocalDateTime,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("refreshExpiredAt")
    val refreshTokenExpiresAt: LocalDateTime,
    @SerializedName("isNew")
    val isNew: Boolean
)
