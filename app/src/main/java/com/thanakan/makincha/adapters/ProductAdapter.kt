package com.thanakan.makincha.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thanakan.makincha.R
import com.thanakan.makincha.models.Product

class ProductAdapter(private var products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // Callback สำหรับส่ง product กลับไป Fragment
    var onProductAddClick: ((Product) -> Unit)? = null

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.product_name)
        val description: TextView = view.findViewById(R.id.product_description)
        val price: TextView = view.findViewById(R.id.product_price)
        val image: ImageView = view.findViewById(R.id.product_image)
        val addButton: ImageView = view.findViewById(R.id.add_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.name.text = product.name
        holder.description.text = product.description
        holder.price.text = "฿ %.2f".format(product.price)

        if (product.imageResId != 0) {
            holder.image.setImageResource(product.imageResId)
        } else {
            holder.image.setImageResource(R.drawable.coco_makincha)
        }

        holder.addButton.setOnClickListener {
            onProductAddClick?.invoke(product)
        }
    }

    override fun getItemCount(): Int = products.size

    fun updateData(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
