package com.laxman.klinqpdp.data.repository

import com.laxman.klinqpdp.data.remote.ProductApiService

class ProductRepository(
    private val apiService: ProductApiService
) {

    suspend fun getProductDetails() =
        apiService.getProductDetails()
}