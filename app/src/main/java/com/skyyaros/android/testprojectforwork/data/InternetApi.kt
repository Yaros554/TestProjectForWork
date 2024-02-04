package com.skyyaros.android.testprojectforwork.data

import com.skyyaros.android.testprojectforwork.entity.ProductsResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface InternetApi {
    @GET("v3/97e721a7-0a66-4cae-b445-83cc0bcf9010")
    suspend fun getListProducts():Response<ProductsResponse>
    companion object {
        private const val BASE_URL = "https://run.mocky.io/"
        fun provide(): InternetApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(InternetApi::class.java)
        }
    }
}