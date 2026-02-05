package com.thanakan.makincha.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thanakan.makincha.R
import com.thanakan.makincha.models.NotificationItem

class NotificationAdapter(
    private val notifications: MutableList<NotificationItem>
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtNotificationTitle)
        val txtTime: TextView = itemView.findViewById(R.id.txtNotificationTime)
        val txtMessage: TextView = itemView.findViewById(R.id.txtNotificationMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = notifications[position]
        holder.txtTitle.text = item.title
        holder.txtTime.text = item.time
        holder.txtMessage.text = item.message
    }

    override fun getItemCount(): Int = notifications.size

    fun deleteItem(position: Int) {
        if (position in notifications.indices) {
            notifications.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    // ฟังก์ชันสำหรับอัปเดตข้อมูลทั้งหมดจาก API
    fun updateData(newItems: List<NotificationItem>) {
        notifications.clear()
        notifications.addAll(newItems)
        notifyDataSetChanged()
    }
}