// Em: app/src/main/java/com/example/ludgrow/data/Crianca.kt
package com.example.ludgrow.data

data class Crianca(
    val id: Int = 0,
    val nome: String,
    val dataNascimento: String,
    val nivelSuporte: String,
    val observacoes: String,
    val idDoPai: Int,
    val idDoAt: Int? = null // AT pode ser nulo
)
