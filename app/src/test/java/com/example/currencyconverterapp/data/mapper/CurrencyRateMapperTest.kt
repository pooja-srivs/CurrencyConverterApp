package com.example.currencyconverterapp.data.mapper

import com.example.currencyconverterapp.Mocks
import org.junit.Assert
import org.junit.Test


class CurrencyRateMapperTest {

    @Test
    fun testCurrencyRatesResponseDtoToDomain() {
        val dto = Mocks.currencyRatesResponseDto
        val expectedDomainList = Mocks.currencyRateList

        val domainList = dto.toDomain()
        Assert.assertEquals(expectedDomainList, domainList)
    }

    @Test
    fun testCurrencyRatesResponseDtoToEntity() {
        val dto = Mocks.currencyRatesResponseDto
        val expectedEntityList = Mocks.currencyRateEntityList

        val entityList = dto.toEntity()
        Assert.assertEquals(expectedEntityList, entityList)
    }

    @Test
    fun testCurrencyRateEntityToDomain() {
        val currencyRateEntity = Mocks.currencyRateEntityList
        val expectedDomainList = Mocks.currencyRateList

        val domainList = currencyRateEntity.toDomain()

        Assert.assertEquals(expectedDomainList, domainList)
    }
}