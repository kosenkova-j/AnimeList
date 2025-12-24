plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Для Hilt (внедрение зависимостей)
    id("com.google.dagger.hilt.android")
    // Для Room (если нужна поддержка kapt)
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.example.animelist"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.animelist"
        minSdk = 27  // Android 8.1+ - хороший выбор
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Room: схема БД для отладки (опционально)
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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

    buildFeatures {
        compose = true
        dataBinding = false
        viewBinding = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // БАЗОВЫЕ
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // COMPOSE UI
    implementation(platform("androidx.compose:compose-bom:2024.04.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose:1.8.2")

    // COMPOSE NAVIGATION
    implementation("androidx.navigation:navigation-compose:2.7.7")
    //implementation(libs.androidx.adapters)
    //implementation(libs.androidx.databinding.adapters)

    implementation("com.google.code.gson:gson:2.10.1")

    // DATA LAYER
    // 1. Room (локальная БД)
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version") // Coroutines поддержка
    kapt("androidx.room:room-compiler:$room_version")

    // 2. Retrofit (сетевые запросы к YummyAnime API)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")


    // 3. Coroutines (асинхронность)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // 4. DataStore (хранение токенов и настроек)
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // DEPENDENCY INJECTION (Hilt)
    val hilt_version = "2.50"
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-compiler:$hilt_version")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // IMAGE LOADING (Coil для загрузки постеров)
    implementation("io.coil-kt:coil-compose:2.5.0")

    // ДЛЯ РЕЙТИНГА (5 звезд)
    implementation(libs.compose.ratingbar)
    // ИЛИ можно сделать свой компонент - тогда зависимость не нужна

    // TESTS
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.04.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

// Для Hilt (если будут проблемы с kapt)
kapt {
    correctErrorTypes = true
}