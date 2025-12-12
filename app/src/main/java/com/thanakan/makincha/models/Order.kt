package com.thanakan.makincha.models

data class Order(
    val orderId: Int,
    val items: List<CartItem>,
    val totalPrice: Double,
    var status: String = "รอการดำเนินการ",
    val timestamp: Long = System.currentTimeMillis()
)
