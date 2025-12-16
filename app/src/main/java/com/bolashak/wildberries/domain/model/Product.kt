package com.bolashak.wildberries.domain.model

data class Product(
    val id: String,
    val name: String,
    val brand: String,
    val price: Int,
    val oldPrice: Int,
    val rating: Float,
    val feedbacks: Int,
    val url: String,
    val imageUrl: String,
    val category: String,
    val description: String = "",
    val reviewsText: String = ""
) {
    val discountPercent: Int
        get() = if (oldPrice > 0) ((oldPrice - price).toFloat() / oldPrice * 100).toInt() else 0
}
