package com.example.currencyconverterapp.ui.mapper

import com.example.currencyconverterapp.domain.model.ConvertedCurrencyItem
import com.example.currencyconverterapp.ui.adapter.CurrencyItem

fun List<CurrencyItem>.toDomainModel(): List<ConvertedCurrencyItem> =
    this.map { currency ->
        ConvertedCurrencyItem(
            id = currency.id,
            currency = currency.currency,
            rate = currency.rate,
            convertedAmount = currency.convertedAmount
        )
    }