package com.bolashak.wildberries.presentation.product_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bolashak.wildberries.domain.model.Product
import com.bolashak.wildberries.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryId: String = checkNotNull(savedStateHandle["category"])

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _title = MutableStateFlow(categoryId)
    val title: StateFlow<String> = _title.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            getProductsUseCase(categoryId).collect {
                _products.value = it
            }
        }
    }
}
