package com.damlakarpus.bankappmobile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.damlakarpus.bankappmobile.data.model.TransactionRequest
import com.damlakarpus.bankappmobile.data.model.transaction.TransactionResponse
import com.damlakarpus.bankappmobile.repository.TransactionRepository
import com.damlakarpus.bankappmobile.viewmodel.TransactionViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()

    private lateinit var repository: TransactionRepository
    private lateinit var viewModel: TransactionViewModel

    @Before
    fun setup() {
        kotlinx.coroutines.Dispatchers.setMain(dispatcher)
        repository = mockk()
        viewModel = TransactionViewModel(repository)
    }

    @After
    fun tearDown() {
        kotlinx.coroutines.Dispatchers.resetMain()
    }

    @Test
    fun `transfer should update LiveData with success response`() = runTest {
        // Arrange
        val request = TransactionRequest("TR111", "TR222", BigDecimal(100))
        val fakeResponse = TransactionResponse(
            success = true,
            message = "Başarılı",
            transaction = null
        )

        coEvery { repository.transfer(any()) } returns fakeResponse

        // Act
        viewModel.transfer(request)
        dispatcher.scheduler.advanceUntilIdle()

        // Assert
        val result = viewModel.transactionResult.value
        assertNotNull(result)
        assertEquals(true, result?.success)
        assertEquals("Başarılı", result?.message)
    }

    @Test
    fun `transfer should handle failure response`() = runTest {
        // Arrange
        val request = TransactionRequest("TR111", "TR999", BigDecimal(50))
        val fakeResponse = TransactionResponse(
            success = false,
            message = "Yetersiz bakiye",
            transaction = null
        )

        coEvery { repository.transfer(any()) } returns fakeResponse

        // Act
        viewModel.transfer(request)
        dispatcher.scheduler.advanceUntilIdle()

        // Assert
        val result = viewModel.transactionResult.value
        assertNotNull(result)
        assertEquals(false, result?.success)
        assertEquals("Yetersiz bakiye", result?.message)
    }
}
