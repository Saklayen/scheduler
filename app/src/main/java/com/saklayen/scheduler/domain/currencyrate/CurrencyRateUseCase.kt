package com.saklayen.scheduler.domain.currencyrate

import com.saklayen.scheduler.di.IoDispatcher
import com.saklayen.scheduler.domain.FlowUseCase
import com.saklayen.scheduler.domain.Result
import com.saklayen.scheduler.model.CurrencyRate
import com.saklayen.scheduler.ui.home.CurrencyRateRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRateUseCase @Inject constructor(
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val currencyRaterepository: CurrencyRateRepository
) : FlowUseCase<String, CurrencyRate>(ioDispatcher) {
    override suspend fun execute(parameters: String): Flow<Result<CurrencyRate>> =  currencyRaterepository.fetchCurrencyRate()
}