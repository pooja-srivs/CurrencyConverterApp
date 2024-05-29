package com.example.currencyconverterapp

import com.example.currencyconverterapp.data.db.entities.CurrencyRateEntity
import com.example.currencyconverterapp.data.model.CurrencyRatesResponseDto
import com.example.currencyconverterapp.domain.model.ConvertedCurrencyItem
import com.example.currencyconverterapp.domain.model.CurrencyRate
import com.example.currencyconverterapp.ui.adapter.CurrencyItem
import com.example.currencyconverterapp.ui.model.CurrencyRateUiModel

object Mocks {

    val currencyRateEntityList = listOf(
        CurrencyRateEntity(
            currency = "AED",
            rate = 3.673
        ),
        CurrencyRateEntity(
            currency = "AFN",
            rate = 71.901399
        ),
        CurrencyRateEntity(
            currency = "ALL",
            rate = 92.646448
        )
    )

    val updatedCurrencyRateEntityList = listOf(
        CurrencyRateEntity(
            currency = "ABC",
            rate = 1.8
        ),
        CurrencyRateEntity(
            currency = "DEF",
            rate = 1.9
        )
    )

    val currencyRateList =  listOf(
        CurrencyRate(
            currency = "AED",
            rate = 3.673
        ),
        CurrencyRate(
            currency = "AFN",
            rate = 71.901399
        ),
        CurrencyRate(
            currency = "ALL",
            rate = 92.646448
        )
    )

    val convertedCurrencyItem =  listOf(
        ConvertedCurrencyItem(
            id = 1,
            currency = "ABC",
            rate = 3.673,
            convertedAmount = 0.0
        ),
        ConvertedCurrencyItem(
            id = 2,
            currency = "DEF",
            rate = 4.3,
            convertedAmount = 0.0
        )
    )

    val currencyRatesResponseDto = CurrencyRatesResponseDto(
        base = "USD",
        timestamp = 0L,
        rates = mapOf(
            "AED" to 3.673,
            "AFN" to 71.901399,
            "ALL" to 92.646448
        )
    )

    val currencyRateUiModel = listOf(
        CurrencyRateUiModel(
            currency = "AED",
            rate = 3.673
        ),
        CurrencyRateUiModel(
            currency = "AFN",
            rate = 71.901399
        ),
        CurrencyRateUiModel(
            currency = "ALL",
            rate = 92.646448
        )
    )

    val currencyItemList = listOf(
        CurrencyItem(
            id = 1,
            currency = "ABC",
            rate = 3.673,
            convertedAmount = 0.0
        ),
        CurrencyItem(
            id = 2,
            currency = "DEF",
            rate = 4.3,
            convertedAmount = 0.0
        )
    )
}