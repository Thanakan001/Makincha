package com.thanakan.makincha.models

data class AddToCartResponse(
    val status: Boolean,
    val order_id: Int,
    val message: String? = null
)
