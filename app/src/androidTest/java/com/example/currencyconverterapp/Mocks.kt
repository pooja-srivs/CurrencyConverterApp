package com.example.currencyconverterapp

import com.example.currencyconverterapp.data.db.entities.CurrencyRateEntity
import com.example.currencyconverterapp.domain.model.ConvertedCurrencyItem
import com.example.currencyconverterapp.ui.adapter.CurrencyItem

object Mocks {

    val currencyRateEntityList = listOf(
        CurrencyRateEntity(
            currency = "EUR",
            rate = 0.8
        ),
        CurrencyRateEntity(
            currency = "BPL",
            rate = 0.9
        )
    )

    val mockResponseBody = """
    {
      "disclaimer": "Usage subject to terms: https://openexchangerates.org/terms",
      "license": "https://openexchangerates.org/license",
      "timestamp": 1716379200,
      "base": "USD",
      "rates": {
        "AED": 3.673,
        "AFN": 71.901399,
        "ALL": 92.646448
      }
    }
""".trimIndent()

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

    val currencyArr = listOf(
        ConvertedCurrencyItem(1, "USD", 1.0, 0.0),
        ConvertedCurrencyItem(2, "EUR", 0.85, 0.0),
        ConvertedCurrencyItem(3, "JPY", 110.0, 0.0)
    )

    val expectedConvertedAmounts = listOf(
        ConvertedCurrencyItem(1, "USD", 1.0, 100.0),
        ConvertedCurrencyItem(2, "EUR", 0.85, 85.0),
        ConvertedCurrencyItem(3, "JPY", 110.0, 11000.0)
    )
}