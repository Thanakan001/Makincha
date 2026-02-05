package com.thanakan.makincha.models

import com.google.gson.annotations.SerializedName

data class OrderDetailResponse(
    val status: Boolean,

    @SerializedName("order_id")
    val orderId: Int,

    // ✅ แก้ไขจาก "total_price" เป็น "totalPrice" ให้ตรงกับ PHP
    @SerializedName("totalPrice")
    val totalPrice: Double,

    val items: List<CartItem>
)