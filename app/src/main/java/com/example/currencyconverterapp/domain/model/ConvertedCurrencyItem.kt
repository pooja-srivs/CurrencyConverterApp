package com.example.currencyconverterapp.domain.model

data class ConvertedCurrencyItem(var id: Int, val currency: String, val rate: Double, var convertedAmount: Double)