package com.bolashak.wildberries.domain.usecase

import com.bolashak.wildberries.domain.model.Product
import com.bolashak.wildberries.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(query: String): Flow<List<Product>> {
        return repository.searchProducts(query)
    }
}
