package com.bolashak.wildberries.domain.repository

import com.bolashak.wildberries.domain.model.Category
import com.bolashak.wildberries.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    fun getProduct(id: String): Flow<Product?>
    fun searchProducts(query: String): Flow<List<Product>>
    fun getProductsByCategory(category: String): Flow<List<Product>>
    fun getAllCategories(): Flow<List<Category>>
}
