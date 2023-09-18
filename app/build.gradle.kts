plugins {
    kotlin("kapt")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.exercise"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.exercise"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
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
        sourceCompatibility = JavaVersion.VERSION_1_9
        targetCompatibility = JavaVersion.VERSION_1_9
    }
    kotlinOptions {
        jvmTarget = "9"
    }

    packaging {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2023.08.00")
    implementation(composeBom)
    implementation("androidx.compose.material:material:1.5.1")
    implementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.lifecycle:lifecycle-runtime-compose")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.activity:activity-compose")
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation("androidx.paging:paging-compose:3.2.1")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("com.squareup.retrofit2:converter-gson:2.1.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.facebook.fresco:fresco:3.0.0")
    implementation("com.facebook.infer.annotation:infer-annotation:0.18.0")
    implementation("androidx.room:room-ktx:2.5.2")
    implementation("androidx.room:room-runtime:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")
    implementation("io.insert-koin:koin-androidx-compose:3.4.6")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test-jvm:1.7.3")
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk-android:1.13.7")
    testImplementation("io.mockk:mockk-agent:1.13.7")
    testImplementation("app.cash.turbine:turbine:1.0.0")
    testImplementation("io.insert-koin:koin-android-test:3.4.3")
    testImplementation("io.insert-koin:koin-test-junit4:3.4.3")

    testImplementation("com.squareup.okhttp3:mockwebserver:4.0.0")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("io.mockk:mockk-android:1.13.7")
    androidTestImplementation("io.mockk:mockk-agent:1.13.7")
}

kapt {
    correctErrorTypes = true
}