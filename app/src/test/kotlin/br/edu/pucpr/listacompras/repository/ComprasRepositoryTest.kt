package br.edu.pucpr.listacompras.repository

import br.edu.pucpr.listacompras.db.ItemCompraDao
import br.edu.pucpr.listacompras.model.ItemCompra
import io.mockk.*
import io.mockk.coEvery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Test for ComprasRepository using MockK
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ComprasRepositoryTest {

    private lateinit var mockDao: ItemCompraDao
    private lateinit var repository: ComprasRepository

    @Before
    fun setup() {
        mockDao = mockk(relaxed = true)
        repository = ComprasRepository(mockDao)
    }

    @After
    fun teardown() {
        clearAllMocks()
    }

    @Test
    fun testAddItemDelegatesToDaoInsert() = runTest {
        // Arrange
        val item = ItemCompra(nome = "Arroz", quantidade = "5 kg")
        coEvery { mockDao.insertItem(item) } returns 1L
      
        // Act
        repository.addItem(item)
      
        // Assert
        coVerify(exactly = 1) { mockDao.insertItem(item) }
        }

    @Test
    fun testUpdateItemDelegatesToDaoUpdate() = runTest {
        // Arrange
        val item = ItemCompra(id = 1, nome = "Feijão", quantidade = "2 kg")
        coEvery { mockDao.updateItem(item) } returns 1
      
        // Act
        repository.updateItem(item)
      
        // Assert
        coVerify(exactly = 1) { mockDao.updateItem(item) }
        }

    @Test
    fun testDeleteItemDelegatesToDaoDelete() = runTest {
        // Arrange
        val item = ItemCompra(id = 1, nome = "Óleo", quantidade = "1 un")
        coEvery { mockDao.deleteItem(item) } returns 1
      
        // Act
        repository.deleteItem(item)
      
        // Assert
        coVerify(exactly = 1) { mockDao.deleteItem(item) }
        }

    @Test
    fun testAllItemsReturnsDaoFlow() = runTest {
        // Arrange
        val items = listOf(
            ItemCompra(id = 1, nome = "Item 1", quantidade = "1 un"),
            ItemCompra(id = 2, nome = "Item 2", quantidade = "2 un")
        )
        coEvery { mockDao.getAllItems() } returns flowOf(items)
        
        // Act
        val result = repository.allItems.first()
      
        // Assert
        assertEquals(2, result.size)
        assertEquals("Item 1", result[0].nome)
        assertEquals("Item 2", result[1].nome)
        coVerify(exactly = 1) { mockDao.getAllItems() }
        }

    @Test
    fun testToggleItemStatusCallsUpdateWithToggledItem() = runTest {
        // Arrange
        val originalItem = ItemCompra(id = 1, nome = "Test", quantidade = "1", isComprado = false)
        coEvery { mockDao.updateItem(any()) } returns 1
        
        // Act
        repository.toggleItemStatus(originalItem)
      
        // Assert
        coVerify(exactly = 1) { 
            mockDao.updateItem(
                withArg { item ->
                    assertEquals(originalItem.id, item.id)
                    assertEquals(originalItem.nome, item.nome)
                    assertEquals(originalItem.quantidade, item.quantidade)
                    assertTrue(item.isComprado) // Should be toggled to true
                }
            )
        }
        }

    @Test
    fun testToggleItemStatusWhenAlreadyPurchased() = runTest {
        // Arrange
        val originalItem = ItemCompra(id = 1, nome = "Test", quantidade = "1", isComprado = true)
        coEvery { mockDao.updateItem(any()) } returns 1
        
        // Act
        repository.toggleItemStatus(originalItem)
      
        // Assert
        coVerify(exactly = 1) { 
            mockDao.updateItem(
                withArg { item ->
                    assertEquals(originalItem.id, item.id)
                    assertFalse(item.isComprado) // Should be toggled to false
                }
            )
        }
        }

    @Test
    fun testRepositoryIsEmptyInitially() = runTest {
        // Arrange
        coEvery { mockDao.getAllItems() } returns flowOf(emptyList())
      
        // Act
        val result = repository.allItems.first()
      
        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun testAddMultipleItems() = runTest {
        // Arrange
        val items = listOf(
            ItemCompra(nome = "Item 1", quantidade = "1"),
            ItemCompra(nome = "Item 2", quantidade = "2"),
            ItemCompra(nome = "Item 3", quantidade = "3")
        )

         coEvery { mockDao.insertItem(any()) } returns 1L

         // Act
         items.forEach { repository.addItem(it) }

         // Assert
         
         coVerify(exactly = 3) { mockDao.insertItem(any()) }
        }
}
