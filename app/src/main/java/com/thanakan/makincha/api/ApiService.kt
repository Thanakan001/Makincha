package com.thanakan.makincha.api

import com.thanakan.makincha.models.*
import retrofit2.Response
import retrofit2.http.*

// ✅ 1. เพิ่ม Data Class สำหรับ Checkout (ตามที่คุณทำไว้แล้ว)
data class CheckoutRequest(
    val user_id: Int,
    val items: List<CartItem>
)

// ✅ 2. เพิ่ม Data Class สำหรับ Update Profile เพื่อให้ส่งแบบ JSON ได้
data class UpdateProfileRequest(
    val user_id: Int,
    val username: String,
    val email: String,
    val phone: String,
    val bio: String,
    val gender: String,
    val birthday: String
)

interface ApiService {

    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("register.php")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("confirm_password") confirm_password: String,
        @Field("email") email: String,
        @Field("phone") phone: String
    ): Response<RegisterResponse>

    @GET("get_products.php")
    suspend fun getProducts(
        @Query("category") category: String
    ): Response<ProductResponse>

    @POST("add_to_cart.php")
    suspend fun addToCart(
        @Body request: CheckoutRequest
    ): Response<AddToCartResponse>

    @GET("get_order_history.php")
    suspend fun getOrderHistory(
        @Query("user_id") userId: Int
    ): Response<OrderHistoryResponse>

    @GET("get_order_detail.php")
    suspend fun getOrderDetail(
        @Query("order_id") orderId: Int
    ): Response<OrderDetailResponse>

    @GET("get_notifications.php")
    suspend fun getNotifications(
        @Query("user_id") userId: Int
    ): Response<NotificationResponse>

    @GET("get_user_profile.php")
    suspend fun getUserProfile(
        @Query("user_id") userId: Int
    ): Response<UserProfileResponse>

    // ✅ 3. แก้ไขจาก @FormUrlEncoded เป็น @POST + @Body
    // เพื่อให้ส่งข้อมูลเป็น JSON ไปยัง update_user_profile.php ได้ถูกต้อง
    @POST("update_user_profile.php")
    suspend fun updateUserProfile(
        @Body request: UpdateProfileRequest
    ): Response<BasicResponse>
}