package com.example.currencyconverterapp.ui.model

sealed class UIState<out T> {
    data class Success<out T>(val value: T): UIState<T>()
    data class Failure(val throwable: Throwable): UIState<Nothing>()
    object Loading: UIState<Nothing>()
}