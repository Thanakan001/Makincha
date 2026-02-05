package com.thanakan.makincha.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.thanakan.makincha.R
import com.thanakan.makincha.databinding.ItemHistoryDetailBinding
import com.thanakan.makincha.models.CartItem

class HistoryItemAdapter(
    private val items: List<CartItem>
) : RecyclerView.Adapter<HistoryItemAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemHistoryDetailBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // ✅ ใส่ Animation เล็กน้อยให้รายการข้างในด้วยเพื่อให้ดู Smooth ทั่วทั้งหน้า
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_animation_fall_down)

        holder.binding.txtName.text = "ชื่อสินค้า: ${item.productName}"
        holder.binding.txtSweet.text = "ความหวาน: ${item.sweetness}"
        holder.binding.txtTopping.text = "Topping: ${item.topping?.takeIf { it.isNotBlank() } ?: "-"}"
        holder.binding.txtAmount.text = "จำนวน: ${item.amount}"
        holder.binding.txtNote.text = "รายละเอียด: ${item.note?.takeIf { it.isNotBlank() } ?: "-"}"

        // ✅ แก้ไข Logic การดึงสถานะจากฐานข้อมูลมาแปลงเป็นภาษาไทย
        val statusFromDb = item.status ?: "pending"
        val statusThai = when (statusFromDb) {
            "pending" -> "รอดำเนินการ"
            "processing" -> "กำลังดำเนินการ"
            "success" -> "ดำเนินการเสร็จสิ้น"
            else -> statusFromDb
        }

        holder.binding.txtStatus.text = "สถานะ: $statusThai"

        // ✅ ปรับสีสถานะตามข้อความภาษาไทยให้ถูกต้อง
        holder.binding.txtStatus.setTextColor(
            when (statusThai) {
                "กำลังดำเนินการ" -> Color.parseColor("#FF9800") // Orange
                "ดำเนินการเสร็จสิ้น" -> Color.parseColor("#4CAF50") // Green
                else -> Color.parseColor("#F44336") // Red สำหรับ pending หรืออื่นๆ
            }
        )

        holder.binding.txtPrice.text = "฿ %.2f".format(item.totalPrice())
    }
}