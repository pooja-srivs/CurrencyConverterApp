package com.example.currencyconverterapp.data.mapper

import com.example.currencyconverterapp.data.db.entities.CurrencyRateEntity
import com.example.currencyconverterapp.data.model.CurrencyRatesResponseDto
import com.example.currencyconverterapp.domain.model.CurrencyRate

fun CurrencyRatesResponseDto.toDomain(): List<CurrencyRate> {
    val currencyRates = this.rates ?: emptyMap()
    return currencyRates.map { rate ->
        CurrencyRate(
            currency = rate.key,
            rate = rate.value
        )
    }
}

fun CurrencyRatesResponseDto.toEntity(): List<CurrencyRateEntity> {
    val currencyRates = this.rates ?: emptyMap()
    return  currencyRates.map { rate ->
        CurrencyRateEntity(
            currency = rate.key,
            rate = rate.value
        )
    }
}

fun List<CurrencyRateEntity>.toDomain(): List<CurrencyRate> =
    this.map { currency ->
        CurrencyRate(
            currency = currency.currency,
            rate = currency.rate
        )
    }
