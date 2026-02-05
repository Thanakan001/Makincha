package com.thanakan.makincha.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    // ✅ ตรวจสอบ URL ทุกครั้งที่รัน Ngrok ใหม่ (ต้องมี / ปิดท้ายเสมอ)
    const val BASE_URL = "https://4269-58-8-89-185.ngrok-free.app/makincha_api/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                // ✅ ส่วนสำคัญ: เพิ่ม Header นี้เพื่อข้ามหน้า Browser Warning ของ Ngrok
                .addHeader("ngrok-skip-browser-warning", "true")
                // ✅ ใส่ User-Agent ไว้เพื่อความปลอดภัยในการเชื่อมต่อ
                .addHeader("User-Agent", "MakinchaAndroidApp/1.0")
                .build()
            chain.proceed(request)
        }
        .build()

    val api: ApiService by lazy {
        val gson = GsonBuilder()
            .setLenient() // ช่วยให้อ่าน JSON ที่อาจมีอักขระแปลกปลอมหลุดมาได้
            .create()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}