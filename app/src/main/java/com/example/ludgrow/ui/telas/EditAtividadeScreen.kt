package com.example.ludgrow.ui.telas

// Caminho do arquivo: app/src/main/java/com/example/ludgrow/ui/telas/EditAtividadeScreen.kt

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAtividadeScreen(
    viewModel: EditAtividadeViewModel,
    onUpdateSuccess: () -> Unit,
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
            Toast.makeText(context, "Atividade atualizada com sucesso!", Toast.LENGTH_SHORT).show()
            viewModel.onUpdateHandled()
            onUpdateSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Atividade") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
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
                    label = { Text("Nome da Atividade") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.dataRealizacao,
                    onValueChange = { viewModel.onDataChange(it) },
                    label = { Text("Data de Realização (DD/MM/AAAA)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.areaDesenvolvimento,
                    onValueChange = { viewModel.onAreaChange(it) },
                    label = { Text("Área de Desenvolvimento") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.descricao,
                    onValueChange = { viewModel.onDescricaoChange(it) },
                    label = { Text("Descrição da Atividade") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { viewModel.updateAtividade() },
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