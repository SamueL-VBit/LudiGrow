// Em: app/src/main/java/com/example/ludgrow/ui/telas/HomeViewModel.kt
package com.example.ludgrow.ui.telas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ludgrow.data.Crianca
import com.example.ludgrow.data.UserDatabaseHelper

// ... (a data class HomeState continua igual)
data class HomeState(
    val criancas: List<Crianca> = emptyList(),
    val isLoading: Boolean = true
)

class HomeViewModel(
    private val dbHelper: UserDatabaseHelper,
    private val userId: Int,
    private val userType: String
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    init {
        loadCriancas()
    }

    // AÇÃO: Mude de 'private fun' para 'fun' para torná-la pública
    fun loadCriancas() {
        state = state.copy(isLoading = true)
        val listaDeCriancas = dbHelper.getCriancasDoUsuario(userId, userType)
        state = state.copy(criancas = listaDeCriancas, isLoading = false)
    }
}

// ... (a Factory continua igual)
class HomeViewModelFactory(
    private val dbHelper: UserDatabaseHelper,
    private val userId: Int,
    private val userType: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dbHelper, userId, userType) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
