package com.example.ludgrow.ui.telas

// Em: app/src/main/java/com/example/ludgrow/ui/telas/AddCriancaScreen.kt

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
fun AddCriancaScreen(
    viewModel: AddCriancaViewModel,
    onSaveSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.state

    // Efeito para tratar erros
    LaunchedEffect(key1 = state.error) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.onErrorShown()
        }
    }

    // Efeito para voltar à tela anterior após salvar
    LaunchedEffect(key1 = state.saveSuccess) {
        if (state.saveSuccess) {
            Toast.makeText(context, "Criança salva com sucesso!", Toast.LENGTH_SHORT).show()
            onSaveSuccess()
            viewModel.onSaveHandled()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Nova Criança") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
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

            // Aqui você poderia usar um DropdownMenu para Nível de Suporte,
            // mas um TextField simples funciona para começar.
            OutlinedTextField(
                value = state.nivelSuporte,
                onValueChange = { viewModel.onNivelSuporteChange(it) },
                label = { Text("Nível de Suporte (Ex: Nível 1)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.observacoes,
                onValueChange = { viewModel.onObservacoesChange(it) },
                label = { Text("Observações (Opcional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.saveCrianca() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Salvar Criança")
            }
        }
    }
}