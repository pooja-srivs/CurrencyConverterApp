package com.example.currencyconverterapp.data.source.local

import com.example.currencyconverterapp.common.PreferenceHelper
import com.example.currencyconverterapp.data.db.dao.CurrencyRateDao
import com.example.currencyconverterapp.data.db.entities.CurrencyRateEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val preferenceHelper: PreferenceHelper,
    private val currencyRateDao: CurrencyRateDao
): LocalDataSource {

    override fun getLastRefreshTime(): Long =
        preferenceHelper.getLastRefreshTime()

    override fun saveLastRefreshTime(lastRefreshTime: Long) =
        preferenceHelper.saveLastRefreshTime(lastRefreshTime)

    override suspend fun insertAll(entities: List<CurrencyRateEntity>) {
        currencyRateDao.clear()
        currencyRateDao.insertCurrencyRates(entities)
    }

    override fun getCurrencyRates(): Flow<List<CurrencyRateEntity>> =
        currencyRateDao.getAllCurrencyRates()
}