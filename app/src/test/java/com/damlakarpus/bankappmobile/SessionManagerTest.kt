package com.damlakarpus.bankappmobile

import com.damlakarpus.bankappmobile.common.SessionManager
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SessionManagerTest {

    @Before
    fun setup() {
        SessionManager.clearSession()
    }

    @Test
    fun `saveSession should update fields correctly`() {
        // Act
        SessionManager.saveSession(
            token = "abc123",
            userId = 42,
            iban = "TR123",
            balance = 100.0,
            userName = "TestUser"
        )

        // Assert
        assertEquals("abc123", SessionManager.token)
        assertEquals(42, SessionManager.userId)
        assertEquals("TR123", SessionManager.iban)
        assertNotNull("balance null olmamalı", SessionManager.balance)
        assertEquals(100.0, SessionManager.balance!!, 0.01) // <- nullable değilmiş gibi karşılaştır
        assertEquals("TestUser", SessionManager.userName)
    }

    @Test
    fun `clearSession should reset all fields`() {
        SessionManager.saveSession("abc123", 1, "TR123", 50.0, "User")
        SessionManager.clearSession()

        assertNull(SessionManager.token)
        assertNull(SessionManager.userId)
        assertNull(SessionManager.iban)
        assertNull(SessionManager.balance)   // balance da null olmalı
        assertNull(SessionManager.userName)
    }
}
