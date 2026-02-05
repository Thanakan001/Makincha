package com.thanakan.makincha.models

data class OrderHistoryResponse(
    val status: Boolean,
    val items: List<CartItem>,
    val message: String? = null
)
