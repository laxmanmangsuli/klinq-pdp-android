package com.laxman.klinqpdp.data.remote

import com.laxman.klinqpdp.data.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApiService {

    @GET("rest/V1/productdetails/6701/253620")
    suspend fun getProductDetails(
        @Query("lang") lang: String = "en",
        @Query("store") store: String = "KWD"
    ): ProductResponse
}