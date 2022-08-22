package com.won983212.mongle.presentation.view.login

import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.won983212.mongle.common.util.asLiveData
import com.won983212.mongle.data.model.OAuthLoginToken
import com.won983212.mongle.data.remote.api.EmptyRequestLifecycleCallback
import com.won983212.mongle.domain.repository.UserRepository
import com.won983212.mongle.domain.usecase.ValidateTokenUseCase
import com.won983212.mongle.presentation.base.BaseViewModel
import com.won983212.mongle.presentation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val validateTokenUseCase: ValidateTokenUseCase
) : BaseViewModel() {

    private val _eventReadyForRegister = SingleLiveEvent<Unit>()
    val eventReadyForRegister = _eventReadyForRegister.asLiveData()

    private val _eventLoggedIn = SingleLiveEvent<Unit>()
    val eventLoggedIn = _eventLoggedIn.asLiveData()

    fun doLoginWithKakaoToken(token: OAuthToken) = viewModelScope.launch {
        val response = userRepository.login(this@LoginViewModel, OAuthLoginToken.of(token))
        if (response != null) {
            userRepository.setCurrentToken(response)
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                // TODO 좀 더 세련된 방법 없나..?
                viewModelScope.launch {
                    if (task.isSuccessful) {
                        userRepository.setFCMToken(this@LoginViewModel, task.result)
                    }
                }
            }
            _eventReadyForRegister.call()
        }
    }

    suspend fun validateToken(): Boolean {
        setLoading(true)
        val canAutoLogin = validateTokenUseCase.execute(EmptyRequestLifecycleCallback)
        setLoading(false)
        if (canAutoLogin) {
            _eventLoggedIn.call()
        }
        return canAutoLogin
    }
}