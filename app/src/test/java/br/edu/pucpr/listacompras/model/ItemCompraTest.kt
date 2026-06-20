package br.edu.pucpr.listacompras.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ItemCompraTest {

    @Test
    fun testCriacaoItemCompraComValoresPadrao() {
        val item = ItemCompra(
            nome = "Arroz",
            quantidade = "5 kg"
        )
        assertEquals(0, item.id)
        assertEquals("Arroz", item.nome)
        assertEquals("5 kg", item.quantidade)
        assertFalse(item.isComprado)
    }

    @Test
    fun testAlterarEstadoDeCompraDoItem() {
        val item = ItemCompra(
            id = 1,
            nome = "Feijão",
            quantidade = "2 kg",
            isComprado = false
        )
        val itemComprado = item.copy(isComprado = true)
        
        assertTrue(itemComprado.isComprado)
        assertEquals(item.id, itemComprado.id)
        assertEquals(item.nome, itemComprado.nome)
        assertEquals(item.quantidade, itemComprado.quantidade)
    }

    @Test
    fun testEdicaoDeValoresDoItem() {
        val item = ItemCompra(
            id = 2,
            nome = "Óleo",
            quantidade = "1 litro"
        )
        val itemEditado = item.copy(nome = "Óleo de Soja", quantidade = "2 litros")
        
        assertEquals("Óleo de Soja", itemEditado.nome)
        assertEquals("2 litros", itemEditado.quantidade)
        assertEquals(item.id, itemEditado.id)
        assertEquals(item.isComprado, itemEditado.isComprado)
    }
}
