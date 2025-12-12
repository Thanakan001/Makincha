package com.thanakan.makincha.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thanakan.makincha.R
import com.thanakan.makincha.models.Order

class HistoryAdapter(private val orders: List<Order>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtOrderId: TextView = view.findViewById(R.id.txtHistoryOrderId)
        val txtStatus: TextView = view.findViewById(R.id.txtHistoryStatus)
        val txtTotal: TextView = view.findViewById(R.id.txtHistoryTotal)
        val txtItems: TextView = view.findViewById(R.id.txtHistoryItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]

        holder.txtOrderId.text = "คำสั่งซื้อ #${order.orderId}"
        holder.txtStatus.text = order.status
        holder.txtTotal.text = "รวม: ฿ %.2f".format(order.totalPrice)
        holder.txtItems.text = "จำนวนสินค้า: ${order.items.sumOf { it.amount }} ชิ้น"
    }
}
