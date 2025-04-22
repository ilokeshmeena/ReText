plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.rexvit.retext"
    buildToolsVersion = "35.0.0"
    compileSdk = 35

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.rexvit.retext"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.multidex)
    // Core Android
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.stdlib.jdk8)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // UI Components
    implementation(libs.androidx.recyclerview)

    // Swipe Decorator
    implementation(libs.recyclerview.swipedecorator)

    // Background Processing
    implementation(libs.androidx.work)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime)

    // Database (Optional - uncomment if using Room)
    // implementation(libs.androidx.room.runtime)
    // kapt(libs.androidx.room.compiler)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}