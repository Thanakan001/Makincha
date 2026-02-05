package com.thanakan.makincha.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.thanakan.makincha.R
import com.thanakan.makincha.api.ApiClient
import com.thanakan.makincha.api.UpdateProfileRequest
import com.thanakan.makincha.databinding.FragmentAccountBinding
import kotlinx.coroutines.launch
import java.util.Calendar

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private val genderCycle = listOf("ตั้งค่า", "เพศชาย", "เพศหญิง")
    private var genderIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUserProfile()

        binding.btnGender.setOnClickListener {
            genderIndex = (genderIndex + 1) % genderCycle.size
            binding.txtGender.text = genderCycle[genderIndex]
            updateProfileOnServer()
        }

        binding.btnBirthday.setOnClickListener {
            showDatePicker()
        }

        binding.btnGoToHistory.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_accountFragment_to_historyFragment)
            } catch (e: Exception) {
                findNavController().navigate(R.id.historyFragment)
            }
        }
    }

    private fun loadUserProfile() {
        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId == -1) return

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = ApiClient.api.getUserProfile(userId)
                if (response.isSuccessful && response.body()?.status == true) {
                    val user = response.body()!!.data
                    binding.apply {
                        txtUserName.text = user.username
                        txtAccountName.text = user.username
                        txtBio.text = user.bio ?: ""
                        txtPhone.text = user.phone
                        txtEmail.text = user.email
                        txtGender.text = user.gender ?: "ตั้งค่า"
                        txtBirthday.text = user.birthday ?: "ตั้งค่า"
                    }
                    genderIndex = genderCycle.indexOf(user.gender ?: "ตั้งค่า").takeIf { it != -1 } ?: 0
                }
            } catch (e: Exception) {
                if (isAdded) {
                    Toast.makeText(context, "โหลดข้อมูลล้มเหลว", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val dp = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val formattedMonth = String.format("%02d", month + 1)
                val formattedDay = String.format("%02d", dayOfMonth)
                val display = "$year-$formattedMonth-$formattedDay"

                binding.txtBirthday.text = display
                updateProfileOnServer()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dp.show()
    }

    private fun updateProfileOnServer() {
        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId == -1) return

        // ดึงค่าปัจจุบันจาก UI
        val request = UpdateProfileRequest(
            user_id = userId,
            username = binding.txtUserName.text.toString(),
            email = binding.txtEmail.text.toString(),
            phone = binding.txtPhone.text.toString(),
            bio = binding.txtBio.text.toString(),
            gender = binding.txtGender.text.toString(),
            birthday = binding.txtBirthday.text.toString()
        )

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // ✅ ส่งข้อมูลแบบ @Body
                val response = ApiClient.api.updateUserProfile(request)

                if (response.isSuccessful && response.body()?.status == true) {
                    Toast.makeText(context, "บันทึกข้อมูลแล้ว", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, response.body()?.message ?: "บันทึกไม่สำเร็จ", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                if (isAdded) {
                    Toast.makeText(context, "เชื่อมต่อไม่ได้: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}