package br.edu.pucpr.listacompras.repository

import br.edu.pucpr.listacompras.db.ItemCompraDao
import br.edu.pucpr.listacompras.model.ItemCompra
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing ItemCompra data
 * Abstracts the data layer from the ViewModel as per SDD section 4.1.4
 */
class ComprasRepository(private val dao: ItemCompraDao) {

    val allItems: Flow<List<ItemCompra>> = dao.getAllItems()

    fun getItemsByCategoria(categoria: String): Flow<List<ItemCompra>> =
        dao.getItemsByCategoria(categoria)

    val totalCount: Flow<Int> = dao.getTotalCount()
    val compradoCount: Flow<Int> = dao.getCompradoCount()
    val pendenteCount: Flow<Int> = dao.getPendenteCount()
    val countByCategoria: Flow<List<ItemCompraDao.CategoriaCount>> = dao.getCountByCategoria()

    suspend fun addItem(item: ItemCompra) {
        dao.insertItem(item)
    }

    suspend fun updateItem(item: ItemCompra) {
        dao.updateItem(item)
    }

    suspend fun deleteItem(item: ItemCompra) {
        dao.deleteItem(item)
    }

    suspend fun toggleItemStatus(item: ItemCompra) {
        val toggledItem = item.copy(isComprado = !item.isComprado)
        dao.updateItem(toggledItem)
    }
}
