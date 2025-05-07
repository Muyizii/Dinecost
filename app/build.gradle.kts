plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = "muyizii.s.dinecost"
    compileSdk = 35

    defaultConfig {
        applicationId = "muyizii.s.dinecost"
        minSdk = 29
        targetSdk = 35
        versionCode = 2
        versionName = "1.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // ===== 核心 AndroidX 基础库 =====
    implementation(libs.androidx.core.ktx)              // Android 核心 Kotlin 扩展
    implementation(libs.androidx.lifecycle.runtime.ktx)  // 生命周期运行时组件
    implementation(libs.androidx.activity.compose)      // Compose 的 Activity 集成

    // ===== Jetpack Compose 相关库 =====
    implementation(platform(libs.androidx.compose.bom))  // Compose 材料清单 (BOM 版本管理)
    implementation(libs.androidx.ui)                    // Compose 基础 UI
    implementation(libs.androidx.ui.graphics)           // Compose 图形支持
    implementation(libs.androidx.ui.tooling.preview)     // Compose 预览工具
    implementation(libs.androidx.material3)             // Material Design 3 组件
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.work.multiprocess)
    implementation(libs.gson)
    implementation (libs.androidx.lifecycle.extensions)

    // ===== 导航组件 =====
    implementation(libs.androidx.navigation.compose)    // Compose 导航功能

    // ===== 持久化存储库 =====
    implementation(libs.androidx.room.runtime)           // Room 数据库运行时
    implementation(libs.androidx.room.ktx)              // Room 的 Kotlin 扩展 (协程支持)
    ksp(libs.androidx.room.compiler)                    // Room 注解处理器 (使用 KSP)
    implementation(libs.androidx.datastore.preferences)  // DataStore 偏好设置存储

    // ===== 测试相关库 =====
    testImplementation(libs.junit)                       // 单元测试框架
    androidTestImplementation(libs.androidx.junit)       // Android 扩展单元测试
    androidTestImplementation(libs.androidx.espresso.core) // Espresso UI 测试核心

    // ===== Compose 测试相关 =====
    androidTestImplementation(platform(libs.androidx.compose.bom)) // 测试用 Compose BOM
    androidTestImplementation(libs.androidx.ui.test.junit4)         // Compose UI 测试

    // ===== 调试工具 =====
    debugImplementation(libs.androidx.ui.tooling)        // Compose 运行时调试工具
    debugImplementation(libs.androidx.ui.test.manifest) // 测试用 Manifest 处理
}