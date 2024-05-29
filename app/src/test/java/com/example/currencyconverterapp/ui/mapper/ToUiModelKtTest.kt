package com.example.currencyconverterapp.ui.mapper

import com.example.currencyconverterapp.Mocks
import com.example.currencyconverterapp.domain.model.ConvertedCurrencyItem
import com.example.currencyconverterapp.ui.adapter.CurrencyItem
import org.junit.Assert
import org.junit.Test

class ToUiModelKtTest {

    @Test
    fun testCurrencyItemToDomain() {
        val currencyItemList = Mocks.currencyItemList
        val expectedDomainList = Mocks.convertedCurrencyItem

        val domainList = currencyItemList.toDomainModel()
        Assert.assertEquals(expectedDomainList.size, domainList.size)
        Assert.assertEquals(expectedDomainList, domainList)
    }

    @Test
    fun testCurrencyItemToDomainWithEmptyList() {
        val currencyItemList = emptyList<CurrencyItem>()
        val expectedDomainList = emptyList<ConvertedCurrencyItem>()

        val domainList = currencyItemList.toDomainModel()

        Assert.assertEquals(0, domainList.size)
        Assert.assertEquals(expectedDomainList, domainList)
    }
}