package com.skyyaros.android.testprojectforwork.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
data class ProductsResponse (
    @Json(name = "items") val listProducts: List<Product>
)

@Parcelize
@JsonClass(generateAdapter = true)
data class Product(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "subtitle") val subtitle: String,
    @Json(name = "price") val price: Price,
    @Json(name = "feedback") val feedback: Feedback?,
    @Json(name = "tags") val tags: List<String>,
    @Json(name = "available") val available: Int,
    @Json(name = "description") val description: String,
    @Json(name = "info") val listDopInfo: List<DopInfo>,
    @Json(name = "ingredients") val ingredients: String
):Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Price(
    @Json(name = "price") val price: Int,
    @Json(name = "discount") val discount: Int,
    @Json(name = "priceWithDiscount") val priceWithDiscount: Int,
    @Json(name = "unit") val unit: String
):Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Feedback(
    @Json(name = "count") val count: Int,
    @Json(name = "rating") val rating: Double
): Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class DopInfo(
    @Json(name = "title") val title: String,
    @Json(name = "value") val value: String
): Parcelable