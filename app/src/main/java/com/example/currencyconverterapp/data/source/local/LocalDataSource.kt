package com.example.currencyconverterapp.data.source.local

import com.example.currencyconverterapp.data.db.entities.CurrencyRateEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getLastRefreshTime(): Long

    fun saveLastRefreshTime(lastRefreshTime: Long)

    suspend fun insertAll(entities: List<CurrencyRateEntity>)

    fun getCurrencyRates(): Flow<List<CurrencyRateEntity>>
}