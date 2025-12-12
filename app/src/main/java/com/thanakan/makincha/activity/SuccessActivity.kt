package com.thanakan.makincha.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.thanakan.makincha.MainActivity
import com.thanakan.makincha.adapters.CheckoutAdapter
import com.thanakan.makincha.databinding.ActivitySuccessBinding
import com.thanakan.makincha.models.CartManager
import com.thanakan.makincha.models.Order
import com.thanakan.makincha.models.OrderManager

class SuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuccessBinding

    companion object {
        private const val PREF_ORDER = "order_pref"
        private const val KEY_LAST_ORDER_ID = "last_order_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cartItems = CartManager.getCartItems()

        val shared = getSharedPreferences(PREF_ORDER, Context.MODE_PRIVATE)
        val lastOrderId = shared.getInt(KEY_LAST_ORDER_ID, 0)
        val newOrderId = lastOrderId + 1
        shared.edit().putInt(KEY_LAST_ORDER_ID, newOrderId).apply()

        binding.txtOrderId.text = "เลขคำสั่งซื้อ #$newOrderId"

        val total = cartItems.sumOf { it.totalPrice() }
        binding.txtSuccessTotal.text = "฿ %.2f".format(total)

        val newOrder = Order(
            orderId = newOrderId,
            items = cartItems.map { it.copy() },
            totalPrice = total,
            status = "รอการดำเนินการ"
        )
        OrderManager.addOrder(newOrder)

        binding.rvSuccessProducts.layoutManager = LinearLayoutManager(this)
        binding.rvSuccessProducts.adapter = CheckoutAdapter(cartItems)

        binding.btnSuccessHome.setOnClickListener {
            CartManager.clearCart()
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }
    }
}
