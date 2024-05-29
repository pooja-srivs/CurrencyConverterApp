package com.example.currencyconverterapp.domain.mapper

import com.example.currencyconverterapp.domain.model.ConvertedCurrencyItem
import com.example.currencyconverterapp.domain.model.CurrencyRate
import com.example.currencyconverterapp.ui.adapter.CurrencyItem
import com.example.currencyconverterapp.ui.model.CurrencyRateUiModel

fun List<CurrencyRate>.toUiModel(): List<CurrencyRateUiModel> =
    this.map { currency ->
        CurrencyRateUiModel(
            currency = currency.currency,
            rate = currency.rate
        )
    }


fun List<ConvertedCurrencyItem>.toAdapterUIModel(): List<CurrencyItem> =
    this.map { currency ->
        CurrencyItem(
            id = currency.id,
            currency = currency.currency,
            rate = currency.rate,
            convertedAmount = currency.convertedAmount
        )
    }

