package com.saklayen.scheduler.ui.home

import com.saklayen.scheduler.utils.ControlledRunner
import com.saklayen.scheduler.api.ApiService
import com.saklayen.scheduler.di.IoDispatcher
import com.saklayen.scheduler.model.CurrencyRate
import com.saklayen.scheduler.network.NetworkResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import com.saklayen.scheduler.domain.Result
import timber.log.Timber

@Singleton
@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyRateRepository @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    val apiService: ApiService
) {
    private val controlledRunner = ControlledRunner<Flow<Result<CurrencyRate>>>()

    suspend fun fetchCurrencyRate(): Flow<Result<CurrencyRate>> {
        Timber.d("Calling API-->")
        return controlledRunner.cancelPreviousThenRun {
            object : NetworkResource<CurrencyRate>(dispatcher) {
                override suspend fun createCall() = apiService.getCurrencyRates()
            }.asFlow()
        }
    }
}