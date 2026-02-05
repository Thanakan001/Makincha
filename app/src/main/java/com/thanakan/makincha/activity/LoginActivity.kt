package com.thanakan.makincha.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.thanakan.makincha.R
import com.thanakan.makincha.api.ApiClient
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEdit = findViewById<EditText>(R.id.usernameEditText)
        val passwordEdit = findViewById<EditText>(R.id.passwordEditText)
        val loginBtn = findViewById<Button>(R.id.loginButton)
        val registerBtn = findViewById<Button>(R.id.registerText)

        // View สำหรับ Animation
        val logoImage = findViewById<ImageView>(R.id.logoImage)
        val welcomeTitle = findViewById<TextView>(R.id.welcomeTitle)
        val subTitle = findViewById<TextView>(R.id.subTitle)
        val inputGroup = findViewById<View>(R.id.inputGroup)
        val bubble1 = findViewById<View>(R.id.bubble1)
        val bubble2 = findViewById<View>(R.id.bubble2)

        startLoginAnimations(logoImage, welcomeTitle, subTitle, inputGroup, loginBtn, registerBtn, bubble1, bubble2)

        registerBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        loginBtn.setOnClickListener {
            val userText = usernameEdit.text.toString().trim()
            val passText = passwordEdit.text.toString().trim()

            if (userText.isEmpty() || passText.isEmpty()) {
                Toast.makeText(this, "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(userText, passText)
            }
        }
    }

    private fun startLoginAnimations(vararg views: View) {
        views.forEach { view ->
            view.alpha = 0f
            view.translationY = 50f
        }
        views.forEachIndexed { index, view ->
            view.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(800)
                .setStartDelay((index * 150).toLong())
                .start()
        }
    }

    private fun loginUser(usernameInput: String, passwordInput: String) {
        lifecycleScope.launch {
            try {
                val response = ApiClient.api.login(usernameInput, passwordInput)

                if (response.isSuccessful) {
                    val loginResponse = response.body()

                    // ✅ แก้ Error 1: ตรวจสอบสถานะ (เช็ค null safety)
                    // เปลี่ยนจาก loginResponse?.status == true เป็นการเช็คค่าที่ส่งมาจาก PHP จริงๆ
                    if (loginResponse != null && loginResponse.status == "success") {

                        // ✅ แก้ Error 3: Argument type mismatch (ใส่ ?: 0 เพื่อป้องกัน Int เป็น null)
                        val userId = loginResponse.user_id ?: 0

                        // ✅ แก้ Error 2: Unresolved reference 'username'
                        // (เช็คใน LoginResponse ว่าประกาศตัวแปรชื่ออะไร ถ้าไม่มีให้ใช้ usernameInput)
                        val finalUsername = usernameInput

                        val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putInt("user_id", userId)
                            putString("username", finalUsername)
                            apply()
                        }

                        Toast.makeText(this@LoginActivity, "เข้าสู่ระบบสำเร็จ", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        val msg = loginResponse?.message ?: "ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง"
                        Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "เชื่อมต่อไม่ได้: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }
}