package com.laxman.klinqpdp.data.model

import com.google.gson.annotations.SerializedName

data class ColorVariant(

    val value: String,

    @SerializedName("option_id")
    val optionId: String,

    @SerializedName("attribute_image_url")
    val attributeImageUrl: String,

    val price: String,

    val images: List<String>,

    @SerializedName("color_code")
    val colorCode: String?,

    @SerializedName("swatch_url")
    val swatchUrl: String
)