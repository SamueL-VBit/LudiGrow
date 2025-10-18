// Em: app/src/main/java/com/example/ludgrow/ui/telas/HomeScreen.kt
package com.example.ludgrow.ui.telas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect // <-- Importe o LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ludgrow.data.Crianca

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAddCrianca: () -> Unit,
    onCriancaClicked: (criancaId: Int) -> Unit
) {
    val state = viewModel.state

    // AÇÃO: Adicione este LaunchedEffect
    // A chave 'state.criancas.size' faz com que este bloco seja re-executado
    // se o tamanho da lista mudar, mas o 'Unit' garante que ele rode pelo menos
    // uma vez e toda vez que você voltar para a tela.
    LaunchedEffect(Unit) {
        viewModel.loadCriancas()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Minhas Crianças") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddCrianca) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Criança")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (state.criancas.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhuma criança cadastrada ainda.", fontSize = 18.sp)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.criancas) { crianca ->
                        CriancaCard(crianca = crianca, onClick = { onCriancaClicked(crianca.id) })
                    }
                }
            }
        }
    }
}

// O CriancaCard continua igual
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriancaCard(crianca: Crianca, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(crianca.nome, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Nível de Suporte: ${crianca.nivelSuporte}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
