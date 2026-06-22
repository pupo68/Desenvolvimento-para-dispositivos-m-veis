package br.edu.pucpr.listacompras.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Model for shopping list item
 * Room entity with auto-generated primary key
 */
@Entity(tableName = "item_compra")
data class ItemCompra(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val quantidade: String,
    val isComprado: Boolean = false
)