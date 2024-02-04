package com.skyyaros.android.testprojectforwork.ui

import com.skyyaros.android.testprojectforwork.entity.DopInfo
import com.skyyaros.android.testprojectforwork.entity.Feedback
import com.skyyaros.android.testprojectforwork.entity.Price
import com.skyyaros.android.testprojectforwork.entity.Product
import com.skyyaros.android.testprojectforwork.entity.ProductWithPhoto

sealed class StateProducts{
    object Loading: StateProducts()
    data class Error(val message: String): StateProducts()
    data class Success(val data: List<ProductWithFav>): StateProducts()
}

data class ProductWithFav(
    val id: String,
    val title: String,
    val subtitle: String,
    val price: Price,
    val feedback: Feedback?,
    val tags: List<String>,
    val available: Int,
    val description: String,
    val listDopInfo: List<DopInfo>,
    val ingredients: String,
    val imgUrl: List<String>,
    val inFav: Boolean
)
