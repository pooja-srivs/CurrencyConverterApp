package com.example.currencyconverterapp.domain.model

sealed class DomainResult<out T> {
    data class Success<out T>(val value: T): DomainResult<T>()
    data class Failure(val throwable: Throwable): DomainResult<Nothing>()
}
