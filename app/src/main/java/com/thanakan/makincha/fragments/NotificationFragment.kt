package com.thanakan.makincha.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thanakan.makincha.R
import com.thanakan.makincha.adapters.NotificationAdapter
import com.thanakan.makincha.api.ApiClient
import com.thanakan.makincha.models.NotificationItem
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class NotificationFragment : Fragment() {

    private lateinit var rvNotifications: RecyclerView
    private lateinit var txtNotificationTitle: TextView
    private var notifications = mutableListOf<NotificationItem>()
    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        rvNotifications = view.findViewById(R.id.rvNotifications)
        txtNotificationTitle = view.findViewById(R.id.txtNotificationTitle)

        // ตั้งค่า Adapter
        adapter = NotificationAdapter(notifications)
        rvNotifications.layoutManager = LinearLayoutManager(requireContext())
        rvNotifications.adapter = adapter

        // ✅ เริ่ม Animation ของตัวหนังสือ Title ตอนเข้าหน้าจอ
        startEntranceAnimation()

        // ดึงข้อมูลจากฐานข้อมูล
        fetchNotifications()

        // Swipe to delete
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                // เก็บข้อมูลไว้เผื่อ Undo (ถ้า Adapter คุณรองรับ)
                adapter.deleteItem(position)
                Snackbar.make(rvNotifications, "ลบการแจ้งเตือนแล้ว", Snackbar.LENGTH_SHORT).show()
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvNotifications)

        return view
    }

    private fun startEntranceAnimation() {
        // ✅ ใช้ Animation ตัวเดียวกับ ProductFragment เพื่อความนุ่มนวล
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.item_animation_fall_down)
        txtNotificationTitle.startAnimation(animation)
    }

    private fun fetchNotifications() {
        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId == -1) return

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = ApiClient.api.getNotifications(userId)

                if (response.isSuccessful && response.body()?.status == true) {
                    val data = response.body()?.notifications ?: emptyList()
                    notifications.clear()
                    notifications.addAll(data)

                    adapter.notifyDataSetChanged()

                    // ✅ สั่งให้ RecyclerView เล่น Animation รายการไหลลงมาอย่างสวยงาม
                    rvNotifications.scheduleLayoutAnimation()
                }
            } catch (e: Exception) {
                if (isAdded) {
                    Toast.makeText(requireContext(), "โหลดแจ้งเตือนล้มเหลว: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}