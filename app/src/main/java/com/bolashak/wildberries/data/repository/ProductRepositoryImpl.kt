package com.bolashak.wildberries.data.repository

import com.bolashak.wildberries.data.model.toDomain
import com.bolashak.wildberries.data.source.CsvDataSource
import com.bolashak.wildberries.domain.model.Category
import com.bolashak.wildberries.domain.model.CategoryType
import com.bolashak.wildberries.domain.model.Product
import com.bolashak.wildberries.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val dataSource: CsvDataSource
) : ProductRepository {

    private var cachedProducts: List<Product>? = null

    private fun getOrLoadProducts(): List<Product> {
        if (cachedProducts == null) {
            cachedProducts = dataSource.getAllProducts().map { it.toDomain() }
        }
        return cachedProducts!!
    }

    override fun getProducts(): Flow<List<Product>> = flow {
        emit(getOrLoadProducts())
    }.flowOn(Dispatchers.IO)

    override fun getProduct(id: String): Flow<Product?> = flow {
        val product = getOrLoadProducts().find { it.id == id }
        emit(product)
    }.flowOn(Dispatchers.IO)

    override fun searchProducts(query: String): Flow<List<Product>> = flow {
        val all = getOrLoadProducts()
        if (query.isBlank()) {
            emit(all)
        } else {
            val filtered = all.filter {
                it.name.contains(query, ignoreCase = true) || 
                it.brand.contains(query, ignoreCase = true)
            }
            emit(filtered)
        }
    }.flowOn(Dispatchers.IO)

    override fun getProductsByCategory(category: String): Flow<List<Product>> = flow {
        val all = getOrLoadProducts()
        
        val filtered = all.filter { it.category.equals(category, ignoreCase = true) }
        emit(filtered)
    }.flowOn(Dispatchers.IO)

    override fun getAllCategories(): Flow<List<Category>> = flow {
        val products = getOrLoadProducts()
        val counts = products.groupingBy { it.category }.eachCount()
        
        val categories = CategoryType.values().map { type ->
            val count = counts[type.id] ?: 0
            Category(type, count)
        }
        emit(categories)
    }.flowOn(Dispatchers.IO)
}
