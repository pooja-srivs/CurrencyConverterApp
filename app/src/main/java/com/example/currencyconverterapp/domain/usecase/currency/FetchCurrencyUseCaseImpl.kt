package com.example.currencyconverterapp.domain.usecase.currency

import com.example.currencyconverterapp.data.repository.CurrencyRepository
import com.example.currencyconverterapp.domain.mapper.toUiModel
import com.example.currencyconverterapp.domain.model.DomainResult
import com.example.currencyconverterapp.ui.model.CurrencyRateUiModel
import com.example.currencyconverterapp.ui.model.UIState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchCurrencyUseCaseImpl @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): FetchCurrencyUseCase {

    override suspend fun getCurrencies(): Flow<UIState<List<CurrencyRateUiModel>>> =
        flow {
            try {
                currencyRepository.getCurrencyData().collect { response ->
                    when(response) {
                        is DomainResult.Success -> {
                            emit(UIState.Success(response.value.toUiModel()))
                        }
                        is DomainResult.Failure -> {
                            emit(UIState.Failure(response.throwable))
                        }
                    }

                }
            } catch (throwable: Throwable) {
                emit(UIState.Failure(throwable))
            }
        }.flowOn(dispatcher)
}