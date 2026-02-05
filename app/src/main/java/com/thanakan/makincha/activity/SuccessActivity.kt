package com.thanakan.makincha.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.thanakan.makincha.adapters.CheckoutAdapter
import com.thanakan.makincha.api.ApiClient
import com.thanakan.makincha.databinding.ActivitySuccessBinding
import kotlinx.coroutines.launch

class SuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderIdString = intent.getStringExtra("order_id")
        val orderIdInt = orderIdString?.toIntOrNull()

        if (orderIdInt == null) {
            Toast.makeText(this, "ไม่พบเลขคำสั่งซื้อที่ถูกต้อง", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.txtOrderId.text = "เลขคำสั่งซื้อ #$orderIdInt"
        loadOrderDetail(orderIdInt)

        // ✅ ปุ่มกลับหน้าหลัก
        binding.btnSuccessHome.setOnClickListener {
            goToMain(goToHistory = false)
        }

        // ✅ แก้ไข: เพิ่มปุ่มไปหน้าประวัติคำสั่งซื้อ
        binding.btnSuccessHistory.setOnClickListener {
            goToMain(goToHistory = true)
        }
    }

    private fun goToMain(goToHistory: Boolean) {
        val intent = Intent(this, MainActivity::class.java)
        // ล้าง Stack เพื่อให้เริ่มหน้าหลักใหม่
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        // ส่ง Flag ไปบอก MainActivity ว่าต้องการให้เปิดหน้าไหน
        if (goToHistory) {
            intent.putExtra("TARGET_FRAGMENT", "HISTORY")
        }

        startActivity(intent)
        finish()
    }

    private fun loadOrderDetail(orderId: Int) {
        lifecycleScope.launch {
            try {
                val response = ApiClient.api.getOrderDetail(orderId)

                if (response.isSuccessful && response.body()?.status == true) {
                    val body = response.body()!!

                    binding.txtSuccessTotal.text = "฿ %.2f".format(body.totalPrice)

                    binding.rvSuccessProducts.layoutManager = LinearLayoutManager(this@SuccessActivity)
                    binding.rvSuccessProducts.adapter = CheckoutAdapter(body.items)
                } else {
                    Toast.makeText(this@SuccessActivity, "โหลดข้อมูลไม่สำเร็จ", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SuccessActivity, "เชื่อมต่อเซิร์ฟเวอร์ไม่ได้: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}