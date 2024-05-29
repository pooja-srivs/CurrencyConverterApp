package com.example.currencyconverterapp.data.source.remote

import com.example.currencyconverterapp.Mocks
import com.example.currencyconverterapp.data.apiservice.ApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RemoteDataSourceImplTest {

    private lateinit var apiService: ApiService
    private lateinit var remoteDataSourceImpl: RemoteDataSourceImpl

    @Before
    fun setUp() {
        apiService = mockk()
        remoteDataSourceImpl = RemoteDataSourceImpl(apiService)
    }

    @Test
    fun `test fetchCurrencies returns expected data`() = runTest {
        val expected = Mocks.currencyRatesResponseDto
        coEvery { apiService.fetchCurrencyRates() } returns expected

        val result = remoteDataSourceImpl.fetchCurrencies()

        Assert.assertEquals(expected, result)
        coVerify(exactly = 1) { apiService.fetchCurrencyRates() }
    }

    @Test(expected = Exception::class)
    fun `test fetchCurrencies returns Exception when ApiService fails`() = runTest{
        val exception = Exception("API call failed")
        coEvery { apiService.fetchCurrencyRates() } throws exception

        remoteDataSourceImpl.fetchCurrencies()
    }
}