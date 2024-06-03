package com.example.currencyconverterapp.data.repository

import com.example.currencyconverterapp.data.mapper.toDomain
import com.example.currencyconverterapp.data.mapper.toEntity
import com.example.currencyconverterapp.data.source.local.LocalDataSource
import com.example.currencyconverterapp.data.source.remote.RemoteDataSource
import com.example.currencyconverterapp.domain.model.CurrencyRate
import com.example.currencyconverterapp.domain.model.DomainResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): CurrencyRepository {

    companion object {
        const val REFRESH_INTERVAL_MS = 30 * 60 * 1000
    }

    override suspend fun getCurrencyData(): Flow<DomainResult<List<CurrencyRate>>> =
        flow {
            try {
                if (isDataStale()) {
                    val response = remoteDataSource.fetchCurrencies()
                    localDataSource.saveLastRefreshTime(System.currentTimeMillis())
                    localDataSource.insertAll(response.toEntity())
                    emit(DomainResult.Success(response.toDomain()))
                } else {
                    localDataSource.getCurrencyRates().collect{ cachedData ->
                        emit(DomainResult.Success(cachedData.toDomain()))
                    }
                }
            } catch (e: Throwable) {
                emit(DomainResult.Failure(e))
            }
        }.flowOn(dispatcher)

    @VisibleForTesting
    internal fun isDataStale(): Boolean {
        val lastRefreshTime= localDataSource.getLastRefreshTime() ?: return true
        if (lastRefreshTime == 0L) {
            return true
        }
        /**
         * System.currentTimeMillis() - why not to use?
        * Manual change: Users can change their time in the Setting page.
        * Automatic change: Current time can be changed because of different time zones.
        *  If a user run across the time zone, the current time will be changed.*/
        val result = System.currentTimeMillis() - lastRefreshTime > REFRESH_INTERVAL_MS
        return result
    }

}