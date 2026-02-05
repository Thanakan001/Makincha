package com.thanakan.makincha.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.thanakan.makincha.R
import com.thanakan.makincha.api.ApiClient
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val username = findViewById<EditText>(R.id.username_register)
        val password = findViewById<EditText>(R.id.password_register)
        val confirm  = findViewById<EditText>(R.id.confirm_register)
        val email    = findViewById<EditText>(R.id.email_register)
        val phone    = findViewById<EditText>(R.id.phone)
        val register = findViewById<Button>(R.id.register_button)
        val login    = findViewById<Button>(R.id.loginText)

        val logoImage = findViewById<ImageView>(R.id.logoImage)
        val registerTitle = findViewById<TextView>(R.id.registerTitle)
        val subTitle = findViewById<TextView>(R.id.subTitle)
        val inputGroup = findViewById<View>(R.id.inputGroup)
        val bubble1 = findViewById<View>(R.id.bubble1)
        val bubble2 = findViewById<View>(R.id.bubble2)

        startRegisterAnimations(logoImage, registerTitle, subTitle, inputGroup, register, login, bubble1, bubble2)

        register.setOnClickListener {
            val userText = username.text.toString().trim()
            val passText = password.text.toString().trim()
            val confText = confirm.text.toString().trim()
            val mailText = email.text.toString().trim()
            val phoneText = phone.text.toString().trim()

            if (userText.isEmpty() || passText.isEmpty() || mailText.isEmpty() || phoneText.isEmpty()) {
                Toast.makeText(this, "กรุณากรอกข้อมูลให้ครบทุกช่อง", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (passText != confText) {
                Toast.makeText(this, "Password ไม่ตรงกัน", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser(userText, passText, confText, mailText, phoneText)
        }

        login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun startRegisterAnimations(vararg views: View) {
        views.forEach { view ->
            view.alpha = 0f
            view.translationY = 50f
        }
        views.forEachIndexed { index, view ->
            view.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(700)
                .setStartDelay((index * 100).toLong())
                .start()
        }
    }

    private fun registerUser(user: String, pass: String, conf: String, mail: String, tel: String) {
        lifecycleScope.launch {
            try {
                // ยิงข้อมูลผ่าน Retrofit ไปที่ Ngrok
                val response = ApiClient.api.register(user, pass, conf, mail, tel)

                if (response.isSuccessful) {
                    val body = response.body()
                    when (body?.status) {
                        "success" -> {
                            Toast.makeText(this@RegisterActivity, "สมัครสมาชิกสำเร็จ", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                            finish()
                        }
                        "username_duplicate" -> Toast.makeText(this@RegisterActivity, "Username ถูกใช้ไปแล้ว", Toast.LENGTH_SHORT).show()
                        "email_duplicate" -> Toast.makeText(this@RegisterActivity, "Email ถูกใช้ไปแล้ว", Toast.LENGTH_SHORT).show()
                        "password_mismatch" -> Toast.makeText(this@RegisterActivity, "Password ไม่ตรงกัน", Toast.LENGTH_SHORT).show()
                        "empty" -> Toast.makeText(this@RegisterActivity, "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show()
                        else -> Toast.makeText(this@RegisterActivity, body?.message ?: "เกิดข้อผิดพลาด", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, "Server Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // หากติด Error นี้ ให้เช็คว่า Ngrok URL ใน ApiClient.kt ยังเป็นอันเดิมหรือไม่
                Toast.makeText(this@RegisterActivity, "เชื่อมต่อไม่ได้: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }
}