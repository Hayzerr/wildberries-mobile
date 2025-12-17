package com.bolashak.wildberries.data.repository

import com.bolashak.wildberries.data.remote.CurrencyApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
    private val currencyApi: CurrencyApi
) {
    suspend fun getExchangeRates(): Map<String, Double> = withContext(Dispatchers.IO) {
        try {
            val response = currencyApi.getExchangeRates("RUB")
            if (response.result == "success") {
                response.rates
            } else {
                emptyMap()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyMap()
        }
    }
}
