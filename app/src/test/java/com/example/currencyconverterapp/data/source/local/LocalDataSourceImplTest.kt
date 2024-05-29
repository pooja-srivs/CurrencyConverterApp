package com.example.currencyconverterapp.data.source.local

import app.cash.turbine.test
import com.example.currencyconverterapp.common.PreferenceHelper
import com.example.currencyconverterapp.Mocks
import com.example.currencyconverterapp.data.db.dao.CurrencyRateDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LocalDataSourceImplTest {

    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var currencyRateDao: CurrencyRateDao
    private lateinit var localDataSourceImpl: LocalDataSourceImpl

    @Before
    fun setUp() {
        preferenceHelper = mockk()
        currencyRateDao = mockk()
        localDataSourceImpl = LocalDataSourceImpl(preferenceHelper, currencyRateDao)
    }

    @Test
    fun `test getLastRefreshTime returns valid timestamp`() {
        val expectedTime = 3687698798
        every { preferenceHelper.getLastRefreshTime() } returns expectedTime
        val result = localDataSourceImpl.getLastRefreshTime()
        assertEquals(expectedTime, result)
        coVerify(exactly = 1) { preferenceHelper.getLastRefreshTime() }
    }

    @Test
    fun `test getLastRefreshTime returns negative timestamp`() {
        val expected = -1000L
        every { preferenceHelper.getLastRefreshTime() } returns expected
        val result = localDataSourceImpl.getLastRefreshTime()
        assertEquals(expected, result)
        coVerify(exactly = 1) { preferenceHelper.getLastRefreshTime() }
    }

    @Test
    fun `test saveLastRefreshTime saves valid timestamp`() {
        val lastRefreshTime = 3687698798
        every { preferenceHelper.saveLastRefreshTime(any()) } just runs
        localDataSourceImpl.saveLastRefreshTime(lastRefreshTime)

        coVerify(exactly = 1) { preferenceHelper.saveLastRefreshTime(lastRefreshTime) }
    }

    @Test
    fun `test insertAll inserts and fetches correct data`() = runTest {
        val input = Mocks.currencyRateEntityList
        coEvery { currencyRateDao.insertCurrencyRates(any()) } just runs
        coEvery { currencyRateDao.clear() } just runs

        localDataSourceImpl.insertAll(input)

        coVerify(exactly = 1) { currencyRateDao.clear() }
        coVerify(exactly = 1) { currencyRateDao.insertCurrencyRates(input) }
    }

    @Test
    fun `test getCurrencyRates fetches correct data`() = runTest {
        val expected = Mocks.currencyRateEntityList
        coEvery { currencyRateDao.getAllCurrencyRates() } returns flow {
            emit(expected)
        }
        localDataSourceImpl.getCurrencyRates().test {
            val emittedItem = awaitItem()
            assertEquals(expected.size, emittedItem.size)
            assertEquals(expected.first(), emittedItem.first())
            assertEquals(expected, emittedItem)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test getCurrencyRates throws Exception`() = runTest {
        val exception = RuntimeException("Database error")
        coEvery { currencyRateDao.getAllCurrencyRates() } returns  flow { throw exception }

        localDataSourceImpl.getCurrencyRates().test {
            val emittedError = awaitError()
            assertEquals(exception.message, emittedError.message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test insertAll inserts fresh data and clears the stale data and returns correct data`() = runTest {
        val input = Mocks.currencyRateEntityList
        val updatedInput = Mocks.updatedCurrencyRateEntityList
        coEvery { currencyRateDao.insertCurrencyRates(any()) } just runs
        coEvery { currencyRateDao.clear() } just runs
        coEvery { currencyRateDao.getAllCurrencyRates() } returns flow {
            emit(updatedInput)
        }

        localDataSourceImpl.insertAll(input)
        localDataSourceImpl.insertAll(updatedInput)

        localDataSourceImpl.getCurrencyRates().test {
            val item = awaitItem()
            assertEquals(updatedInput.first(), item.first())
            assertEquals(updatedInput.size, item.size)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 2) { currencyRateDao.clear() }
        coVerify(exactly = 1) { currencyRateDao.insertCurrencyRates(input) }
    }
}