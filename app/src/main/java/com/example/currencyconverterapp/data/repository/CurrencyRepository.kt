package com.example.currencyconverterapp.data.repository

import com.example.currencyconverterapp.domain.model.CurrencyRate
import com.example.currencyconverterapp.domain.model.DomainResult
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {

    suspend fun getCurrencyData(): Flow<DomainResult<List<CurrencyRate>>>
}