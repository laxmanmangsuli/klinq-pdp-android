package com.laxman.klinqpdp.data.model

import com.google.gson.annotations.SerializedName

data class Review(

    @SerializedName("total_review")
    val totalReview: Int,

    @SerializedName("rating_count")
    val ratingCount: Int
)