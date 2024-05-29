package com.example.currencyconverterapp.di

import com.example.currencyconverterapp.data.repository.CurrencyRepository
import com.example.currencyconverterapp.data.repository.CurrencyRepositoryImpl
import com.example.currencyconverterapp.data.source.local.LocalDataSource
import com.example.currencyconverterapp.data.source.local.LocalDataSourceImpl
import com.example.currencyconverterapp.data.source.remote.RemoteDataSource
import com.example.currencyconverterapp.data.source.remote.RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindsRemoteDataSource(dataSource: RemoteDataSourceImpl): RemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsLocalDataSource(localDataSource: LocalDataSourceImpl): LocalDataSource

    @Binds
    @Singleton
    abstract fun bindsRepository(currencyRepositoryImpl: CurrencyRepositoryImpl): CurrencyRepository
}