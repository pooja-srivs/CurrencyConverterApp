package com.example.currencyconverterapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.currencyconverterapp.Mocks
import com.example.currencyconverterapp.domain.usecase.currency.FetchCurrencyUseCase
import com.example.currencyconverterapp.domain.usecase.convert.FetchCurrencyConvertUseCase
import com.example.currencyconverterapp.ui.adapter.CurrencyItem
import com.example.currencyconverterapp.ui.mapper.toDomainModel
import com.example.currencyconverterapp.ui.model.CurrencyRateUiModel
import com.example.currencyconverterapp.ui.model.UIState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CurrencyViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fetchCurrencyConvertUseCase: FetchCurrencyConvertUseCase
    private lateinit var fetchCurrencyUseCase: FetchCurrencyUseCase
    private lateinit var viewModel: CurrencyViewModel


    @Before
    fun setUp() {
        fetchCurrencyConvertUseCase = mockk()
        fetchCurrencyUseCase = mockk()
        viewModel = CurrencyViewModel(fetchCurrencyUseCase, fetchCurrencyConvertUseCase)
    }

    @Test
    fun `test FetchCurrencies should emits Success state`() = runTest {
        val expected = Mocks.currencyRateUiModel
        coEvery { fetchCurrencyUseCase.getCurrencies() } coAnswers {
            flow { emit(UIState.Success(expected)) }
        }
        viewModel.fetchCurrencies()
        advanceUntilIdle()
        val response = viewModel.currencyLiveData.getOrAwaitValue()

        Assert.assertTrue(response is UIState.Success)
        Assert.assertEquals(UIState.Success(expected), response)
    }

    @Test
    fun `test FetchCurrencies should emit Failure when flow emits Failure state`() = runTest {
        val expected = RuntimeException("Flow error")
        coEvery { fetchCurrencyUseCase.getCurrencies() } coAnswers {
            flow { throw expected }
        }
        viewModel.fetchCurrencies()
        advanceUntilIdle()

        val response = viewModel.currencyLiveData.getOrAwaitValue()

        Assert.assertTrue(response is UIState.Failure)
        Assert.assertEquals(UIState.Failure(expected), response)
    }

    @Test
    fun `test fetchCurrencies should emit failure state when exception occurs during data fetch`() = runTest {
        val exception = RuntimeException("Data fetch failed")
        coEvery { fetchCurrencyUseCase.getCurrencies() } throws exception

        viewModel.fetchCurrencies()
        advanceUntilIdle()
        val response = viewModel.currencyLiveData.getOrAwaitValue()
        Assert.assertTrue(response is UIState.Failure)
        Assert.assertEquals(exception.message, (response as UIState.Failure).throwable.message)
    }

    @Test
    fun `test to convert the amount with success result`() {
        val amount = 100.00
        val currencyRate = 1.0
        val currencyItem = Mocks.currencyItemList

        every { fetchCurrencyConvertUseCase.convert(
            amount, currencyRate, currencyItem.toDomainModel()
        ) } returns UIState.Success(currencyItem)

        viewModel.convert(
            amount, currencyRate, currencyItem
        )
        val state = viewModel.convertedAmountLiveData.getOrAwaitValue()
        Assert.assertTrue(state is UIState.Success)
        Assert.assertEquals(currencyItem, (state as UIState.Success).value)
    }

    @Test
    fun `convert with failure result`() {
        val amount = 100.0
        val currencyRate = 1.0
        val currencyItem = Mocks.currencyItemList
        val errorMsg = "Conversion error"
        val exception = Exception(errorMsg)
        every { fetchCurrencyConvertUseCase.convert(
            amount, currencyRate, currencyItem.toDomainModel()
        ) } returns UIState.Failure(exception)

        viewModel.convert(
            amount, currencyRate, currencyItem
        )

        val state = viewModel.convertedAmountLiveData.getOrAwaitValue()
        Assert.assertTrue(state is UIState.Failure)
        Assert.assertEquals(errorMsg, (state as UIState.Failure).throwable.message)
    }

    @Test
    fun `convert with 0 amount returns failure result`() {
        val amount = 0.0
        val currencyRate = 1.0
        val currencyItem = Mocks.currencyItemList
        val errorMsg = "Conversion error"
        val exception = Exception(errorMsg)
        every { fetchCurrencyConvertUseCase.convert(amount, currencyRate, currencyItem.toDomainModel())
        } returns UIState.Failure(exception)

        viewModel.convert(
            amount, currencyRate, currencyItem
        )
        val state = viewModel.convertedAmountLiveData.getOrAwaitValue()
        Assert.assertTrue(state is UIState.Failure)
        Assert.assertEquals(errorMsg, (state as UIState.Failure).throwable.message)
    }

    @Test
    fun `test setInputAmount and getInputAmount`() {
        val input = "123.45"
        val expected = 123.45

        viewModel.setInputAmount(input)
        val result = viewModel.getInputAmount()

        assertEquals(expected, result, 0.0)
    }

    @Test
    fun `test setInputAmount and getInputAmount with empty input`() {
        val input = ""
        val expected = 0.0

        viewModel.setInputAmount(input)
        val result = viewModel.getInputAmount()

        assertEquals(expected, result, 0.0)
    }

    @Test
    fun `test getSelectedCurrencyPosition and getSelectedCurrencyUiModel`() {
        val position = 2
        val currencyRateUiModel = mockk<CurrencyRateUiModel>()

        viewModel.selectedCurrency(position, currencyRateUiModel)
        val selectedPos = viewModel.getSelectedCurrencyPosition()
        val selectedUiModel = viewModel.getSelectedCurrencyUiModel()

        assertEquals(position, selectedPos)
        assertEquals(currencyRateUiModel, selectedUiModel)
    }

    @Test
    fun `test setCurrencyList and getCurrencyList with some CurrencyItem data`() {
        val currencyItem = Mocks.currencyItemList

        viewModel.setCurrencyList(currencyItem)
        val actual = viewModel.getCurrencyList()

        assertEquals(currencyItem.size, actual.size)
        assertEquals(currencyItem, actual)
    }

    @Test
    fun `test setCurrencyList and getCurrencyList with empty CurrencyItem data`() {
        val currencyItem = emptyList<CurrencyItem>()

        viewModel.setCurrencyList(currencyItem)
        val actual = viewModel.getCurrencyList()

        assertEquals(0, actual.size)
        assertEquals(currencyItem, actual)
    }

    @Test
    fun `test valid amount`() {
        val validInput = "10.5"
        val isValid = viewModel.isValidAmount(validInput)
        assertTrue(isValid)
    }

    @Test
    fun `test empty input`() {
        val emptyInput = ""
        val isValid = viewModel.isValidAmount(emptyInput)
        assertFalse(isValid)
    }

    @Test
    fun `test dot input`() {
        val dotInput = "."
        val isValid = viewModel.isValidAmount(dotInput)
        assertFalse(isValid)
    }

    @Test
    fun `test non-numeric input`() {
        val nonNumericInput = "abc"
        val isValid = viewModel.isValidAmount(nonNumericInput)
        assertFalse(isValid)
    }

    @Test
    fun `test null input`() {
        val nullInput: String? = null
        val isValid = viewModel.isValidAmount(nullInput ?: "")
        assertFalse(isValid)
    }
}