package com.example.currencyconverterapp.domain.usecase

import app.cash.turbine.test
import com.example.currencyconverterapp.Mocks
import com.example.currencyconverterapp.data.repository.CurrencyRepository
import com.example.currencyconverterapp.domain.mapper.toUiModel
import com.example.currencyconverterapp.domain.model.CurrencyRate
import com.example.currencyconverterapp.domain.model.DomainResult
import com.example.currencyconverterapp.domain.usecase.currency.FetchCurrencyUseCaseImpl
import com.example.currencyconverterapp.ui.model.UIState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.coroutines.cancellation.CancellationException

class FetchCurrencyUseCaseImplTest {

    private lateinit var currencyRepository: CurrencyRepository
    private lateinit var fetchCurrencyUseCaseImpl: FetchCurrencyUseCaseImpl

    @Before
    fun setUp() {
        currencyRepository = mockk()
        fetchCurrencyUseCaseImpl = FetchCurrencyUseCaseImpl(currencyRepository)
    }

    @Test
    fun `test fetchCurrencyUseCase returns UIState Success`() = runTest {
        val response = Mocks.currencyRateList
        coEvery { currencyRepository.getCurrencyData() } coAnswers {
            flow { emit(DomainResult.Success(response)) }
        }
        val expected = UIState.Success(response.toUiModel())

        fetchCurrencyUseCaseImpl.getCurrencies().test {
            val emittedItem = awaitItem()
            Assert.assertTrue(emittedItem is UIState.Success)
            Assert.assertEquals(expected, emittedItem)
            awaitComplete()
        }
    }

    @Test
    fun `test fetchCurrencyUseCase returns UIState Failure`() = runTest {
        val exception = RuntimeException("Failed to fetch data")
        coEvery { currencyRepository.getCurrencyData() } coAnswers {
            flow { emit(DomainResult.Failure(exception)) }
        }
        val expected = UIState.Failure(exception)

        fetchCurrencyUseCaseImpl.getCurrencies().test {
            val emittedItem = awaitItem()
            Assert.assertTrue(emittedItem is UIState.Failure)
            Assert.assertEquals(expected, emittedItem)
            awaitComplete()
        }
    }

    @Test
    fun `test fetchCurrencyUseCase returns Exception`() = runTest {
        val exception = RuntimeException("Unexpected error")
        coEvery { currencyRepository.getCurrencyData() } throws exception

        fetchCurrencyUseCaseImpl.getCurrencies().test {
            val emittedItem = awaitItem()
            Assert.assertTrue(emittedItem is UIState.Failure)
            Assert.assertEquals(UIState.Failure(exception), emittedItem)
            awaitComplete()
        }
    }

    @Test
    fun `getCurrencies test coroutine cancellation`() = runTest {
        val response = Mocks.currencyRateList
        coEvery { currencyRepository.getCurrencyData() } coAnswers {
            flow { emit(DomainResult.Success(response)) }
        }

        val job = launch {
            fetchCurrencyUseCaseImpl.getCurrencies().test {
                val emittedFlow = awaitItem()
                Assert.assertTrue(emittedFlow is UIState.Success)
                Assert.assertEquals(UIState.Success(response.toUiModel()), emittedFlow)
                awaitComplete()
            }
        }
        job.cancelAndJoin()

        job.invokeOnCompletion { reason ->
            Assert.assertTrue(reason is CancellationException)
        }
    }

    @Test
    fun `getCurrencies delayed response`() = runTest {
        val response = Mocks.currencyRateList
        coEvery { currencyRepository.getCurrencyData() } coAnswers {
            delay(1000)
            flow { emit(DomainResult.Success(response)) }
        }
        val expected = UIState.Success(response.toUiModel())
        fetchCurrencyUseCaseImpl.getCurrencies().test {
            val emittedFlow = awaitItem()
            Assert.assertTrue(emittedFlow is UIState.Success)
            Assert.assertEquals(expected, emittedFlow)
            awaitComplete()
        }
    }

    @Test
    fun `test fetchCurrencyUseCase returns empty list`() = runTest {
        val response = emptyList<CurrencyRate>()
        coEvery { currencyRepository.getCurrencyData() } coAnswers {
            flow { emit(DomainResult.Success(response)) }
        }

        fetchCurrencyUseCaseImpl.getCurrencies().test {
            val emittedFlow = awaitItem()
            Assert.assertTrue(emittedFlow is UIState.Success)
            Assert.assertEquals(UIState.Success(response.toUiModel()), emittedFlow)
            awaitComplete()
        }
    }
}