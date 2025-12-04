package com.example.vinheira_agnello_f4.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produtos")
data class Produto(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val descricao: String? = null,
    val uva: String? = null,
    val ano: Int? = null,
    val preco: Double,
    val quantidade: Int
)

