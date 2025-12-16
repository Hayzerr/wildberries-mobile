package com.bolashak.wildberries.data.model

import com.bolashak.wildberries.domain.model.Product
import com.opencsv.bean.CsvBindByName

data class ProductDto(
    @CsvBindByName(column = "nm_id")
    val id: String = "",
    
    @CsvBindByName(column = "name")
    val name: String = "",
    
    @CsvBindByName(column = "brand")
    val brand: String = "",
    
    @CsvBindByName(column = "price")
    val price: String = "0",
    
    @CsvBindByName(column = "old_price")
    val oldPrice: String = "0",
    
    @CsvBindByName(column = "rating")
    val rating: String = "0.0",
    
    @CsvBindByName(column = "feedbacks")
    val feedbacks: String = "0",
    
    @CsvBindByName(column = "url")
    val url: String = "",

    @CsvBindByName(column = "image_url")
    val imageUrl: String = "",
    
    @CsvBindByName(column = "category")
    val category: String = "",
    
    @CsvBindByName(column = "description")
    val description: String = "",
    
    @CsvBindByName(column = "reviews")
    val reviews: String = ""
)

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        brand = brand,
        price = price.replace(" ", "").replace("\u00a0", "").toIntOrNull() ?: 0,
        oldPrice = oldPrice.replace(" ", "").replace("\u00a0", "").toIntOrNull() ?: 0,
        rating = rating.toFloatOrNull() ?: 0f,
        feedbacks = feedbacks.toIntOrNull() ?: 0,
        url = url,
        imageUrl = imageUrl,
        category = category,
        description = description,
        reviewsText = reviews
    )
}
