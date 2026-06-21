package br.edu.pucpr.listacompras.repository

import br.edu.pucpr.listacompras.db.ItemCompraDao
import br.edu.pucpr.listacompras.model.ItemCompra
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

/**
 * Test for ComprasRepository - Red phase of TDD (write failing tests first)
 */
class ComprasRepositoryTest {

    // TODO: Initialize mock dao and repository
    private lateinit var mockDao: ItemCompraDao
    private lateinit var repository: ComprasRepository

    @Before
    fun setup(): Unit {
        // TODO: Create mock DAO and repository
        // mockDao = mockk(relaxed = true)
        // every { mockDao.getAllItems() } returns flowOf(emptyList())
        // repository = ComprasRepository(mockDao)
    }

    @After
    fun teardown(): Unit {
        // TODO: Unmock if needed
        // unmockall(relaxed = true)
    }

    @Test
    fun `test addItem delegates to dao insert`(): Unit = TODO()

    @Test
    fun `test updateItem delegates to dao update`(): Unit = TODO()

    @Test
    fun `test deleteItem delegates to dao delete`(): Unit = TODO()

    @Test
    fun `test getAllItems returns repository flow`(): Unit = TODO()
}