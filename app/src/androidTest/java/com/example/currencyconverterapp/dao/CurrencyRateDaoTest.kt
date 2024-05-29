package com.example.currencyconverterapp.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.currencyconverterapp.Mocks
import com.example.currencyconverterapp.data.db.AppDatabase
import com.example.currencyconverterapp.data.db.dao.CurrencyRateDao
import com.example.currencyconverterapp.data.db.entities.CurrencyRateEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CurrencyRateDaoTest {

    private lateinit var dao: CurrencyRateDao
    private lateinit var database: AppDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.currencyRateDao()
    }

    @Test
    fun testInsertAndRetrieveCurrencyRateInEmptyDatabase() = runTest {
        dao.getAllCurrencyRates().test {
            val emittedResult = awaitItem()
            assertEquals(0, emittedResult.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testInsertAndRetrieveCurrencyRateEmptyList() = runTest {
        val input = emptyList<CurrencyRateEntity>()
        dao.insertCurrencyRates(input)

        dao.getAllCurrencyRates().test {
            val emittedResult = awaitItem()
            assertEquals(0, emittedResult.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testInsertAllCurrencyRate()= runTest {
        val input = Mocks.currencyRateEntityList
        dao.insertCurrencyRates(input)

        dao.getAllCurrencyRates().test {
            val result = awaitItem()
            assertEquals(input.size, result.size)
            assertEquals(input.first(), result.first())
            assertEquals(input, result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testDeletesAllCurrencyRates() = runTest {
        val currencyRates = listOf(
            CurrencyRateEntity("USD", 1.0),
            CurrencyRateEntity("EUR", 0.9)
        )
        dao.insertCurrencyRates(currencyRates)
        dao.clear()
        dao.getAllCurrencyRates().test {
            val emittedItem = awaitItem()
            assertEquals(0, emittedItem.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testClearOnEmptyDatabase() = runTest {
        dao.clear()
        val result = dao.getAllCurrencyRates().first()
        assertEquals(0, result.size)
    }

    @After
    fun tearDown() {
        database.close()
    }
}