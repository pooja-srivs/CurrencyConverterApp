package com.example.currencyconverterapp.domain.mapper

import com.example.currencyconverterapp.Mocks
import com.example.currencyconverterapp.domain.model.CurrencyRate
import org.junit.Assert
import org.junit.Test

class ToUIResultKtTest {

    @Test
    fun `test CurrencyRate ToUiModel with empty list`() {
        val currencyRate = emptyList<CurrencyRate>()
        val currencyRateUIModel = emptyList<CurrencyRate>()
        val uiList = currencyRate.toUiModel()

        Assert.assertEquals(0, uiList.size)
        Assert.assertEquals(currencyRateUIModel, uiList)
    }

    @Test
    fun `test CurrencyRate ToUiModel with valida data`() {
        val currencyRate = Mocks.currencyRateList
        val currencyRateUIModel = Mocks.currencyRateUiModel
        val uiList = currencyRate.toUiModel()

        Assert.assertEquals(currencyRate.size, uiList.size)
        Assert.assertEquals(currencyRateUIModel, uiList)
    }
}