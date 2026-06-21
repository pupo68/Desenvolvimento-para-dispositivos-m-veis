package br.edu.pucpr.listacompras.repository

import br.edu.pucpr.listacompras.db.ItemCompraDao
import br.edu.pucpr.listacompras.model.ItemCompra
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Repository for managing ItemCompra data
 * Abstracts the data layer from the ViewModel as per SDD section 4.1.4 and spec.md section 2.2
 */
class ComprasRepository(private val dao: ItemCompraDao) {

    // TODO: Implement getAllItems method
    val allItems: Flow<List<ItemCompra>> = dao.getAllItems()

    // TODO: Implement addItem method
    suspend fun addItem(item: ItemCompra) {
        dao.insertItem(item)
    }

    // TODO: Implement updateItem method
    suspend fun updateItem(item: ItemCompra) {
        dao.updateItem(item)
    }

    // TODO: Implement deleteItem method
    suspend fun deleteItem(item: ItemCompra) {
        dao.deleteItem(item)
    }

    // TODO: Implement toggleItemStatus method (from spec.md section 2.3 and tdd.md example)
    suspend fun toggleItemStatus(item: ItemCompra) {
        val toggledItem = item.copy(isComprado = !item.isComprado)
        dao.updateItem(toggledItem)
    }
}