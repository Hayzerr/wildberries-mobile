package com.bolashak.wildberries.domain.manager

import com.bolashak.wildberries.domain.model.CartItem
import com.bolashak.wildberries.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

data class PurchasedItem(
    val product: Product,
    val quantity: Int,
    val purchaseDate: String,
    val orderId: String
)

@Singleton
class PurchasesManager @Inject constructor() {
    private val _purchases = MutableStateFlow<List<PurchasedItem>>(emptyList())
    val purchases: StateFlow<List<PurchasedItem>> = _purchases.asStateFlow()
    
    private var orderCounter = 1000
    
    fun addPurchases(cartItems: List<CartItem>) {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        val orderId = "WB${++orderCounter}"
        
        val newPurchases = cartItems.map { cartItem ->
            PurchasedItem(
                product = cartItem.product,
                quantity = cartItem.count,
                purchaseDate = currentDate,
                orderId = orderId
            )
        }
        
        _purchases.value = newPurchases + _purchases.value
    }
    
    fun getPurchaseCount(): Int = _purchases.value.size
}
