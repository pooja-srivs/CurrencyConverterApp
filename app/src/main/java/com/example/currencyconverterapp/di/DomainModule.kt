package com.example.currencyconverterapp.di

import com.example.currencyconverterapp.domain.usecase.currency.FetchCurrencyUseCase
import com.example.currencyconverterapp.domain.usecase.currency.FetchCurrencyUseCaseImpl
import com.example.currencyconverterapp.domain.usecase.convert.FetchCurrencyConvertUseCase
import com.example.currencyconverterapp.domain.usecase.convert.FetchCurrencyConvertUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindsCurrencyUseCase(fetchCurrencyUseCaseImpl: FetchCurrencyUseCaseImpl): FetchCurrencyUseCase

    @Binds
    @Singleton
    abstract fun bindsCurrencyConvertUseCase(fetchCurrencyConvertUseCaseImpl: FetchCurrencyConvertUseCaseImpl): FetchCurrencyConvertUseCase
}