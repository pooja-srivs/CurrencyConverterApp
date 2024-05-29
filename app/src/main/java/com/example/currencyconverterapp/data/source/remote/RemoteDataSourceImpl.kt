package com.example.currencyconverterapp.data.source.remote

import com.example.currencyconverterapp.data.apiservice.ApiService
import com.example.currencyconverterapp.data.model.CurrencyRatesResponseDto
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : RemoteDataSource {

    override suspend fun fetchCurrencies(): CurrencyRatesResponseDto = apiService.fetchCurrencyRates()
}