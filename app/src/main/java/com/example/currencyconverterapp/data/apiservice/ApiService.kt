package com.example.currencyconverterapp.data.apiservice

import com.example.currencyconverterapp.BuildConfig
import com.example.currencyconverterapp.data.model.CurrencyRatesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/api/latest.json")
    suspend fun fetchCurrencyRates(
        @Query("app_id") appId: String = BuildConfig.APP_ID,
        @Query("base") base: String = "USD"
    ) : CurrencyRatesResponseDto

}