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
 * Implements CRUD operations and category-based queries
 */
@Dao
interface ItemCompraDao {

    @Insert
    suspend fun insertItem(item: ItemCompra): Long

    @Update
    suspend fun updateItem(item: ItemCompra): Int

    @Delete
    suspend fun deleteItem(item: ItemCompra): Int

    @Query("SELECT * FROM item_compra ORDER BY isComprado ASC, nome ASC")
    fun getAllItems(): Flow<List<ItemCompra>>

    @Query("SELECT * FROM item_compra WHERE categoria = :categoria ORDER BY isComprado ASC, nome ASC")
    fun getItemsByCategoria(categoria: String): Flow<List<ItemCompra>>

    @Query("SELECT COUNT(*) FROM item_compra")
    fun getTotalCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM item_compra WHERE isComprado = 1")
    fun getCompradoCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM item_compra WHERE isComprado = 0")
    fun getPendenteCount(): Flow<Int>

    @Query("SELECT categoria, COUNT(*) as count FROM item_compra GROUP BY categoria")
    fun getCountByCategoria(): Flow<List<CategoriaCount>>

    /** Result row for category count query */
    data class CategoriaCount(val categoria: String, val count: Int)
}
