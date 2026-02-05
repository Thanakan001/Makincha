package com.thanakan.makincha.models

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val description: String,
    val image_url: String,
    val category: String
)