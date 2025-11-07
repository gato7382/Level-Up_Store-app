plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.levelupstore_app"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.levelupstore_app"
        minSdk = 24
        targetSdk = 36
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
    // --- 1. JETPACK COMPOSE (El "React" de Android - UI) ---
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // --- ¡¡AÑADE ESTA LÍNEA AQUÍ!! ---
    // Esta librería contiene los iconos "Outlined", "Rounded", y "Sharp"
    implementation("androidx.compose.material:material-icons-extended")
    // --- FIN DE LÍNEA AÑADIDA ---

    // --- 2. NAVIGATION COMPOSE (El "React Router" - Navegación) ---
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // --- 3. VIEWMODELS (El "Context" - Cerebros) ---
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")

    // --- 4. DATASTORE (El "LocalStorage" - Guardar Sesión/Carrito) ---
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // --- 5. KOTLINX SERIALIZATION (El "Lector de JSON") ---
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // --- 6. COIL (Cargador de Imágenes) ---
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}