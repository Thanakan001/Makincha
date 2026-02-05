package com.thanakan.makincha.models

import com.google.gson.annotations.SerializedName

data class CartItem(
    @SerializedName("orderId")
    val orderId: Int = 0,

    @SerializedName("status") // ✅ มั่นใจว่าตรงกับ Key "status" ใน JSON ที่ PHP ส่งมา
    val status: String? = "pending",

    @SerializedName("productId")
    val productId: Int,

    @SerializedName("productName")
    val productName: String,

    @SerializedName("price")
    val price: Double,

    @SerializedName("sweetness")
    val sweetness: String,

    @SerializedName("topping")
    val topping: String?,

    @SerializedName("toppingPrice")
    val toppingPrice: Double,

    @SerializedName("amount")
    val amount: Int,

    @SerializedName("note")
    val note: String?,

    @SerializedName("imageUrl")
    val imageUrl: String,

    @SerializedName("createdAt")
    val createdAt: String? = null
) {
    // ✅ คำนวณราคาสุทธิของรายการนี้ (ราคาสินค้า + ท็อปปิ้ง) * จำนวน
    fun totalPrice(): Double = (price + toppingPrice) * amount
}