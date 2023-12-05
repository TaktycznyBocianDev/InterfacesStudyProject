plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
    id ("com.google.gms.google-services")
}

android {
    namespace = "com.example.whatsthere"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.whatsthere"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.5.1")
    implementation(platform("androidx.compose:compose-bom:2022.10.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    implementation ("androidx.navigation:navigation-compose:2.5.3")

    implementation ("com.google.firebase:firebase-auth:22.0.0")
    implementation (platform("com.google.firebase:firebase-bom:32.0.0"))
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.firebase:firebase-firestore-ktx")
    implementation ("com.google.firebase:firebase-storage-ktx")

    implementation ("com.google.dagger:hilt-android:2.42")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation ("com.google.firebase:firebase-firestore:24.6.0")
    kapt ("com.google.dagger:hilt-android-compiler:2.42")

    implementation ("io.coil-kt:coil-compose:1.3.2")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2022.10.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //runtimeOnly("com.squareup.retrofit2:converter-moshi:2.9.0")
    //implementation ("com.squareup.moshi:moshi-kotlin:1.15.0")
    //implementation ("com.squareup.moshi:moshi:1.15.0")
    //kapt ("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")
    //implementation("com.squareup.okhttp3:okhttp:4.9.0")
    //implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    //implementation ("com.squareup.moshi:moshi-adapters:1.15.0")


    implementation ("com.squareup.moshi:moshi:1.13.0")
    kapt ("com.squareup.moshi:moshi-kotlin-codegen:1.13.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")
}