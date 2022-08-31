package com.won983212.mongle.data.model

import com.google.gson.annotations.SerializedName
import com.kakao.sdk.auth.model.OAuthToken
import com.won983212.mongle.toLocalDateTime
import java.time.LocalDateTime

data class OAuthLoginToken(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("accessTokenExpiresAt")
    val accessTokenExpiresAt: LocalDateTime,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("refreshTokenExpiresAt")
    val refreshTokenExpiresAt: LocalDateTime,
) {
    fun isAccessTokenExpired(): Boolean {
        return LocalDateTime.now() > accessTokenExpiresAt
    }

    fun isRefreshTokenExpired(): Boolean {
        return LocalDateTime.now() > refreshTokenExpiresAt
    }

    companion object {
        fun fromKakaoToken(kakaoToken: OAuthToken) =
            OAuthLoginToken(
                kakaoToken.accessToken,
                kakaoToken.accessTokenExpiresAt.toLocalDateTime(),
                kakaoToken.refreshToken,
                kakaoToken.refreshTokenExpiresAt.toLocalDateTime()
            )

        val EMPTY_TOKEN = OAuthLoginToken(
            "", LocalDateTime.MIN,
            "", LocalDateTime.MIN
        )
    }
}