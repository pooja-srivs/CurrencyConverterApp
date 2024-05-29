package com.example.currencyconverterapp.domain.usecase.convert

import com.example.currencyconverterapp.domain.model.ConvertedCurrencyItem
import com.example.currencyconverterapp.ui.adapter.CurrencyItem
import com.example.currencyconverterapp.ui.model.UIState

interface FetchCurrencyConvertUseCase {

    fun convert(
        inputAmount: Double,
        currentRate: Double,
        currencyArr: List<ConvertedCurrencyItem>,
    ): UIState<List<CurrencyItem>>
}