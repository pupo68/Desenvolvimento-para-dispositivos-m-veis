package br.edu.pucpr.listacompras.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import br.edu.pucpr.listacompras.data.AppDatabase
import br.edu.pucpr.listacompras.model.ItemCompra
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.junit.Test

import java.io.IOException

/**
 * Test for ItemCompraDao using Room in-memory database
 */
class ItemCompraDaoTest {

    private lateinit var itemCompraDao: ItemCompraDao
    private lateinit var database: AppDatabase

    @Before
    fun setup() {
        // Create in-memory database for testing
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        itemCompraDao = database.itemCompraDao()
    }

    @After
    @Throws(IOException::class)
    fun teardown() {
        database.close()
    }

    @Test
    fun testInsertItem() = runTest {
        val item = ItemCompra(nome = "Arroz", quantidade = "5 kg")
        
        val insertId = itemCompraDao.insertItem(item)
        
        assertTrue(insertId > 0)
    }

    @Test
    fun testInsertedItemCanBeRetrieved() = runTest {
        val item = ItemCompra(nome = "Feijão", quantidade = "2 kg")
        itemCompraDao.insertItem(item)
        
        val items = itemCompraDao.getAllItems().first()
        
        assertEquals(1, items.size)
        assertEquals("Feijão", items[0].nome)
        assertEquals("2 kg", items[0].quantidade)
        assertFalse(items[0].isComprado)
    }

    @Test
    fun testUpdateItem() = runTest {
        val item = ItemCompra(nome = "Óleo", quantidade = "1 litro")
        itemCompraDao.insertItem(item)
        
        val items = itemCompraDao.getAllItems().first()
        val insertedItem = items[0]
        
        val updatedItem = insertedItem.copy(nome = "Óleo de Soja", quantidade = "2 litros")
        itemCompraDao.updateItem(updatedItem)
        
        val updatedItems = itemCompraDao.getAllItems().first()
        assertEquals(1, updatedItems.size)
        assertEquals("Óleo de Soja", updatedItems[0].nome)
        assertEquals("2 litros", updatedItems[0].quantidade)
    }

    @Test
    fun testDeleteItem() = runTest {
        val item = ItemCompra(nome = "Sabão", quantidade = "1 un")
        itemCompraDao.insertItem(item)
        
        val items = itemCompraDao.getAllItems().first()
        assertEquals(1, items.size)
        
        itemCompraDao.deleteItem(items[0])
        
        val remainingItems = itemCompraDao.getAllItems().first()
        assertEquals(0, remainingItems.size)
    }

    @Test
    fun testGetAllItemsReturnsOrderedList() = runTest {
        // Insert items in random order
        itemCompraDao.insertItem(ItemCompra(nome = "Zebra", quantidade = "1"))
        itemCompraDao.insertItem(ItemCompra(nome = "Apple", quantidade = "2"))
        itemCompraDao.insertItem(ItemCompra(nome = "Banana", quantidade = "3", isComprado = true))
        
        val items = itemCompraDao.getAllItems().first()
        
        // Should be ordered: não-comprados primero (alfabético), depois comprados
        assertEquals(3, items.size)
        assertEquals("Apple", items[0].nome)  // First non-purchased (alphabetical)
        assertEquals("Zebra", items[1].nome)  // Second non-purchased
        assertEquals("Banana", items[2].nome) // Purchased (at end)
        assertTrue(items[2].isComprado)
    }

    @Test
    fun testMultipleItems() = runTest {
        for (i in 1..5) {
            itemCompraDao.insertItem(ItemCompra(nome = "Item $i", quantidade = "$i un"))
        }
        
        val items = itemCompraDao.getAllItems().first()
        assertEquals(5, items.size)
    }

    @Test
    fun testToggleItemStatus() = runTest {
        val item = ItemCompra(nome = "Test", quantidade = "1", isComprado = false)
        itemCompraDao.insertItem(item)
        
        val items = itemCompraDao.getAllItems().first()
        val insertedItem = items[0]
        assertFalse(insertedItem.isComprado)
        
        val toggledItem = insertedItem.copy(isComprado = true)
        itemCompraDao.updateItem(toggledItem)
        
        val updatedItems = itemCompraDao.getAllItems().first()
        assertTrue(updatedItems[0].isComprado)
    }
}