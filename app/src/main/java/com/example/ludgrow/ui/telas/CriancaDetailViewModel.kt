// Caminho do arquivo: app/src/main/java/com/example/ludgrow/ui/telas/CriancaDetailViewModel.kt
package com.example.ludgrow.ui.telas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ludgrow.data.Atividade
import com.example.ludgrow.data.Crianca
import com.example.ludgrow.data.UserDatabaseHelper

// Estado que a tela de detalhes vai usar
data class CriancaDetailState(
    val crianca: Crianca? = null,
    val atividades: List<Atividade> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class CriancaDetailViewModel(
    private val dbHelper: UserDatabaseHelper,
    private val criancaId: Int
) : ViewModel() {

    var state by mutableStateOf(CriancaDetailState())
        private set

    init {
        loadCriancaDetails()
    }

    // Função para a UI pedir um recarregamento dos dados.
    // Útil para quando se volta de outra tela (como a de adicionar atividade).
    fun refreshData() {
        loadCriancaDetails()
    }

    private fun loadCriancaDetails() {
        state = state.copy(isLoading = true)
        val crianca = dbHelper.getSingleCrianca(criancaId)

        if (crianca != null) {
            // 1. Busca a lista de atividades associadas a esta criança
            val atividades = dbHelper.getAtividadesDaCrianca(criancaId)

            // 2. Atualiza o estado com os dados da criança E a lista de atividades
            state = state.copy(
                crianca = crianca,
                atividades = atividades,
                isLoading = false
            )
        } else {
            state = state.copy(error = "Criança não encontrada.", isLoading = false)
        }
    }
    // Em: app/src/main/java/com/example/ludgrow/ui/telas/CriancaDetailViewModel.kt

// ... (dentro da classe CriancaDetailViewModel, após a função loadCriancaDetails)

    fun deleteAtividade(atividadeId: Int) {
        val result = dbHelper.deleteAtividade(atividadeId)
        if (result > 0) {
            // Se a exclusão deu certo, simplesmente recarrega a lista
            refreshData()
        } else {
            // Opcional: Tratar erro, se desejar
            state = state.copy(error = "Falha ao excluir atividade.")
        }
    }

}

// Factory para construir o ViewModel com os parâmetros necessários
class CriancaDetailViewModelFactory(
    private val dbHelper: UserDatabaseHelper,
    private val criancaId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CriancaDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CriancaDetailViewModel(dbHelper, criancaId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
