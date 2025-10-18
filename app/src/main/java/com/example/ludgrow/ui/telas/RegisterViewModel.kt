package com.example.ludgrow.ui.telas

// Em: app/src/main/java/com/example/ludgrow/ui/telas/RegisterViewModel.kt

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ludgrow.data.UserDatabaseHelper

// 1. Estado da tela de Registro
data class RegisterState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val userType: String = "Pai",
    val registrationSuccess: Boolean = false,
    val error: String? = null
)

// 2. ViewModel para gerenciar a lógica de registro
class RegisterViewModel(private val dbHelper: UserDatabaseHelper) : ViewModel() {

    var state by mutableStateOf(RegisterState())
        private set

    fun onEmailChange(newValue: String) {
        state = state.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        state = state.copy(password = newValue)
    }

    fun onConfirmPasswordChange(newValue: String) {
        state = state.copy(confirmPassword = newValue)
    }

    fun onUserTypeChange(newValue: String) {
        state = state.copy(userType = newValue)
    }

    fun onRegistrationHandled() {
        state = state.copy(registrationSuccess = false)
    }

    fun onErrorShown() {
        state = state.copy(error = null)
    }

    fun register() {
        if (state.email.isBlank() || state.password.isBlank() || state.confirmPassword.isBlank()) {
            state = state.copy(error = "Preencha todos os campos!")
            return
        }
        if (state.password != state.confirmPassword) {
            state = state.copy(error = "As senhas não coincidem!")
            return
        }
        // Validação simples de email
        if (!Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            state = state.copy(error = "Formato de e-mail inválido!")
            return
        }

        // Tenta adicionar o usuário no banco de dados
        val result = dbHelper.addUser(state.email, state.password, state.userType)

        if (result != -1L) { // -1L é o retorno padrão do SQLite para erro de inserção
            state = state.copy(registrationSuccess = true)
        } else {
            // A causa mais provável de falha é o email já existir (UNIQUE constraint)
            state = state.copy(error = "Este e-mail já está em uso!")
        }
    }
}

// 3. Factory para construir o ViewModel com o dbHelper
class RegisterViewModelFactory(private val dbHelper: UserDatabaseHelper) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(dbHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}