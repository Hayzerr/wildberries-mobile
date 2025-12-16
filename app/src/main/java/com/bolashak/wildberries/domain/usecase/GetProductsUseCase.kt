package com.bolashak.wildberries.domain.usecase

import com.bolashak.wildberries.domain.model.Product
import com.bolashak.wildberries.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(category: String? = null): Flow<List<Product>> {
        return if (category != null) {
            repository.getProductsByCategory(category)
        } else {
            repository.getProducts()
        }
    }
}
