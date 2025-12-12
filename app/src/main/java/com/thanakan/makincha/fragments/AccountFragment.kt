package com.thanakan.makincha.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.thanakan.makincha.databinding.FragmentAccountBinding
import java.util.Calendar

class FragmentAccount : Fragment() {

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

        val userName = "Thanakan"
        val userBio = "ผู้ใช้ทั่วไป"
        val userPhone = "0912345678"
        val userEmail = "user@email.com"

        binding.txtUserName.text = userName
        binding.txtAccountName.text = userName
        binding.txtBio.text = userBio
        binding.txtPhone.text = userPhone
        binding.txtEmail.text = userEmail

        binding.btnGender.setOnClickListener {
            genderIndex = (genderIndex + 1) % genderCycle.size
            binding.txtGender.text = genderCycle[genderIndex]
        }

        binding.btnBirthday.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val dp = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val display = "$dayOfMonth/${month + 1}/$year"
                binding.txtBirthday.text = display
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dp.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
