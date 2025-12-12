package com.thanakan.makincha.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thanakan.makincha.R
import com.thanakan.makincha.models.CartItem
import com.thanakan.makincha.models.CartManager

class CartAdapter(
    private val cartList: MutableList<CartItem>,
    private val onCartChanged: (() -> Unit)? = null
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProduct: ImageView = itemView.findViewById(R.id.imgCartProduct)
        val txtName: TextView = itemView.findViewById(R.id.txtCartName)
        val txtSweetness: TextView = itemView.findViewById(R.id.txtCartSweetness)
        val txtTopping: TextView = itemView.findViewById(R.id.txtCartTopping)
        val txtNote: TextView = itemView.findViewById(R.id.txtCartNote)
        val txtPrice: TextView = itemView.findViewById(R.id.txtCartPrice)
        val txtAmount: TextView = itemView.findViewById(R.id.txtCartAmount)
        val btnRemove: ImageView = itemView.findViewById(R.id.btnRemoveCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart_product, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartList[position]

        holder.txtName.text = item.productName
        holder.txtSweetness.text = "ความหวาน: ${item.sweetness}"
        holder.txtTopping.text = "ท็อปปิ้ง: ${item.topping}"
        holder.txtNote.text = "หมายเหตุ: ${if (item.note.isNotBlank()) item.note else "-"}"
        holder.txtPrice.text = "฿ %.2f".format(item.totalPrice())
        holder.txtAmount.text = "จำนวน:${item.amount}"

        if (item.imageResId != 0) holder.imgProduct.setImageResource(item.imageResId)
        else holder.imgProduct.setImageResource(R.drawable.coco_makincha)

        holder.btnRemove.setOnClickListener {
            CartManager.removeFromCart(item)
            cartList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartList.size)
            onCartChanged?.invoke()
        }
    }

    override fun getItemCount(): Int = cartList.size
}
