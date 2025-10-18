// Caminho do arquivo: app/src/main/java/com/example/ludgrow/ui/telas/EditCriancaViewModel.kt
package com.example.ludgrow.ui.telas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ludgrow.data.Crianca
import com.example.ludgrow.data.UserDatabaseHelper

data class EditCriancaState(
    val id: Int = 0,
    val nome: String = "",
    val dataNascimento: String = "",
    val nivelSuporte: String = "",
    val observacoes: String = "",
    val idDoPai: Int = 0,
    val updateSuccess: Boolean = false,
    val deleteSuccess: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null
)

class EditCriancaViewModel(
    private val dbHelper: UserDatabaseHelper,
    private val criancaId: Int
) : ViewModel() {

    var state by mutableStateOf(EditCriancaState())
        private set

    init {
        loadCriancaDetails()
    }

    private fun loadCriancaDetails() {
        val crianca = dbHelper.getSingleCrianca(criancaId)
        if (crianca != null) {
            state = state.copy(
                id = crianca.id,
                nome = crianca.nome,
                dataNascimento = crianca.dataNascimento,
                nivelSuporte = crianca.nivelSuporte,
                observacoes = crianca.observacoes,
                idDoPai = crianca.idDoPai,
                isLoading = false
            )
        } else {
            state = state.copy(error = "Criança não encontrada.", isLoading = false)
        }
    }

    fun onNomeChange(newValue: String) { state = state.copy(nome = newValue) }
    fun onDataNascimentoChange(newValue: String) { state = state.copy(dataNascimento = newValue) }
    fun onNivelSuporteChange(newValue: String) { state = state.copy(nivelSuporte = newValue) }
    fun onObservacoesChange(newValue: String) { state = state.copy(observacoes = newValue) }
    fun onUpdateHandled() { state = state.copy(updateSuccess = false) }
    fun onDeleteHandled() { state = state.copy(deleteSuccess = false) }
    fun onErrorShown() { state = state.copy(error = null) }

    fun updateCrianca() {
        if (state.nome.isBlank()) {
            state = state.copy(error = "O nome não pode ficar em branco!")
            return
        }

        val criancaAtualizada = Crianca(
            id = state.id,
            nome = state.nome,
            dataNascimento = state.dataNascimento,
            nivelSuporte = state.nivelSuporte,
            observacoes = state.observacoes,
            idDoPai = state.idDoPai
        )

        val result = dbHelper.updateCrianca(criancaAtualizada)
        if (result > 0) {
            state = state.copy(updateSuccess = true)
        } else {
            state = state.copy(error = "Falha ao atualizar os dados.")
        }
    }

    fun deleteCrianca() {
        val result = dbHelper.deleteCrianca(state.id)
        if (result > 0) {
            state = state.copy(deleteSuccess = true)
        } else {
            state = state.copy(error = "Falha ao excluir a criança.")
        }
    }
}

class EditCriancaViewModelFactory(
    private val dbHelper: UserDatabaseHelper,
    private val criancaId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditCriancaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditCriancaViewModel(dbHelper, criancaId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

