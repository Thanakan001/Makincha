package com.thanakan.makincha.models

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("data")
    val data: UserData
)

data class UserData(
    val username: String,
    val bio: String?,
    val gender: String?,
    val birthday: String?,
    val phone: String,
    val email: String
)