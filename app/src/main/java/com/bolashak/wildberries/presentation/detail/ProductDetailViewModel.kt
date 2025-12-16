package com.bolashak.wildberries.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bolashak.wildberries.domain.model.Product
import com.bolashak.wildberries.domain.usecase.GetProductDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.bolashak.wildberries.data.repository.ProductDetailsRepository
import com.bolashak.wildberries.domain.manager.CartManager
import com.bolashak.wildberries.domain.manager.CurrencyManager
import com.bolashak.wildberries.domain.manager.FavoritesManager
import com.bolashak.wildberries.domain.model.Review
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val productDetailsRepository: ProductDetailsRepository,
    private val cartManager: CartManager,
    private val favoritesManager: FavoritesManager,
    val currencyManager: CurrencyManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: String = checkNotNull(savedStateHandle["productId"])

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product.asStateFlow()

    private val _description = MutableStateFlow<String>("Loading details...")
    val description: StateFlow<String> = _description.asStateFlow()

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    val isFavorite: StateFlow<Boolean> = favoritesManager.favorites
        .map { products -> products.any { it.id == productId } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        loadProduct()
    }

    private fun loadProduct() {
        viewModelScope.launch {
            getProductDetailsUseCase(productId).collect { product ->
                _product.value = product
                if (product != null) {
                    if (product.description.isNotBlank()) {
                        _description.value = product.description
                    } else {
                        viewModelScope.launch {
                            _description.value = productDetailsRepository.getDescription(product.id)
                        }
                    }
                    
                    if (product.reviewsText.isNotBlank()) {
                        _reviews.value = parseReviewsFromCsv(product.reviewsText)
                    } else {
                        viewModelScope.launch {
                            _reviews.value = productDetailsRepository.getReviews(product.id)
                        }
                    }
                }
            }
        }
    }
    
    private fun parseReviewsFromCsv(reviewsText: String): List<Review> {
        return reviewsText.split("|").mapNotNull { part ->
            val trimmed = part.trim()
            if (trimmed.isNotBlank()) {
                val rating = trimmed.substringAfter("★").substringBefore(":").toIntOrNull() ?: 5
                val text = trimmed.substringAfter(":").trim()
                if (text.isNotBlank()) {
                    Review(
                        id = text.hashCode().toString(),
                        author = "Покупатель",
                        text = text,
                        rating = rating,
                        date = ""
                    )
                } else null
            } else null
        }
    }

    fun toggleFavorite() {
        _product.value?.let { favoritesManager.toggleFavorite(it) }
    }

    fun addToCart() {
         _product.value?.let { cartManager.addToCart(it) }
    }
}
