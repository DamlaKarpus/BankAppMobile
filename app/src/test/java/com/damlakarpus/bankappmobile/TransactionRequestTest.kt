package com.damlakarpus.bankappmobile

import com.damlakarpus.bankappmobile.data.model.TransactionRequest
import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal

class TransactionRequestTest {

    @Test
    fun `transactionRequest fields should be assigned correctly`() {
        val request = TransactionRequest(
            accountIban = "TR111",
            targetAccountIban = "TR222",
            amount = BigDecimal(100)
        )

        assertEquals("TR111", request.accountIban)
        assertEquals("TR222", request.targetAccountIban)
        assertEquals(BigDecimal(100), request.amount)
    }

    @Test
    fun `transactionRequest equality should work correctly`() {
        val r1 = TransactionRequest("TR111", "TR222", BigDecimal(50))
        val r2 = TransactionRequest("TR111", "TR222", BigDecimal(50))

        // Kotlin data class otomatik equals ve hashCode verir
        assertEquals(r1, r2)
        assertEquals(r1.hashCode(), r2.hashCode())
    }

    @Test
    fun `transactionRequest copy should create new object with same values`() {
        val original = TransactionRequest("TR111", "TR222", BigDecimal(200))
        val copy = original.copy()

        assertEquals(original, copy)
        assertNotSame(original, copy) // aynı değer ama farklı referans
    }

    @Test
    fun `transactionRequest copy can override single field`() {
        val original = TransactionRequest("TR111", "TR222", BigDecimal(200))
        val modified = original.copy(amount = BigDecimal(300))

        assertEquals("TR111", modified.accountIban)
        assertEquals("TR222", modified.targetAccountIban)
        assertEquals(BigDecimal(300), modified.amount)
    }
}
