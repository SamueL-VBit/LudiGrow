package com.example.ludgrow.ui.telas

// Em: app/src/main/java/com/example/ludgrow/ui/telas/LoginScreen.kt

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (userId: Int, userType: String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.state

    // Efeito para mostrar Toasts de erro
    LaunchedEffect(key1 = state.error) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.onErrorShown()
        }
    }

    // Efeito para navegar quando o login for bem-sucedido
    LaunchedEffect(key1 = state.loginSuccess) {
        if (state.loginSuccess) {
            Toast.makeText(context, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
            onLoginSuccess(state.loggedInUserId!!, state.userType)
            viewModel.onLoginHandled()
        }
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("LudiGrow - Login", fontSize = 20.sp) }) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFE3F2FD)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            ) {
                Text("Bem-vindo ao LudiGrow!", fontSize = 22.sp, color = Color(0xFF1565C0))
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = state.email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    label = { Text("E-mail") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = state.password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { Text("Senha") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Selecione o tipo de usuário:")
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = state.userType == "Pai",
                        onClick = { viewModel.onUserTypeChange("Pai") }
                    )
                    Text("Pai", modifier = Modifier.padding(end = 16.dp))
                    RadioButton(
                        selected = state.userType == "AT",
                        onClick = { viewModel.onUserTypeChange("AT") }
                    )
                    Text("AT")
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { viewModel.login() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Entrar")
                }
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(onClick = onNavigateToRegister) {
                    Text("Não tem uma conta? Cadastre-se")
                }
            }
        }
    }
}