package com.example.ludgrow.ui.telas

// Em: app/src/main/java/com/example/ludgrow/ui/telas/AddAtividadeViewModel.kt

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ludgrow.data.Atividade
import com.example.ludgrow.data.UserDatabaseHelper

// Estado para o formulário de adicionar atividade
data class AddAtividadeState(
    val nome: String = "",
    val descricao: String = "",
    val areaDesenvolvimento: String = "",
    val dataRealizacao: String = "",
    val saveSuccess: Boolean = false,
    val error: String? = null
)

class AddAtividadeViewModel(
    private val dbHelper: UserDatabaseHelper,
    private val criancaId: Int // Precisa saber a qual criança a atividade pertence
) : ViewModel() {

    var state by mutableStateOf(AddAtividadeState())
        private set

    // Funções para a UI atualizar o estado do formulário
    fun onNomeChange(newValue: String) {
        state = state.copy(nome = newValue)
    }

    fun onDescricaoChange(newValue: String) {
        state = state.copy(descricao = newValue)
    }

    fun onAreaChange(newValue: String) {
        state = state.copy(areaDesenvolvimento = newValue)
    }

    fun onDataChange(newValue: String) {
        state = state.copy(dataRealizacao = newValue)
    }

    fun onSaveHandled() {
        state = state.copy(saveSuccess = false)
    }

    fun onErrorShown() {
        state = state.copy(error = null)
    }

    fun saveAtividade() {
        if (state.nome.isBlank() || state.dataRealizacao.isBlank()) {
            state = state.copy(error = "Nome e Data são obrigatórios.")
            return
        }

        val novaAtividade = Atividade(
            nome = state.nome,
            descricao = state.descricao,
            areaDesenvolvimento = state.areaDesenvolvimento,
            dataRealizacao = state.dataRealizacao,
            idDaCrianca = this.criancaId // Associa a atividade à criança correta
        )

        val result = dbHelper.addAtividade(novaAtividade)

        if (result > -1) {
            state = state.copy(saveSuccess = true)
        } else {
            state = state.copy(error = "Falha ao salvar a atividade.")
        }
    }
}

// Factory para construir o ViewModel com os parâmetros
class AddAtividadeViewModelFactory(
    private val dbHelper: UserDatabaseHelper,
    private val criancaId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddAtividadeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddAtividadeViewModel(dbHelper, criancaId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}