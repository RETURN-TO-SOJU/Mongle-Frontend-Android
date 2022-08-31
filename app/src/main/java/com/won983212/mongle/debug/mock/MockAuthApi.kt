package com.won983212.mongle.debug.mock

import android.util.Log
import com.won983212.mongle.data.model.OAuthLoginToken
import com.won983212.mongle.data.model.User
import com.won983212.mongle.data.source.api.AuthApi
import com.won983212.mongle.data.source.api.UserApi
import com.won983212.mongle.data.source.remote.model.MessageResult
import com.won983212.mongle.data.source.remote.model.request.FCMTokenRequest
import com.won983212.mongle.data.source.remote.model.request.UsernameRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MockAuthApi : AuthApi {

    private fun generateToken(): OAuthLoginToken {
        val now = LocalDateTime.now()
        val genToken = now.format(DateTimeFormatter.ISO_DATE_TIME)
        val accessExpires = now.plusSeconds(ACCESS_TOKEN_EXPIRES_SEC)
        val refreshExpires = now.plusSeconds(REFRESH_TOKEN_EXPIRES_SEC)
        return OAuthLoginToken(genToken, accessExpires, genToken, refreshExpires)
    }

    override suspend fun login(kakaoToken: OAuthLoginToken): OAuthLoginToken =
        withContext(Dispatchers.IO) {
            delay(300)
            generateToken()
        }

    override suspend fun refreshToken(token: OAuthLoginToken): OAuthLoginToken =
        withContext(Dispatchers.IO) {
            delay(500)
            if (token.isRefreshTokenExpired()) {
                throw MockingHttpException("Refresh 토큰이 만료되었습니다. 다시 로그인하세요.")
            }
            Log.d("MockAuthApi", "RefreshToken 발급")
            generateToken()
        }

    companion object {
        private const val ACCESS_TOKEN_EXPIRES_SEC = 10L
        private const val REFRESH_TOKEN_EXPIRES_SEC = 30L

        fun checkToken(token: String) {
            val tokenDate = LocalDateTime.parse(token, DateTimeFormatter.ISO_DATE_TIME)
            if (tokenDate.plusSeconds(ACCESS_TOKEN_EXPIRES_SEC) < LocalDateTime.now()) {
                throw MockingHttpException("토큰이 만료되었습니다.")
            }
        }
    }
}