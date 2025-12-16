package com.bolashak.wildberries.domain.usecase

import com.bolashak.wildberries.domain.model.Category
import com.bolashak.wildberries.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<Category>> {
        return repository.getAllCategories()
    }
}
