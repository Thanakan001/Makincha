package com.thanakan.makincha.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thanakan.makincha.R
import com.thanakan.makincha.adapters.CheckoutAdapter
import com.thanakan.makincha.models.CartManager

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

        btnPlaceOrder.setOnClickListener {
            showConfirmDialog()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showConfirmDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ยืนยันการสั่งซื้อ")
        builder.setMessage("คุณต้องการสั่งซื้อสินค้านี้ใช่หรือไม่?")

        builder.setPositiveButton("ยืนยัน") { dialog, _ ->
            dialog.dismiss()

            val intent = Intent(this, SuccessActivity::class.java)
            startActivity(intent)
            finish()
        }

        builder.setNegativeButton("ยกเลิก") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
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
