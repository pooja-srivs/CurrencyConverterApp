package com.example.currencyconverterapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyconverterapp.data.db.entities.CurrencyRateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyRateDao {

    @Query("SELECT * FROM currency_rates")
    fun getAllCurrencyRates(): Flow<List<CurrencyRateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyRates(currencyRateEntities: List<CurrencyRateEntity>)

    @Query("DELETE FROM currency_rates")
    suspend fun clear()
}