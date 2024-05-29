package com.example.currencyconverterapp.di

import android.content.Context
import androidx.room.Room
import com.example.currencyconverterapp.data.db.AppDatabase
import com.example.currencyconverterapp.data.db.dao.CurrencyRateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context = context, AppDatabase::class.java,
            "currency_converter_db"
        ).build()
    }

    @Provides
    fun provideCurrencyRateDao(database: AppDatabase): CurrencyRateDao = database.currencyRateDao()
}