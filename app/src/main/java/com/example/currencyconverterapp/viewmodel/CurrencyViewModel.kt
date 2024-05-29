package com.example.currencyconverterapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverterapp.domain.usecase.currency.FetchCurrencyUseCase
import com.example.currencyconverterapp.domain.usecase.convert.FetchCurrencyConvertUseCase
import com.example.currencyconverterapp.ui.adapter.CurrencyItem
import com.example.currencyconverterapp.ui.mapper.toDomainModel
import com.example.currencyconverterapp.ui.model.CurrencyRateUiModel
import com.example.currencyconverterapp.ui.model.UIState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

class CurrencyViewModel @Inject constructor(
    private val fetchCurrencyUseCase: FetchCurrencyUseCase,
    private val fetchCurrencyConvertUseCase: FetchCurrencyConvertUseCase
): ViewModel() {

    private var inputAmount = 0.0
    private var selectedToCurrencyPosition = 0
    private lateinit var selectedToCurrencyUiModel: CurrencyRateUiModel
    private val currencyMutableLive = MutableLiveData<UIState<List<CurrencyRateUiModel>>>()
    val currencyLiveData: LiveData<UIState<List<CurrencyRateUiModel>>> by lazy { currencyMutableLive }
    private val convertedAmountMutableLiveData = MutableLiveData<UIState<List<CurrencyItem>>>()
    val convertedAmountLiveData: LiveData<UIState<List<CurrencyItem>>> by lazy { convertedAmountMutableLiveData }
    private var currencyListArr = mutableListOf<CurrencyItem>()

    fun fetchCurrencies() {
        viewModelScope.launch {
            try {
               fetchCurrencyUseCase.getCurrencies()
                   .catch { exception -> currencyMutableLive.value = UIState.Failure(exception) }
                   .collect { response ->
                       when(response) {
                           is UIState.Success -> {
                               currencyMutableLive.value = response
                           }
                           is UIState.Failure -> {
                               currencyMutableLive.value = response
                           }
                       }
                   }
            } catch (e: Exception) {
                currencyMutableLive.value = UIState.Failure(e)
            }
        }
    }

    fun isValidAmount(input: String): Boolean {
        return try {
            input.isNotEmpty() && input != "." && input.toDoubleOrNull() != null
        } catch (e: Exception) {
            false
        }
    }

    fun setInputAmount(input: String) {
        inputAmount = input.toDouble()
    }

    fun getInputAmount(): Double {
        return inputAmount
    }

    fun selectedCurrency(position: Int, selectedItem: CurrencyRateUiModel) {
        selectedToCurrencyPosition = position
        selectedToCurrencyUiModel = selectedItem
    }

    fun getSelectedCurrencyPosition(): Int {
        return selectedToCurrencyPosition
    }

    fun getSelectedCurrencyUiModel(): CurrencyRateUiModel {
        return selectedToCurrencyUiModel
    }

    fun setCurrencyList(currencyList: List<CurrencyItem>) {
        currencyList.map {
            currencyListArr.add(it)
        }
    }

    fun getCurrencyList(): List<CurrencyItem> = currencyListArr

    fun convert(
        inputAmount: Double,
        currentRate: Double,
        currencyList: List<CurrencyItem>
    ) {
        val convertedAmount = fetchCurrencyConvertUseCase.convert(
            inputAmount, currentRate, currencyList.toDomainModel()
        )
        convertedAmountMutableLiveData.value = convertedAmount
    }
}