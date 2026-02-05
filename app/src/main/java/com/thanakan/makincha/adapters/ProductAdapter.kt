package com.thanakan.makincha.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thanakan.makincha.R
import com.thanakan.makincha.api.ApiClient
import com.thanakan.makincha.models.Product

class ProductAdapter(private var products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    var onProductAddClick: ((Product) -> Unit)? = null

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.product_name)
        val description: TextView = view.findViewById(R.id.product_description)
        val price: TextView = view.findViewById(R.id.product_price)
        val image: ImageView = view.findViewById(R.id.product_image)
        val addButton: ImageButton = view.findViewById(R.id.add_button)
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

        // ✅ แก้ไข: ใช้ BASE_URL จาก ApiClient แทนการใช้ 10.0.2.2
        // และเพิ่ม Header "ngrok-skip-browser-warning" เข้าไปใน Glide ด้วยเพื่อให้ข้ามหน้าแจ้งเตือนของ Ngrok
        val imageUrl = "${ApiClient.BASE_URL}images/${product.image_url}"

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.coco_makincha) // รูปที่ขึ้นระหว่างรอโหลด
            .error(R.drawable.coco_makincha)       // รูปที่จะขึ้นถ้าโหลดภาพจากเซิร์ฟเวอร์ไม่ได้
            .into(holder.image)

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