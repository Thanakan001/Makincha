package com.thanakan.makincha.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thanakan.makincha.databinding.ItemHistoryBinding
import com.thanakan.makincha.models.CartItem

class OrderHistoryAdapter(
    private val items: List<CartItem>
) : RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {

    // ✅ รวมสินค้าโดยใช้ orderId และเรียงลำดับจากใหม่ไปเก่า
    private val orders = items.groupBy { it.orderId }
        .toList()
        .sortedByDescending { it.first }

    inner class ViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (orderId, orderItems) = orders[position]

        // ✅ แก้ไขปัญหา #null โดยการตรวจสอบว่า orderId มีค่าจริงไหม
        holder.binding.txtOrderId.text = if (orderId != 0) {
            "รหัสคำสั่งซื้อ: #$orderId"
        } else {
            "รหัสคำสั่งซื้อ: #N/A"
        }

        // ดึงสถานะจากสินค้าชิ้นแรกในออเดอร์นั้น
        val firstItem = orderItems.firstOrNull()
        val status = firstItem?.status ?: "pending"
        holder.binding.txtOrderStatus.text = status

        // เปลี่ยนสีสถานะ
        holder.binding.txtOrderStatus.setTextColor(
            when (status.lowercase()) {
                "success", "ดำเนินการเสร็จสิ้น" -> 0xFF4CAF50.toInt() // เขียว
                "pending", "รอดำเนินการ" -> 0xFFFF9800.toInt() // ส้ม
                else -> 0xFFF44336.toInt() // แดง
            }
        )

        // ✅ คำนวณราคารวมทั้งหมดของ Order นี้
        val totalOrderPrice = orderItems.sumOf { (it.price + it.toppingPrice) * it.amount }
        holder.binding.txtOrderTotal.text = "รวม ฿ %.2f".format(totalOrderPrice)

        // ตั้งค่า RecyclerView ชั้นในสำหรับแสดงรายการสินค้าแต่ละชิ้น
        holder.binding.rvItems.apply {
            layoutManager = LinearLayoutManager(holder.itemView.context)
            adapter = HistoryItemAdapter(orderItems)
            // ป้องกันการ Scroll ทับซ้อนกัน
            isNestedScrollingEnabled = false
        }

        // ระบบ Dropdown เปิด-ปิด Card
        holder.binding.layoutItems.visibility = View.GONE
        holder.binding.root.setOnClickListener {
            val isVisible = holder.binding.layoutItems.visibility == View.VISIBLE
            holder.binding.layoutItems.visibility = if (isVisible) View.GONE else View.VISIBLE
        }
    }
}