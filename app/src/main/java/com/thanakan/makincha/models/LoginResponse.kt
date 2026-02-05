package com.thanakan.makincha.models

data class LoginResponse(
    val status: String,
    val user_id: Int? = null,
    val message: String? = null
)