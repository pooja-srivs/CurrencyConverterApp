package com.example.currencyconverterapp.domain.usecase.currency

import com.example.currencyconverterapp.ui.model.CurrencyRateUiModel
import com.example.currencyconverterapp.ui.model.UIState
import kotlinx.coroutines.flow.Flow

interface FetchCurrencyUseCase {

    suspend fun getCurrencies(): Flow<UIState<List<CurrencyRateUiModel>>>
}