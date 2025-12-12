plugins {
    // ใช้ plugin ของ Android Application
    alias(libs.plugins.android.application)

    // ใช้ plugin Kotlin สำหรับ Android
    alias(libs.plugins.kotlin.android)
}

android {
    // กำหนด namespace ของโปรเจกต์
    namespace = "com.thanakan.makincha"

    // เปลี่ยน compileSdk เป็น 36 เพื่อให้เข้ากับ dependency ล่าสุด
    compileSdk = 36

    defaultConfig {
        // Application ID ของแอป
        applicationId = "com.thanakan.makincha"

        // minSdk ระบุว่าแอปนี้สามารถรันบน Android 26 ขึ้นไป
        minSdk = 26

        // targetSdk = 36 เพื่อใช้ behavior ของ Android 36
        targetSdk = 34

        // Version ของแอป
        versionCode = 1
        versionName = "1.0"

        // Test runner สำหรับ Android Instrumentation Tests
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        // เปิดใช้งาน ViewBinding
        viewBinding = true
    }

    buildTypes {
        release {
            // ไม่ minify code (ลดความซับซ้อน)
            isMinifyEnabled = false

            // ใช้ไฟล์ Proguard สำหรับ release
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        // กำหนด Java version สำหรับการ compile
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        // JVM target ของ Kotlin
        jvmTarget = "17"
    }
}

dependencies {
    // Core KTX สำหรับฟีเจอร์ Kotlin ของ Android
    implementation(libs.androidx.core.ktx)

    implementation("androidx.activity:activity-ktx:1.11.0")
    implementation("androidx.core:core-ktx:1.17.0")

    // AppCompat สำหรับ back-compatibility UI components
    implementation(libs.androidx.appcompat)

    // Material Design Components
    implementation(libs.material)
    implementation("androidx.appcompat:appcompat:1.6.1")

    // Activity KTX สำหรับ lifecycle, intent และ extensions
    implementation(libs.androidx.activity)

    // ConstraintLayout สำหรับ layout
    implementation(libs.androidx.constraintlayout)

    // Navigation component สำหรับ fragment navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Unit testing
    testImplementation(libs.junit)

    // Android Instrumentation tests
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
