package com.thanakan.makincha.models

import com.google.gson.annotations.SerializedName

data class BasicResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String
)