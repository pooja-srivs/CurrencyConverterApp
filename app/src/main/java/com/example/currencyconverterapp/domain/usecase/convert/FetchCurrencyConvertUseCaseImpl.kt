package com.example.currencyconverterapp.domain.usecase.convert

import com.example.currencyconverterapp.domain.mapper.toAdapterUIModel
import com.example.currencyconverterapp.domain.model.ConvertedCurrencyItem
import com.example.currencyconverterapp.ui.adapter.CurrencyItem
import com.example.currencyconverterapp.ui.model.UIState
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject

class FetchCurrencyConvertUseCaseImpl @Inject constructor(): FetchCurrencyConvertUseCase{

    companion object {
        private const val AMOUNT_GREATER_THAN_ZERO = "101"
    }

    override fun convert(
        inputAmount: Double,
        currentRate: Double,
        currencyArr: List<ConvertedCurrencyItem>
    ): UIState<List<CurrencyItem>> {
        return try {
            validateInput(inputAmount)
            val conversionArr = convertCurrency(inputAmount, currentRate, currencyArr)
            UIState.Success(conversionArr.toAdapterUIModel())
        } catch (e: Exception) {
            UIState.Failure(e)
        }
    }

    @VisibleForTesting
    internal fun validateInput(inputAmount: Double) {
        require(inputAmount > 0) { AMOUNT_GREATER_THAN_ZERO }
    }

    @VisibleForTesting
    internal fun convertCurrency(
        inputAmount: Double,
        currentRate: Double,
        currencyArr: List<ConvertedCurrencyItem>
    ): List<ConvertedCurrencyItem> {
        return currencyArr.map { toCurrency ->
            val amountInBaseCurrency = inputAmount / currentRate
            val convertedAmount = amountInBaseCurrency * toCurrency.rate
                ConvertedCurrencyItem(
                    id = toCurrency.id,
                    currency = toCurrency.currency,
                    convertedAmount = convertedAmount,
                    rate = toCurrency.rate
                )
        }
    }
}