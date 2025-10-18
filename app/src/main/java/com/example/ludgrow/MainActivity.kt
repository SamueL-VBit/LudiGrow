// Em: app/src/main/java/com/example/ludgrow/MainActivity.kt
package com.example.ludgrow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ludgrow.ui.telas.AppNavigation // <-- IMPORTANTE: Importar nossa navegação
import com.example.ludgrow.ui.theme.LudiGrowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LudiGrowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // A única coisa que a MainActivity faz é carregar a navegação
                    AppNavigation()
                }
            }
        }
    }
}

