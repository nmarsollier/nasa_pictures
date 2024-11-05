plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kotlinCompose)
}

android {
    namespace = "com.nmarsollier.nasa"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.nmarsollier.nasa"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
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
            isDebuggable = true
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
    val composeBom = platform(libs.composeBom)
    implementation(composeBom)
    implementation(libs.uiTooling)
    implementation(libs.uiToolingPreview)
    implementation(libs.foundation)
    implementation(libs.androidxLifecycleRuntimeCompose)
    implementation(libs.lifecycleViewmodelCompose)
    implementation(libs.androidxActivityCompose)
    implementation(libs.pagingRuntimeKtx)
    implementation(libs.pagingCompose)

    implementation(libs.coreKtx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.coroutinesAndroid)
    implementation(libs.coroutinesCore)

    implementation(libs.koinAndroidxCompose)
    implementation(libs.kotlinxDatetime)

    // COIL
    implementation(libs.coilCompose)
    implementation(libs.coilNetworkKtor3)

    // MATERIAL
    implementation(libs.material)
    implementation(libs.googleMaterial)
    implementation(libs.materialIconsCore)
    implementation(libs.materialIconsExtended)
    implementation(libs.accompanistNavigationMaterial)

    // ROOM
    implementation(libs.roomKtx)
    implementation(libs.roomRuntime)
    ksp(libs.roomCompiler)

    // Ktor Client Core
    implementation(libs.ktorClientCore)
    implementation(libs.ktorClientAndroid)
    implementation(libs.ktorClientContentNegotiation)
    implementation(libs.ktorSerializationKotlinxJson)
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
}
