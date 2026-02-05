package com.thanakan.makincha.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.thanakan.makincha.R
import com.thanakan.makincha.adapters.OrderHistoryAdapter
import com.thanakan.makincha.api.ApiClient
import com.thanakan.makincha.databinding.FragmentHistoryBinding
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    // ✅ ใช้ onResume แทนการเรียกใน onCreateView เพียงอย่างเดียว
    // เพื่อให้เวลาผู้ใช้กด Back กลับมา สถานะจะถูกโหลดใหม่จากเซิร์ฟเวอร์ทันที
    override fun onResume() {
        super.onResume()
        loadHistory()
    }

    private fun loadHistory() {
        val sharedPref = requireActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId == -1) {
            if (isAdded) {
                Toast.makeText(requireContext(), "ไม่พบข้อมูลผู้ใช้ กรุณาเข้าสู่ระบบใหม่", Toast.LENGTH_SHORT).show()
            }
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // เรียกใช้ getOrderHistory จาก ApiClient (ที่เชื่อมกับ get_order_history.php ที่เรา JOIN ตารางไว้)
                val response = ApiClient.api.getOrderHistory(userId)

                if (response.isSuccessful && response.body()?.status == true) {
                    val items = response.body()!!.items ?: emptyList()

                    if (items.isEmpty()) {
                        binding.rvHistory.visibility = View.GONE
                        // คุณอาจจะเพิ่ม TextView แสดงคำว่า "ไม่มีประวัติ" ใน XML แล้วสั่งให้โชว์ที่นี่ได้
                    } else {
                        binding.rvHistory.visibility = View.VISIBLE
                        // ✅ เซ็ต Adapter ด้วยข้อมูลชุดใหม่ที่มี Status ล่าสุดจาก Admin
                        binding.rvHistory.adapter = OrderHistoryAdapter(items)

                        // ✅ กระตุ้น Animation ให้ทำงานแบบ Cascading
                        val resId = R.anim.layout_animation_fall_down
                        val animation = AnimationUtils.loadLayoutAnimation(requireContext(), resId)
                        binding.rvHistory.layoutAnimation = animation
                        binding.rvHistory.scheduleLayoutAnimation()
                    }

                } else {
                    if (isAdded) {
                        Toast.makeText(
                            requireContext(),
                            "โหลดประวัติไม่สำเร็จ: ${response.body()?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                if (isAdded) {
                    Toast.makeText(requireContext(), "เกิดข้อผิดพลาดในการเชื่อมต่อ", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}