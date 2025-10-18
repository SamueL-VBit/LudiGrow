// Caminho do arquivo: app/src/main/java/com/example/ludgrow/ui/telas/EditCriancaScreen.kt
package com.example.ludgrow.ui.telas

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCriancaScreen(
    viewModel: EditCriancaViewModel,
    onUpdateSuccess: () -> Unit,
    onDeleteSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.state

    LaunchedEffect(key1 = state.error) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.onErrorShown()
        }
    }

    LaunchedEffect(key1 = state.updateSuccess) {
        if (state.updateSuccess) {
            Toast.makeText(context, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show()
            onUpdateSuccess()
            viewModel.onUpdateHandled()
        }
    }

    LaunchedEffect(key1 = state.deleteSuccess) {
        if (state.deleteSuccess) {
            Toast.makeText(context, "Criança excluída com sucesso!", Toast.LENGTH_SHORT).show()
            onDeleteSuccess()
            viewModel.onDeleteHandled()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Criança") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.deleteCrianca() }) {
                        Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = Color.Red)
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = state.nome,
                    onValueChange = { viewModel.onNomeChange(it) },
                    label = { Text("Nome Completo") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.dataNascimento,
                    onValueChange = { viewModel.onDataNascimentoChange(it) },
                    label = { Text("Data de Nascimento (DD/MM/AAAA)") },
                    modifier = Modifier.fillMaxWidth()
                )

                // ******** CÓDIGO CORRIGIDO AQUI ********
                OutlinedTextField(
                    value = state.nivelSuporte,
                    onValueChange = { viewModel.onNivelSuporteChange(it) },
                    label = { Text("Nível de Suporte") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.observacoes,
                    onValueChange = { viewModel.onObservacoesChange(it) },
                    label = { Text("Observações") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { viewModel.updateCrianca() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Salvar Alterações")
                }
            }
        }
    }
}
