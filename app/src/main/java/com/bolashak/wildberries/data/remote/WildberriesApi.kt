package com.bolashak.wildberries.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Query

interface WildberriesApi {
    @GET("https://feedbacks.wb.ru/api/v1/feedbacks")
    suspend fun getReviews(
        @Query("imtId") imtId: Int,
        @Query("order") order: String = "dateDesc"
    ): WbReviewsResponse

    @GET("https://card.wb.ru/cards/v1/detail?appType=1&curr=rub&dest=-1257786&spp=30")
    suspend fun getProductDetails(
        @Query("nm") nmId: String
    ): WbCardResponse
}

@JsonClass(generateAdapter = true)
data class WbReviewsResponse(
    @Json(name = "feedbacks") val feedbacks: List<WbFeedbackDto>?
)

@JsonClass(generateAdapter = true)
data class WbFeedbackDto(
    @Json(name = "id") val id: String,
    @Json(name = "text") val text: String?,
    @Json(name = "wbUserDetails") val userDetails: WbUserDetails?,
    @Json(name = "productValuation") val rating: Int,
    @Json(name = "createdDate") val createdDate: String
)

@JsonClass(generateAdapter = true)
data class WbUserDetails(
    @Json(name = "name") val name: String?
)

@JsonClass(generateAdapter = true)
data class WbCardResponse(
    @Json(name = "data") val data: WbDataDto
)

@JsonClass(generateAdapter = true)
data class WbDataDto(
    @Json(name = "products") val products: List<WbProductDetailsDto>
)

@JsonClass(generateAdapter = true)
data class WbProductDetailsDto(
    @Json(name = "root") val root: Int?,
    @Json(name = "description") val description: String?
)
