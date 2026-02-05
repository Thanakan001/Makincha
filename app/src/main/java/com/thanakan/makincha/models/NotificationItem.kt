package com.thanakan.makincha.models

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("notifications")
    val notifications: List<NotificationItem>
)

data class NotificationItem(
    @SerializedName("title")
    val title: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("time")
    val time: String
)