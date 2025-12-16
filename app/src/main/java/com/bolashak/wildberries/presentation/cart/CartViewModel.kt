package com.bolashak.wildberries.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bolashak.wildberries.domain.manager.CartManager
import com.bolashak.wildberries.domain.manager.CurrencyManager
import com.bolashak.wildberries.domain.manager.PurchasesManager
import com.bolashak.wildberries.domain.model.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartManager: CartManager,
    private val purchasesManager: PurchasesManager,
    val currencyManager: CurrencyManager
) : ViewModel() {
    val items = cartManager.items
    val totalPrice = cartManager.totalPrice
    val totalCount = cartManager.totalCount

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun removeProduct(cartItem: CartItem) {
        cartManager.removeProduct(cartItem.product)
    }

    fun checkout() {
        viewModelScope.launch {
            val currentItems = cartManager.items.value
            if (currentItems.isNotEmpty()) {
                purchasesManager.addPurchases(currentItems)
            }
            cartManager.clear()
            _uiEvent.emit(UiEvent.NavigateToPurchases)
        }
    }

    sealed class UiEvent {
        object NavigateToPurchases : UiEvent()
    }
}
