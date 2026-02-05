package com.thanakan.makincha.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thanakan.makincha.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ 1. แสดงชื่อผู้ใช้จาก SharedPreferences
        updateGreeting()

        // ✅ 2. รัน Animation เมื่อเปิดหน้า Fragment
        startHomeAnimations()
    }

    private fun startHomeAnimations() {
        // ตั้งค่าเริ่มต้น: ซ่อน View และเลื่อนลงด้านล่างเล็กน้อย
        val viewsToAnimate = listOf(
            binding.searchBarCard,
            binding.recommendedTitle,
            binding.menuContainerCard,
            binding.topBannerCard,
            binding.bottomBannerCard
        )

        viewsToAnimate.forEach { view ->
            view.alpha = 0f
            view.translationY = 80f
        }

        // ✅ ทยอยรันอนิเมชั่นทีละส่วน (Staggered Animation)
        viewsToAnimate.forEachIndexed { index, view ->
            view.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(800)
                .setStartDelay((index * 150).toLong()) // ห่างกันตัวละ 150ms
                .start()
        }
    }

    private fun updateGreeting() {
        // ดึง SharedPreferences เพื่อแสดงชื่อ User
        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "Guest")
        binding.greetingText.text = "Hi $username!"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}