package com.skyyaros.android.testprojectforwork.data

import android.util.Log
import com.skyyaros.android.testprojectforwork.entity.Product
import com.skyyaros.android.testprojectforwork.entity.ProductWithPhoto
import com.skyyaros.android.testprojectforwork.ui.ProductWithFav

class InternetRepository(private val internetApi: InternetApi) {
    val myImages = mapOf(
        "cbf0c984-7c6c-4ada-82da-e29dc698bb50" to listOf("img6.png", "img5.png"),
        "54a876a5-2205-48ba-9498-cfecff4baa6e" to listOf("img1.png", "img2.png"),
        "75c84407-52e1-4cce-a73a-ff2d3ac031b3" to listOf("img5.png", "img6.png"),
        "16f88865-ae74-4b7c-9d85-b68334bb97db" to listOf("img3.png", "img4.png"),
        "26f88856-ae74-4b7c-9d85-b68334bb97db" to listOf("img2.png", "img3.png"),
        "15f88865-ae74-4b7c-9d81-b78334bb97db" to listOf("img6.png", "img1.png"),
        "88f88865-ae74-4b7c-9d81-b78334bb97db" to listOf("img4.png", "img3.png"),
        "55f58865-ae74-4b7c-9d81-b78334bb97db" to listOf("img1.png", "img5.png")
    )
    suspend fun getListProducts(): List<ProductWithFav>? {
        return try {
            val res = internetApi.getListProducts()
            if (res.isSuccessful) {
                res.body()!!.listProducts.map {
                    ProductWithFav(
                        it.id, it.title, it.subtitle,
                        it.price, it.feedback, it.tags,
                        it.available, it.description, it.listDopInfo, it.ingredients,
                        myImages[it.id]!!, false
                    )
                }
            }
            else
                null
        } catch (t: Throwable) {
            null
        }
    }
}