package com.saklayen.scheduler.api

import com.saklayen.scheduler.model.token.RefreshToken
import com.saklayen.scheduler.model.token.RefreshTokenResponse
import com.saklayen.scheduler.model.CurrencyRate
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("latest")
    fun getCurrencyRates(): Flow<ApiResponse<CurrencyRate>>

    @POST("getAccessTokenByRefreshToken")
    fun getRefreshToken(@Body data: RefreshToken): Call<RefreshTokenResponse>

}
