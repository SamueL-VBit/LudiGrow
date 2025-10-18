package com.example.ludgrow.ui.telas

// Em: app/src/main/java/com/example/ludgrow/ui/telas/AddCriancaViewModel.kt

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ludgrow.data.Crianca
import com.example.ludgrow.data.UserDatabaseHelper

// 1. Estado do formulário de adicionar criança
data class AddCriancaState(
    val nome: String = "",
    val dataNascimento: String = "",
    val nivelSuporte: String = "Nível 1", // Valor padrão
    val observacoes: String = "",
    val saveSuccess: Boolean = false,
    val error: String? = null
)

// 2. ViewModel para gerenciar a lógica
class AddCriancaViewModel(
    private val dbHelper: UserDatabaseHelper,
    private val paiId: Int // Precisamos saber a qual pai a criança pertence
) : ViewModel() {

    var state by mutableStateOf(AddCriancaState())
        private set

    fun onNomeChange(newValue: String) {
        state = state.copy(nome = newValue)
    }

    fun onDataNascimentoChange(newValue: String) {
        state = state.copy(dataNascimento = newValue)
    }

    fun onNivelSuporteChange(newValue: String) {
        state = state.copy(nivelSuporte = newValue)
    }

    fun onObservacoesChange(newValue: String) {
        state = state.copy(observacoes = newValue)
    }

    fun onSaveHandled() {
        state = state.copy(saveSuccess = false)
    }

    fun onErrorShown() {
        state = state.copy(error = null)
    }

    fun saveCrianca() {
        if (state.nome.isBlank() || state.dataNascimento.isBlank()) {
            state = state.copy(error = "Nome e Data de Nascimento são obrigatórios!")
            return
        }

        val novaCrianca = Crianca(
            nome = state.nome,
            dataNascimento = state.dataNascimento,
            nivelSuporte = state.nivelSuporte,
            observacoes = state.observacoes,
            idDoPai = paiId // Associa a criança ao ID do pai logado
        )

        val result = dbHelper.addCrianca(novaCrianca)

        if (result != -1L) {
            state = state.copy(saveSuccess = true)
        } else {
            state = state.copy(error = "Falha ao salvar a criança. Tente novamente.")
        }
    }
}

// 3. Factory para construir o ViewModel com os parâmetros necessários
class AddCriancaViewModelFactory(
    private val dbHelper: UserDatabaseHelper,
    private val paiId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddCriancaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddCriancaViewModel(dbHelper, paiId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}