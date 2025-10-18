package com.example.ludgrow.ui.telas

// Caminho do arquivo: app/src/main/java/com/example/ludgrow/ui/telas/LoginViewModel.kt

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ludgrow.data.UserDatabaseHelper

// 1. DATA CLASS para representar o estado da tela
data class LoginState(
    val email: String = "",
    val password: String = "",
    val userType: String = "Pai",
    val loginSuccess: Boolean = false,
    val error: String? = null,
    val loggedInUserId: Int? = null
)

// 2. VIEWMODEL para gerenciar a lógica
class LoginViewModel(private val dbHelper: UserDatabaseHelper) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    fun onEmailChange(newValue: String) {
        state = state.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        state = state.copy(password = newValue)
    }

    fun onUserTypeChange(newValue: String) {
        state = state.copy(userType = newValue)
    }

    fun onLoginHandled() {
        state = state.copy(loginSuccess = false, loggedInUserId = null)
    }

    fun onErrorShown() {
        state = state.copy(error = null)
    }

    fun login() {
        if (state.email.isBlank() || state.password.isBlank()) {
            state = state.copy(error = "Preencha todos os campos!")
            return
        }

        val loggedInUser = dbHelper.getUserId(state.email, state.password, state.userType)

        if (loggedInUser != null) {
            state = state.copy(loginSuccess = true, loggedInUserId = loggedInUser)
        } else {
            state = state.copy(error = "E-mail, senha ou tipo de usuário incorretos!")
        }
    }
}

// 3. FACTORY para construir o ViewModel
class LoginViewModelFactory(private val dbHelper: UserDatabaseHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(dbHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}