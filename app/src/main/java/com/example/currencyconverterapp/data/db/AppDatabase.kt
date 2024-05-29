package com.example.currencyconverterapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.currencyconverterapp.data.db.dao.CurrencyRateDao
import com.example.currencyconverterapp.data.db.entities.CurrencyRateEntity

@Database(entities = [CurrencyRateEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun currencyRateDao(): CurrencyRateDao
}