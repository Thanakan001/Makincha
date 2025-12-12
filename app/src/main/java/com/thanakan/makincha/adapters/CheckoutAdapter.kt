package com.thanakan.makincha.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thanakan.makincha.R
import com.thanakan.makincha.models.CartItem

class CheckoutAdapter(
    private val cartList: List<CartItem>
) : RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder>() {

    inner class CheckoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProduct: ImageView = itemView.findViewById(R.id.imgCartProduct)
        val txtName: TextView = itemView.findViewById(R.id.txtCartName)
        val txtSweetness: TextView = itemView.findViewById(R.id.txtCartSweetness)
        val txtTopping: TextView = itemView.findViewById(R.id.txtCartTopping)
        val txtNote: TextView = itemView.findViewById(R.id.txtCartNote)
        val txtPrice: TextView = itemView.findViewById(R.id.txtCartPrice)
        val txtAmount: TextView = itemView.findViewById(R.id.txtCartAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart_product, parent, false)
        return CheckoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: CheckoutViewHolder, position: Int) {
        val item = cartList[position]

        holder.txtName.text = item.productName
        holder.txtSweetness.text = "ความหวาน: ${item.sweetness}"

        holder.txtTopping.text = if (item.topping.isNotBlank()) {
            "ท็อปปิ้ง: ${item.topping} (+฿ ${item.toppingPrice})"
        } else {
            "ท็อปปิ้ง: -"
        }

        holder.txtNote.text = "หมายเหตุ: ${if (item.note.isNotBlank()) item.note else "-"}"
        holder.txtPrice.text = "฿ %.2f".format(item.totalPrice())
        holder.txtAmount.text = "จำนวน: ${item.amount}"

        if (item.imageResId != 0) {
            holder.imgProduct.setImageResource(item.imageResId)
        } else {
            holder.imgProduct.setImageResource(R.drawable.coco_makincha) // fallback
        }
    }

    override fun getItemCount(): Int = cartList.size
}
