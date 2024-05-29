package com.example.currencyconverterapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.currencyconverterapp.domain.usecase.currency.FetchCurrencyUseCase
import com.example.currencyconverterapp.domain.usecase.convert.FetchCurrencyConvertUseCase
import javax.inject.Inject

class CurrencyViewModelFactory @Inject constructor(
    private val fetchCurrencyUseCase: FetchCurrencyUseCase,
    private val fetchCurrencyConvertUseCase: FetchCurrencyConvertUseCase
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrencyViewModel(fetchCurrencyUseCase, fetchCurrencyConvertUseCase) as T
    }
}
