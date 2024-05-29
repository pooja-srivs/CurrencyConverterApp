package com.example.currencyconverterapp.ui.model

sealed class UIError : Exception() {
    object InvalidAmount : UIError()
    object InvalidCurrencyRate : UIError()
    object InvalidConversion : UIError()
}