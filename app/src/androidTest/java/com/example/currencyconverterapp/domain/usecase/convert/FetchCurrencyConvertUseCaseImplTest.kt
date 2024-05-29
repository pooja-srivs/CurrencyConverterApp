package com.example.currencyconverterapp.domain.usecase.convert

import com.example.currencyconverterapp.Mocks
import com.example.currencyconverterapp.domain.model.ConvertedCurrencyItem
import com.example.currencyconverterapp.ui.mapper.toDomainModel
import com.example.currencyconverterapp.ui.model.UIState
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class FetchCurrencyConvertUseCaseImplTest {

    private lateinit var fetchCurrencyConvertUseCaseImpl: FetchCurrencyConvertUseCaseImpl

    @Before
    fun setUp() {
        fetchCurrencyConvertUseCaseImpl = FetchCurrencyConvertUseCaseImpl()
    }

    @Test
    fun `test to convert same currency rates`() {
        val currencyItem = Mocks.currencyItemList
        val result = fetchCurrencyConvertUseCaseImpl.convert(100.0, 1.0, currencyItem.toDomainModel())
        Assert.assertTrue(result is UIState.Success)
        Assert.assertEquals(currencyItem.size, (result as UIState.Success).value.size)
    }

    @Test
    fun `test to convert different currency rates`() {
        val currencyItem = Mocks.currencyItemList
        val result = fetchCurrencyConvertUseCaseImpl.convert(100.0, 1.0, currencyItem.toDomainModel())
        Assert.assertTrue(result is UIState.Success)
        Assert.assertEquals(currencyItem.size, (result as UIState.Success).value.size)
    }

    @Test
    fun `test to convert with zero amount returns failure state`() {
        val currencyItem = Mocks.currencyItemList
        val result = fetchCurrencyConvertUseCaseImpl.convert(0.0, 1.0, currencyItem.toDomainModel())
        Assert.assertTrue(result is UIState.Failure)
        val failureState = result as UIState.Failure
        Assert.assertTrue(failureState.throwable is IllegalArgumentException)
        Assert.assertEquals("101", failureState.throwable.message)
    }

    @Test
    fun `test to convert with negative amount throws exception`() {
        val currencyItem = Mocks.currencyItemList
        val result = fetchCurrencyConvertUseCaseImpl.convert(-100.0, 1.0, currencyItem.toDomainModel())
        Assert.assertTrue(result is UIState.Failure)
        val failureState = result as UIState.Failure
        Assert.assertTrue(failureState.throwable is IllegalArgumentException)
        Assert.assertEquals("101", failureState.throwable.message)
    }

    @Test
    fun `test currency conversion`() {
        val inputAmount = 100.0
        val currentRate = 1.0
        val currencyArr = Mocks.currencyArr
        val expectedConvertedAmounts = Mocks.expectedConvertedAmounts
        val result = fetchCurrencyConvertUseCaseImpl.convertCurrency(inputAmount, currentRate, currencyArr)
        Assert.assertEquals(expectedConvertedAmounts, result)
    }
}