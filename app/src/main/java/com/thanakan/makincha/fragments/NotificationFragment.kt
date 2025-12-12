package com.thanakan.makincha.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thanakan.makincha.R
import com.thanakan.makincha.adapters.NotificationAdapter
import com.thanakan.makincha.models.NotificationItem
import com.google.android.material.snackbar.Snackbar

class NotificationFragment : Fragment() {

    private lateinit var rvNotifications: RecyclerView
    private val notifications = mutableListOf<NotificationItem>()
    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        rvNotifications = view.findViewById(R.id.rvNotifications)

        notifications.add(NotificationItem("คำสั่งซื้อเสร็จสิ้น", "คุณได้สั่งซื้อเรียบร้อย", "10:00"))
        notifications.add(NotificationItem("โปรโมชั่นใหม่", "ลด 10% สำหรับวันนี้", "09:00"))

        adapter = NotificationAdapter(notifications) {}
        rvNotifications.layoutManager = LinearLayoutManager(requireContext())
        rvNotifications.adapter = adapter

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                adapter.deleteItem(position)
                Snackbar.make(rvNotifications, "ลบการแจ้งเตือนแล้ว", Snackbar.LENGTH_SHORT).show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rvNotifications)

        return view
    }
}
