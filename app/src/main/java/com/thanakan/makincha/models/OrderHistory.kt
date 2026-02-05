package com.thanakan.makincha.models

data class OrderHistory(
    val orderId: Int,
    val status: String,
    val totalPrice: Double,
    val items: List<CartItem>
)
