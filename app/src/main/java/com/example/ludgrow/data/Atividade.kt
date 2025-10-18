// Em: app/src/main/java/com/example/ludgrow/data/Atividade.kt
package com.example.ludgrow.data

data class Atividade(
    val id: Int = 0,
    val nome: String,
    val descricao: String,
    val areaDesenvolvimento: String, // Ex: "Cognitivo", "Motor"
    val dataRealizacao: String, // Data em que a atividade foi feita
    val idDaCrianca: Int // Chave estrangeira para a tabela de Crianças
)
