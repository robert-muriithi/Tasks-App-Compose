package dev.robert.auth.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.auth.domain.repository.AuthenticationRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthenticationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState = _uiState.asStateFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _uiState.update { it.copy(error = exception.message) }
    }
    fun onEmailChange(email: String) = _uiState.update { uiState ->
        uiState.copy(email = email)
    }
    fun onPasswordChange(password: String) = _uiState.update { uiState ->
        uiState.copy(password = password)
    }
    fun onNameChange(name: String) = _uiState.update { uiState ->
        uiState.copy(name = name)
    }

    fun register(email: String, password: String, name: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(coroutineExceptionHandler) {
            repository.register(email, password)
            _uiState.update { it.copy(isSuccess = true, isLoading = false) }
        }
    }

    fun login(email: String, password: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(coroutineExceptionHandler) {
            repository.login(email = email, password = password).collectLatest {
                _uiState.update {
                    it.copy(isSuccess = true, isLoading = false)
                }
            }
        }
    }
}
