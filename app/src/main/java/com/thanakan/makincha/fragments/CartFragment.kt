package com.thanakan.makincha.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.thanakan.makincha.R
import com.thanakan.makincha.adapters.CartAdapter
import com.thanakan.makincha.models.CartItem
import com.thanakan.makincha.models.CartManager
import com.thanakan.makincha.activity.CheckoutActivity

class CartFragment : BottomSheetDialogFragment() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var txtTotalPrice: TextView
    private lateinit var btnCheckout: Button

    private val cartList = mutableListOf<CartItem>()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        cartRecyclerView = view.findViewById(R.id.cartRecyclerView)
        txtTotalPrice = view.findViewById(R.id.txtTotalPrice)
        btnCheckout = view.findViewById(R.id.btnCheckout)

        cartAdapter = CartAdapter(cartList) { updateTotalPrice() }
        cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        cartRecyclerView.adapter = cartAdapter

        loadCartItems()

        btnCheckout.setOnClickListener {
            if (cartList.isEmpty()) {
                Toast.makeText(requireContext(), "คุณไม่มีสินค้าอยู่ในตะกร้า", Toast.LENGTH_SHORT).show()
            } else {
                goToCheckout()
            }
        }

        return view
    }

    private fun loadCartItems() {
        cartList.clear()
        cartList.addAll(CartManager.getCartItems())
        cartAdapter.notifyDataSetChanged()
        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        val total = cartList.sumOf { it.totalPrice() }
        txtTotalPrice.text = "ราคารวม: ฿ %.2f".format(total)
    }

    private fun goToCheckout() {
        val intent = Intent(requireContext(), CheckoutActivity::class.java)
        startActivity(intent)

        dismiss()
    }

    override fun onResume() {
        super.onResume()
        loadCartItems()
    }
}



