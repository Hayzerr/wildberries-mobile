package com.bolashak.wildberries.domain.model

data class Review(
    val id: String = "",
    val author: String = "Anonymous",
    val text: String = "",
    val rating: Int = 5,
    val date: String = ""
)
