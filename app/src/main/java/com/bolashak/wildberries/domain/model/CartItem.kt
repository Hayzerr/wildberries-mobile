package com.bolashak.wildberries.domain.model

data class CartItem(
    val product: Product,
    var count: Int = 1
)
