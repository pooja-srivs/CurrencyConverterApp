package com.example.currencyconverterapp.data.repository

import app.cash.turbine.test
import com.example.currencyconverterapp.Mocks
import com.example.currencyconverterapp.data.mapper.toDomain
import com.example.currencyconverterapp.data.mapper.toEntity
import com.example.currencyconverterapp.data.source.local.LocalDataSource
import com.example.currencyconverterapp.data.source.remote.RemoteDataSource
import com.example.currencyconverterapp.domain.model.DomainResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CurrencyRepositoryImplTest {
    private lateinit var localDataSource: LocalDataSource
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var currencyRepositoryImpl: CurrencyRepositoryImpl

    @Before
    fun setUp() {
        localDataSource = mockk()
        remoteDataSource = mockk()
        currencyRepositoryImpl = CurrencyRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun `isDataStale returns true if lastRefreshTime is zero`() {
        every { localDataSource.getLastRefreshTime() } returns 0L

        val result = currencyRepositoryImpl.isDataStale()

        Assert.assertTrue(result)
    }

    @Test
    fun `isDataStale returns true when lastRefreshTime is older than REFRESH_INTERVAL_MS`() {
        val lastRefreshTime = System.currentTimeMillis() - (CurrencyRepositoryImpl.REFRESH_INTERVAL_MS + 1)
        every { localDataSource.getLastRefreshTime() } returns lastRefreshTime

        val result = currencyRepositoryImpl.isDataStale()

        Assert.assertTrue(result)
    }

    @Test
    fun `isDataStale returns false when lastRefreshTime is within REFRESH_INTERVAL_MS`() {
        val lastRefreshTime = System.currentTimeMillis() + CurrencyRepositoryImpl.REFRESH_INTERVAL_MS
        every { localDataSource.getLastRefreshTime() } returns lastRefreshTime

        val result = currencyRepositoryImpl.isDataStale()

        Assert.assertFalse(result)
    }

    @Test
    fun `test getCurrencyData when data is stale`() = runTest{
        val expected = Mocks.currencyRatesResponseDto

        val lastRefreshTime = 0L
        coEvery { remoteDataSource.fetchCurrencies() } coAnswers { expected }
        every { localDataSource.saveLastRefreshTime(any()) } just runs
        coEvery { localDataSource.insertAll(expected.toEntity()) } just runs
        every { localDataSource.getLastRefreshTime() } returns lastRefreshTime

        currencyRepositoryImpl.getCurrencyData().test {
            val emittedItem = awaitItem()
            Assert.assertTrue(emittedItem is DomainResult.Success)
            Assert.assertEquals(DomainResult.Success(expected.toDomain()), emittedItem)
            awaitComplete()
        }

        coVerify(exactly = 1) { remoteDataSource.fetchCurrencies() }
        coVerify(exactly = 1) { localDataSource.saveLastRefreshTime(any()) }
        coVerify(exactly = 1) { localDataSource.insertAll(expected.toEntity()) }
    }

    @Test
    fun `test getCurrencyData when data is fresh`() = runTest {
        val lastRefreshTime = System.currentTimeMillis()
        val expected = Mocks.currencyRateEntityList

        every { localDataSource.getLastRefreshTime() } returns lastRefreshTime
        coEvery { localDataSource.getCurrencyRates() } coAnswers {
            flow { emit(expected) }
        }

        currencyRepositoryImpl.getCurrencyData().test {
            val emittedItem = awaitItem()
            Assert.assertTrue(emittedItem is DomainResult.Success)
            Assert.assertEquals(DomainResult.Success(expected.toDomain()), emittedItem)
            awaitComplete()
        }

     coVerify(exactly = 1) { localDataSource.getLastRefreshTime() }
     coVerify(exactly = 1) { localDataSource.getCurrencyRates() }
    }

    @Test
    fun `test getCurrencyData emits Failure when api throws error`() = runTest{
        val expected = Throwable()
        val pastTime = System.currentTimeMillis() - (CurrencyRepositoryImpl.REFRESH_INTERVAL_MS + 1)
        coEvery { remoteDataSource.fetchCurrencies() } throws expected
        every { localDataSource.getLastRefreshTime() } returns pastTime

        currencyRepositoryImpl.getCurrencyData().test {
            val emittedItem = awaitItem()
            Assert.assertTrue(emittedItem is DomainResult.Failure)
            Assert.assertEquals(DomainResult.Failure(expected), emittedItem)
            awaitComplete()
        }

        coVerify(exactly = 1) { remoteDataSource.fetchCurrencies() }
        coVerify(exactly = 1) { localDataSource.getLastRefreshTime() }
    }

    @Test
    fun `test getCurrencyData emits Failure when database throws error`() = runTest{
        val expected = Throwable()
        val pastTime = System.currentTimeMillis() + CurrencyRepositoryImpl.REFRESH_INTERVAL_MS
        coEvery { localDataSource.getCurrencyRates() } throws expected
        every { localDataSource.getLastRefreshTime() } returns pastTime

        currencyRepositoryImpl.getCurrencyData().test {
            val emittedItem = awaitItem()
            Assert.assertTrue(emittedItem is DomainResult.Failure)
            Assert.assertEquals(DomainResult.Failure(expected), emittedItem)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { localDataSource.getCurrencyRates() }
        coVerify(exactly = 1) { localDataSource.getLastRefreshTime() }
    }
}