plugins {
    kotlin("kapt")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.nmarsollier.nasa"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nmarsollier.nasa"
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
        debug {
            isMinifyEnabled = false
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2023.08.00")
    implementation(composeBom)
    implementation("androidx.compose.material:material:1.7.2")
    implementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.lifecycle:lifecycle-runtime-compose")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.activity:activity-compose")
    implementation("androidx.paging:paging-runtime-ktx:3.3.2")
    implementation("androidx.paging:paging-compose:3.3.2")
    implementation("com.google.accompanist:accompanist-navigation-material:0.34.0")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.compose.material:material-icons-core:1.7.2")
    implementation("androidx.compose.material:material-icons-extended:1.7.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.facebook.fresco:fresco:3.0.0")
    implementation("com.facebook.infer.annotation:infer-annotation:0.18.0")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("io.insert-koin:koin-androidx-compose:3.5.3")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test-jvm:1.7.3")
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk-android:1.13.9")
    testImplementation("io.mockk:mockk-agent:1.13.9")
    testImplementation("app.cash.turbine:turbine:1.0.0")
    testImplementation("io.insert-koin:koin-android-test:3.5.3")
    testImplementation("io.insert-koin:koin-test-junit4:3.5.3")

    testImplementation("com.squareup.okhttp3:mockwebserver:4.0.0")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("io.mockk:mockk-android:1.13.9")
    androidTestImplementation("io.mockk:mockk-agent:1.13.9")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
}

kapt {
    correctErrorTypes = true
}