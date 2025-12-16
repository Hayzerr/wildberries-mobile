package com.bolashak.wildberries.domain.manager

import com.bolashak.wildberries.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesManager @Inject constructor() {
    private val _favorites = MutableStateFlow<Set<Product>>(emptySet())
    val favorites: StateFlow<Set<Product>> = _favorites.asStateFlow()

    fun toggleFavorite(product: Product) {
        val current = _favorites.value.toMutableSet()
        val existing = current.find { it.id == product.id }
        if (existing != null) {
            current.remove(existing)
        } else {
            current.add(product)
        }
        _favorites.value = current
    }

    fun isFavorite(productId: String): Boolean {
        return _favorites.value.any { it.id == productId }
    }
}
