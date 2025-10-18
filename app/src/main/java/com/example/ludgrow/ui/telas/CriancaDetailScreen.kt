// Caminho do arquivo: app/src/main/java/com/example/ludgrow/ui/telas/CriancaDetailScreen.kt
package com.example.ludgrow.ui.telas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ludgrow.data.Atividade

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriancaDetailScreen(
    viewModel: CriancaDetailViewModel,
    onNavigateBack: () -> Unit,
    onEditCrianca: (criancaId: Int) -> Unit,
    onAddAtividade: (criancaId: Int) -> Unit,
    // AÇÃO 1: Adicionar o callback para navegar para a edição da atividade
    onAtividadeClicked: (atividadeId: Int) -> Unit
) {
    val state = viewModel.state

    LaunchedEffect(key1 = Unit) {
        viewModel.refreshData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.crianca?.nome ?: "Detalhes") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { state.crianca?.id?.let { onEditCrianca(it) } }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar Criança")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { state.crianca?.id?.let { onAddAtividade(it) } }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Atividade")
            }
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.crianca == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Erro: Criança não encontrada.")
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("Detalhes", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Nível de Suporte: ${state.crianca.nivelSuporte}", fontSize = 16.sp)
                Text("Observações: ${state.crianca.observacoes}", fontSize = 16.sp)
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text("Atividades Recentes", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))

                if (state.atividades.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Nenhuma atividade cadastrada ainda.")
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.atividades) { atividade ->
                            AtividadeCard(
                                atividade = atividade,
                                // AÇÃO 2: Passar a ação de clique para o card
                                onClick = { onAtividadeClicked(atividade.id) },
                                onDelete = { viewModel.deleteAtividade(atividade.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AtividadeCard(
    atividade: Atividade,
    onClick: () -> Unit, // Recebe a função de clique
    onDelete: () -> Unit
) {
    Card(
        // AÇÃO 3: Adicionar o modificador clickable ao Card
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = atividade.nome,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Excluir Atividade", tint = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Data: ${atividade.dataRealizacao}", style = MaterialTheme.typography.bodySmall)
            if (atividade.areaDesenvolvimento.isNotBlank()) {
                Text(
                    text = "Área: ${atividade.areaDesenvolvimento}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            if (atividade.descricao.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = atividade.descricao, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
