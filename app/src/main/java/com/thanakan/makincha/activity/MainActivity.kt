package com.thanakan.makincha.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.thanakan.makincha.R

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mainToolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainToolbar = findViewById(R.id.main_toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.main_navigation_view)
        bottomNavigationView = findViewById(R.id.main_bottom_navigation_view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.productFragment,
                R.id.cartFragment,
                R.id.notificationFragment,
                R.id.accountFragment
            ),
            drawerLayout
        )

        setSupportActionBar(mainToolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // ✅ เชื่อมต่อ NavigationView กับ NavController เบื้องต้น
        navigationView.setupWithNavController(navController)

        // ✅ เพิ่มการดักคลิกสำหรับเมนูใน Drawer (รวมถึงปุ่มออกจากระบบ)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_sigout -> {
                    // เรียกฟังก์ชันออกจากระบบ
                    logoutUser()
                    true
                }
                else -> {
                    // จัดการการเปลี่ยนหน้า Fragment ตามปกติ
                    val handled = NavigationUI.onNavDestinationSelected(menuItem, navController)
                    if (handled) {
                        drawerLayout.closeDrawers()
                    }
                    handled
                }
            }
        }

        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.setOnItemSelectedListener { item ->
            val builder = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(false)
                .setPopUpTo(navController.graph.startDestinationId, false)

            navController.navigate(item.itemId, null, builder.build())
            true
        }

        val target = intent.getStringExtra("TARGET_FRAGMENT")
        if (target == "HISTORY") {
            navController.navigate(R.id.historyFragment)
        }

        bottomNavigationView.setOnItemReselectedListener { }
    }

    // ✅ ฟังก์ชันสำหรับการออกจากระบบ
    private fun logoutUser() {
        // 1. ล้างข้อมูล SharedPreferences ทั้งหมด
        val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        // 2. แจ้งเตือนผู้ใช้
        Toast.makeText(this, "ออกจากระบบสำเร็จ", Toast.LENGTH_SHORT).show()

        // 3. ย้ายไปหน้า Login และล้าง History ของ Activity ทั้งหมด
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }
}