package com.thanakan.makincha.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thanakan.makincha.R
import com.thanakan.makincha.adapters.CheckoutAdapter
import com.thanakan.makincha.api.ApiClient
import com.thanakan.makincha.api.CheckoutRequest
import com.thanakan.makincha.models.CartManager
import kotlinx.coroutines.launch

class CheckoutActivity : AppCompatActivity() {

    private lateinit var rvCheckoutProducts: RecyclerView
    private lateinit var txtTotalAmount: TextView
    private lateinit var txtTotalPrice: TextView
    private lateinit var txtGrandTotal: TextView
    private lateinit var btnPlaceOrder: Button
    private lateinit var btnBack: ImageView

    private lateinit var checkoutAdapter: CheckoutAdapter
    private val cartListener = { updateCartUI() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        rvCheckoutProducts = findViewById(R.id.rvCheckoutProducts)
        txtTotalAmount = findViewById(R.id.txtTotalAmount)
        txtTotalPrice = findViewById(R.id.txtTotalPrice)
        txtGrandTotal = findViewById(R.id.txtGrandTotal)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        btnBack = findViewById(R.id.btnBack)

        rvCheckoutProducts.layoutManager = LinearLayoutManager(this)
        checkoutAdapter = CheckoutAdapter(CartManager.getCartItems())
        rvCheckoutProducts.adapter = checkoutAdapter

        updateCartUI()
        CartManager.addListener(cartListener)

        btnPlaceOrder.setOnClickListener { showConfirmDialog() }
        btnBack.setOnClickListener { finish() }
    }

    private fun showConfirmDialog() {
        AlertDialog.Builder(this)
            .setTitle("ยืนยันการสั่งซื้อ")
            .setMessage("คุณต้องการสั่งซื้อสินค้านี้ใช่หรือไม่?")
            .setPositiveButton("ยืนยัน") { dialog, _ ->
                dialog.dismiss()
                submitOrder()
            }
            .setNegativeButton("ยกเลิก", null)
            .show()
    }

    private fun submitOrder() {
        val cartItems = CartManager.getCartItems()

        if (cartItems.isEmpty()) {
            Toast.makeText(this, "ไม่มีสินค้าในตะกร้า", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId == -1) {
            Toast.makeText(this, "กรุณาเข้าสู่ระบบก่อนสั่งซื้อ", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                // ✅ แก้ไข: สร้าง CheckoutRequest แทนการแปลง Gson เอง
                // Retrofit จะจัดการแปลง Object เป็น JSON ให้โดยอัตโนมัติ
                val request = CheckoutRequest(userId, cartItems)

                // ✅ แก้ไข: เรียกใช้ api.addToCart โดยส่งอ็อบเจกต์ request
                val response = ApiClient.api.addToCart(request)

                if (response.isSuccessful && response.body()?.status == true) {

                    val orderId = response.body()?.order_id ?: "0"

                    CartManager.clearCart()

                    val intent = Intent(this@CheckoutActivity, SuccessActivity::class.java)
                    intent.putExtra("order_id", orderId.toString())
                    startActivity(intent)
                    finish()

                } else {
                    // ดึง Error Message จาก Server มาแสดง
                    val errorMsg = response.body()?.message ?: "สั่งซื้อไม่สำเร็จ"
                    Toast.makeText(this@CheckoutActivity, errorMsg, Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@CheckoutActivity,
                    "เชื่อมต่อเซิร์ฟเวอร์ไม่ได้: ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateCartUI() {
        checkoutAdapter.notifyDataSetChanged()

        val cartList = CartManager.getCartItems()
        val totalAmount = cartList.sumOf { it.amount }
        val totalPrice = cartList.sumOf { it.totalPrice() }

        txtTotalAmount.text = "สินค้ารวม $totalAmount ชิ้น"
        txtTotalPrice.text = "฿ %.2f".format(totalPrice)
        txtGrandTotal.text = "฿ %.2f".format(totalPrice)
    }

    override fun onDestroy() {
        super.onDestroy()
        CartManager.removeListener(cartListener)
    }
}