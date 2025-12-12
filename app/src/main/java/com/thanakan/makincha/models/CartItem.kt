package com.thanakan.makincha.models

data class CartItem(
    val productName: String,
    val price: Double,
    val sweetness: String,
    val topping: String,
    val toppingPrice: Double,
    var amount: Int,
    val note: String,
    val imageResId: Int
) {
    fun totalPrice(): Double = price * amount
}
