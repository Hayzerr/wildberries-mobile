package com.bolashak.wildberries.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApi {
    @GET("https://open.er-api.com/v6/latest/{base}")
    suspend fun getExchangeRates(
        @Path("base") baseCurrency: String = "RUB"
    ): ExchangeRateResponse
}

@JsonClass(generateAdapter = true)
data class ExchangeRateResponse(
    @Json(name = "result") val result: String,
    @Json(name = "base_code") val baseCode: String,
    @Json(name = "rates") val rates: Map<String, Double>
)
