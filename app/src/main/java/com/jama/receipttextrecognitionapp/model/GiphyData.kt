package com.jama.receipttextrecognitionapp.model

import com.google.gson.annotations.SerializedName

data class GiphyData(
    @SerializedName("image_url")
    val imageUrl: String
)