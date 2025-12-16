package com.bolashak.wildberries.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bolashak.wildberries.domain.model.Product
import com.bolashak.wildberries.domain.usecase.GetProductsUseCase
import com.bolashak.wildberries.domain.usecase.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = ""
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            getProductsUseCase().collectLatest { products ->
                _state.value = _state.value.copy(
                    products = products.shuffled().take(20),
                    isLoading = false
                )
            }
        }
    }
    
    fun onSearchQueryChange(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
        
        viewModelScope.launch {
            if (query.isBlank()) {
                loadProducts()
            } else {
                searchProductsUseCase(query).collectLatest { results ->
                    _state.value = _state.value.copy(products = results, isLoading = false)
                }
            }
        }
    }
}
