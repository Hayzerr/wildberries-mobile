package com.bolashak.wildberries.domain.manager

import com.bolashak.wildberries.domain.model.CartItem
import com.bolashak.wildberries.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartManager @Inject constructor() {
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()
    
    val totalCount = _items.map { list -> list.sumOf { it.count } }
    val totalPrice = _items.map { list -> list.sumOf { it.count * it.product.price } }

    fun addToCart(product: Product) {
        val current = _items.value.toMutableList()
        val existing = current.find { it.product.id == product.id }
        if (existing != null) {
            val index = current.indexOf(existing)
            current[index] = existing.copy(count = existing.count + 1)
        } else {
            current.add(CartItem(product, 1))
        }
        _items.value = current
    }

    fun decreaseCount(product: Product) {
        val current = _items.value.toMutableList()
        val existing = current.find { it.product.id == product.id } ?: return
        
        if (existing.count > 1) {
            val index = current.indexOf(existing)
            current[index] = existing.copy(count = existing.count - 1)
        } else {
            current.remove(existing)
        }
        _items.value = current
    }

    fun removeProduct(product: Product) {
        val current = _items.value.toMutableList()
        current.removeAll { it.product.id == product.id }
        _items.value = current
    }
    
    fun clear() {
        _items.value = emptyList()
    }
}
