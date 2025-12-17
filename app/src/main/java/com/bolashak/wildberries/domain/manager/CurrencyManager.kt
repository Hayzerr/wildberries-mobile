package com.bolashak.wildberries.domain.manager

import com.bolashak.wildberries.data.repository.CurrencyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

enum class Currency(val code: String, val symbol: String, val displayName: String) {
    RUB("RUB", "₽", "Russian Ruble"),
    USD("USD", "$", "US Dollar"),
    EUR("EUR", "€", "Euro"),
    KZT("KZT", "₸", "Kazakh Tenge"),
    GBP("GBP", "£", "British Pound")
}

@Singleton
class CurrencyManager @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    private val _selectedCurrency = MutableStateFlow(Currency.RUB)
    val selectedCurrency: StateFlow<Currency> = _selectedCurrency.asStateFlow()
    
    private val _exchangeRates = MutableStateFlow<Map<String, Double>>(emptyMap())
    val exchangeRates: StateFlow<Map<String, Double>> = _exchangeRates.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadExchangeRates()
    }
    
    fun loadExchangeRates() {
        scope.launch {
            _isLoading.value = true
            val rates = currencyRepository.getExchangeRates()
            _exchangeRates.value = rates
            _isLoading.value = false
        }
    }
    
    fun selectCurrency(currency: Currency) {
        _selectedCurrency.value = currency
    }
    
    fun convertPrice(priceInRub: Int): String {
        val currency = _selectedCurrency.value
        
        if (currency == Currency.RUB) {
            return "$priceInRub ${currency.symbol}"
        }
        
        val rate = _exchangeRates.value[currency.code]
        return if (rate != null && rate > 0) {
            val converted = priceInRub * rate
            when (currency) {
                Currency.USD, Currency.EUR, Currency.GBP -> String.format("%.2f %s", converted, currency.symbol)
                Currency.KZT -> String.format("%.0f %s", converted, currency.symbol)
                else -> "$priceInRub ${Currency.RUB.symbol}"
            }
        } else {
            "$priceInRub ${Currency.RUB.symbol}"
        }
    }
    
    fun getAvailableCurrencies(): List<Currency> = Currency.values().toList()
}
