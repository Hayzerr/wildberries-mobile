package com.bolashak.wildberries.presentation.currency

import androidx.lifecycle.ViewModel
import com.bolashak.wildberries.domain.manager.Currency
import com.bolashak.wildberries.domain.manager.CurrencyManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val currencyManager: CurrencyManager
) : ViewModel() {

    val selectedCurrency: StateFlow<Currency> = currencyManager.selectedCurrency
    val exchangeRates: StateFlow<Map<String, Double>> = currencyManager.exchangeRates
    val isLoading: StateFlow<Boolean> = currencyManager.isLoading

    fun selectCurrency(currency: Currency) {
        currencyManager.selectCurrency(currency)
    }

    fun refreshRates() {
        currencyManager.loadExchangeRates()
    }

    fun getAvailableCurrencies(): List<Currency> = currencyManager.getAvailableCurrencies()
}
