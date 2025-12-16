package com.bolashak.wildberries.data.repository

import com.bolashak.wildberries.data.remote.WildberriesApi
import com.bolashak.wildberries.domain.model.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductDetailsRepository @Inject constructor(
    private val api: WildberriesApi
) {

    suspend fun getReviews(nmId: String): List<Review> = withContext(Dispatchers.IO) {
        val cleanId = nmId.substringBefore(".").trim()
        if (!isValidId(cleanId)) return@withContext emptyList()
        
        try {
            val detailsResponse = api.getProductDetails(nmId = cleanId)
            val imtId = detailsResponse.data.products.firstOrNull()?.root
            
            if (imtId == null) {
                return@withContext emptyList()
            }

            val response = api.getReviews(imtId = imtId)
            response.feedbacks?.mapNotNull { item ->
                if (!item.text.isNullOrBlank()) {
                     Review(
                         id = item.id,
                         author = item.userDetails?.name ?: "User",
                         text = item.text,
                         rating = item.rating,
                         date = item.createdDate.take(10)
                     )
                } else null
            }?.take(10) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getDescription(nmId: String): String = withContext(Dispatchers.IO) {
        val cleanId = nmId.substringBefore(".").trim()
        if (!isValidId(cleanId)) return@withContext "Invalid Product ID"

        try {
            val response = api.getProductDetails(nmId = cleanId)
            val desc = response.data.products.firstOrNull()?.description
            if (desc.isNullOrBlank()) "Description not provided by seller." else desc
        } catch (e: Exception) {
            e.printStackTrace()
            "Error loading description: ${e.message}"
        }
    }
    
    private fun isValidId(id: String): Boolean {
        return id.isNotBlank() && id.all { it.isDigit() }
    }
}
