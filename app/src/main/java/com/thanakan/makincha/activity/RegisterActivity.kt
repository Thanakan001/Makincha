package com.thanakan.makincha.activity

import android.content.Intent
import android.os.Bundle
import com.thanakan.makincha.R
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.thanakan.makincha.activity.LoginActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val goToLoginBtn: Button = findViewById(R.id.loginText)
        goToLoginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val registerBtn: Button = findViewById(R.id.register_button)
        registerBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
