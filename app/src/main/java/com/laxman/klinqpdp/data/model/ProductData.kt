package com.laxman.klinqpdp.data.model

import com.google.gson.annotations.SerializedName

data class ProductData(

    val id: String,

    val sku: String,

    val name: String,

    val price: String,

    @SerializedName("web_url")
    val webUrl: String,

    @SerializedName("brand_name")
    val brandName: String,

    val image: String,

    val description: String?,

    @SerializedName("configurable_option")
    val configurableOption: List<ConfigurableOption>,

    @SerializedName("remaining_qty")
    val remainingQty: Int,

    val images: List<String>,

    val review: Review
)