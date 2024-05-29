package com.example.currencyconverterapp.data.source.remote

import com.example.currencyconverterapp.data.model.CurrencyRatesResponseDto

interface RemoteDataSource {

    suspend fun fetchCurrencies() : CurrencyRatesResponseDto
}