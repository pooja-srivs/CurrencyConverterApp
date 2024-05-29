package com.example.currencyconverterapp.data.model

import com.squareup.moshi.Json

data class CurrencyRatesResponseDto(
    @field:Json(name = "rates") val rates: Map<String, Double>?,
    @field:Json(name = "base") val base: String?,
    @field:Json(name = "timestamp") val timestamp: Long?
)
