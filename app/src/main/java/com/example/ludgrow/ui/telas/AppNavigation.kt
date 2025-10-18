// Caminho do arquivo: app/src/main/java/com/example/ludgrow/ui/telas/AppNavigation.kt
package com.example.ludgrow.ui.telas

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ludgrow.data.UserDatabaseHelper

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val dbHelper = remember { UserDatabaseHelper(context) }

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(dbHelper))
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { userId, userType ->
                    val destination = if (userType == "Pai") "home_pai" else "home_at"
                    navController.navigate("$destination/$userId") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            val registerViewModel: RegisterViewModel = viewModel(factory = RegisterViewModelFactory(dbHelper))
            RegisterScreen(
                viewModel = registerViewModel,
                onRegisterSuccess = { navController.popBackStack() },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("home_pai/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
            if (userId != null) {
                val homeViewModel: HomeViewModel = viewModel(
                    key = "home_pai_$userId",
                    factory = HomeViewModelFactory(dbHelper, userId, "Pai")
                )
                HomeScreen(
                    viewModel = homeViewModel,
                    onAddCrianca = {
                        navController.navigate("add_crianca/$userId")
                    },
                    onCriancaClicked = { criancaId ->
                        navController.navigate("crianca_detail/$criancaId")
                    }
                )
            }
        }

        composable("crianca_detail/{criancaId}") { backStackEntry ->
            val criancaId = backStackEntry.arguments?.getString("criancaId")?.toIntOrNull()
            if (criancaId != null) {
                val detailViewModel: CriancaDetailViewModel = viewModel(
                    key = "crianca_detail_$criancaId",
                    factory = CriancaDetailViewModelFactory(dbHelper, criancaId)
                )
                CriancaDetailScreen(
                    viewModel = detailViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onEditCrianca = { id ->
                        navController.navigate("edit_crianca/$id")
                    },
                    onAddAtividade = { id ->
                        navController.navigate("add_atividade/$id")
                    },
                    // AÇÃO 4: Ligar o callback à navegação
                    onAtividadeClicked = { atividadeId ->
                        navController.navigate("edit_atividade/$atividadeId")
                    }
                )
            }
        }

        composable("add_crianca/{paiId}") { backStackEntry ->
            val paiId = backStackEntry.arguments?.getString("paiId")?.toIntOrNull()
            if (paiId != null) {
                val addCriancaViewModel: AddCriancaViewModel = viewModel(
                    key = "add_crianca_$paiId",
                    factory = AddCriancaViewModelFactory(dbHelper, paiId)
                )
                AddCriancaScreen(
                    viewModel = addCriancaViewModel,
                    onSaveSuccess = { navController.popBackStack() },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        composable("edit_crianca/{criancaId}") { backStackEntry ->
            val criancaId = backStackEntry.arguments?.getString("criancaId")?.toIntOrNull()
            if (criancaId != null) {
                val editCriancaViewModel: EditCriancaViewModel = viewModel(
                    key = "edit_crianca_$criancaId",
                    factory = EditCriancaViewModelFactory(dbHelper, criancaId)
                )
                EditCriancaScreen(
                    viewModel = editCriancaViewModel,
                    onUpdateSuccess = { navController.popBackStack() },
                    onDeleteSuccess = {
                        navController.popBackStack(route = "home_pai/{userId}", inclusive = false)
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        composable("add_atividade/{criancaId}") { backStackEntry ->
            val criancaId = backStackEntry.arguments?.getString("criancaId")?.toIntOrNull()
            if (criancaId != null) {
                val addAtividadeViewModel: AddAtividadeViewModel = viewModel(
                    key = "add_atividade_$criancaId",
                    factory = AddAtividadeViewModelFactory(dbHelper, criancaId)
                )
                AddAtividadeScreen(
                    viewModel = addAtividadeViewModel,
                    onSaveSuccess = { navController.popBackStack() },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        // AÇÃO 5: Adicionar a nova rota de edição de atividade
        composable("edit_atividade/{atividadeId}") { backStackEntry ->
            val atividadeId = backStackEntry.arguments?.getString("atividadeId")?.toIntOrNull()
            if (atividadeId != null) {
                val editAtividadeViewModel: EditAtividadeViewModel = viewModel(
                    key = "edit_atividade_$atividadeId",
                    factory = EditAtividadeViewModelFactory(dbHelper, atividadeId)
                )
                EditAtividadeScreen(
                    viewModel = editAtividadeViewModel,
                    onUpdateSuccess = { navController.popBackStack() },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        composable("home_at/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
            Text("Home do AT - ID do Usuário: $userId (a ser criada)")
        }
    }
}
