package br.edu.pucpr.listacompras.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.edu.pucpr.listacompras.model.ItemCompra
import kotlinx.coroutines.flow.Flow

/**
 * DAO for ItemCompra entity
 * Implements CRUD operations as per SDD section 4.1.2 and spec.md section 2.2
 */
@Dao
interface ItemCompraDao {

    // TODO: Implement insert method
    @Insert
    suspend fun insertItem(item: ItemCompra): Long

    // TODO: Implement update method
    @Update
    suspend fun updateItem(item: ItemCompra): Int

    // TODO: Implement delete method
    @Delete
    suspend fun deleteItem(item: ItemCompra): Int

    // TODO: Implement getAllItems method with ordering (not-comprados first)
    @Query("SELECT * FROM item_compra ORDER BY isComprado ASC, nome ASC")
    fun getAllItems(): Flow<List<ItemCompra>>
}