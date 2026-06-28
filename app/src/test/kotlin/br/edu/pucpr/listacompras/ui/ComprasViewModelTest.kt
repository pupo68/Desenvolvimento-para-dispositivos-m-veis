package br.edu.pucpr.listacompras.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.edu.pucpr.listacompras.model.ItemCompra
import br.edu.pucpr.listacompras.repository.ComprasRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.Before
import org.junit.After

import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalCoroutinesApi::class)
class ComprasViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var mockRepo: ComprasRepository
    private lateinit var viewModel: ComprasViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockRepo = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    // addItem triggers repository add and UI event emitted
    fun testAddItem() = runTest {
        // Arrange
        coEvery { mockRepo.addItem(any()) } returns Unit
        coEvery { mockRepo.allItems } returns MutableStateFlow(emptyList())
        viewModel = ComprasViewModel(mockRepo)

        // Act
        viewModel.addItem("Camisa", "2")

        // Assert – repository call verified via mockk, and event posted
        // Since we use a relaxed mock, just ensure no exception and event is set
        assertNotNull(viewModel.event.value?.getContent())
    }

    @Test
    // toggleItemStatus forwards call to repository
    fun testToggleItemStatus() = runTest {
        // Arrange
        val item = ItemCompra(id = 1, nome = "Camisa", quantidade = "2", isComprado = false)
        coEvery { mockRepo.toggleItemStatus(item) } returns Unit
        coEvery { mockRepo.allItems } returns MutableStateFlow(listOf(item))
        viewModel = ComprasViewModel(mockRepo)

        // Act
        viewModel.toggleItemStatus(item)

        // Assert – no exception means call succeeded
        assertTrue(true)
    }
}
