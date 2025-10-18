// Caminho do arquivo: app/src/main/java/com/example/ludgrow/ui/telas/EditAtividadeViewModel.kt
package com.example.ludgrow.ui.telas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ludgrow.data.Atividade
import com.example.ludgrow.data.UserDatabaseHelper

data class EditAtividadeState(
    val id: Int = 0,
    val nome: String = "",
    val descricao: String = "",
    val areaDesenvolvimento: String = "",
    val dataRealizacao: String = "",
    val idDaCrianca: Int = 0,
    val isLoading: Boolean = true,
    val updateSuccess: Boolean = false,
    val error: String? = null
)

class EditAtividadeViewModel(
    private val dbHelper: UserDatabaseHelper,
    private val atividadeId: Int
) : ViewModel() {

    var state by mutableStateOf(EditAtividadeState())
        private set

    init {
        loadAtividadeDetails()
    }

    private fun loadAtividadeDetails() {
        val atividade = dbHelper.getSingleAtividade(atividadeId)
        if (atividade != null) {
            state = state.copy(
                id = atividade.id,
                nome = atividade.nome,
                descricao = atividade.descricao,
                areaDesenvolvimento = atividade.areaDesenvolvimento,
                dataRealizacao = atividade.dataRealizacao,
                idDaCrianca = atividade.idDaCrianca,
                isLoading = false
            )
        } else {
            state = state.copy(error = "Atividade não encontrada.", isLoading = false)
        }
    }

    // Funções para a UI atualizar o estado
    fun onNomeChange(newValue: String) { state = state.copy(nome = newValue) }
    fun onDescricaoChange(newValue: String) { state = state.copy(descricao = newValue) }
    fun onAreaChange(newValue: String) { state = state.copy(areaDesenvolvimento = newValue) }
    fun onDataChange(newValue: String) { state = state.copy(dataRealizacao = newValue) }
    fun onUpdateHandled() { state = state.copy(updateSuccess = false) }
    fun onErrorShown() { state = state.copy(error = null) }

    fun updateAtividade() {
        if (state.nome.isBlank() || state.dataRealizacao.isBlank()) {
            state = state.copy(error = "Nome e Data são obrigatórios.")
            return
        }

        val atividadeAtualizada = Atividade(
            id = state.id,
            nome = state.nome,
            descricao = state.descricao,
            areaDesenvolvimento = state.areaDesenvolvimento,
            dataRealizacao = state.dataRealizacao,
            idDaCrianca = state.idDaCrianca
        )

        val result = dbHelper.updateAtividade(atividadeAtualizada)
        if (result > 0) {
            state = state.copy(updateSuccess = true)
        } else {
            state = state.copy(error = "Falha ao atualizar a atividade.")
        }
    }
}

class EditAtividadeViewModelFactory(
    private val dbHelper: UserDatabaseHelper,
    private val atividadeId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditAtividadeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditAtividadeViewModel(dbHelper, atividadeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
