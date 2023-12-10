plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "br.com.pablo.ordersregisterwhitelabel"
    compileSdk = 34

    defaultConfig {
        applicationId = "br.com.pablo.ordersregisterwhitelabel"
        minSdk = 24
        targetSdk = 34
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
        viewBinding.isEnabled = true
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
        buildConfig = true
    }
    flavorDimensions += listOf("corp", "role")
    productFlavors {

        create("alphacorp") {
            dimension = "corp"
            applicationIdSuffix = ".alphacorp"
            versionNameSuffix = "-alphacorp"
            manifestPlaceholders["appName"] = "Alpha Corp"
            buildConfigField("String", "FIREBASE_FLAVOR_COLLECTION", "\"alphacorp\"")
        }
        create("betacorp") {
            dimension = "corp"
            applicationIdSuffix = ".betacorp"
            versionNameSuffix = "-betacorp"
            manifestPlaceholders["appName"] = "Beta Corp"
            buildConfigField("String", "FIREBASE_FLAVOR_COLLECTION", "\"betacorp\"")
        }

        create("reader") {
            dimension = "role"
            manifestPlaceholders["appNameSuffix"] = ""
        }
        create("creator") {
            dimension = "role"
            applicationIdSuffix = ".creator"
            versionNameSuffix = "-creator"
            manifestPlaceholders["appNameSuffix"] = " - Insert Mode"
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0-alpha13")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0-alpha13")
    implementation("com.google.android.material:material:1.12.0-alpha02")
    implementation("com.google.dagger:hilt-android:2.49")
    ksp("com.google.dagger:dagger-compiler:2.49")
    ksp("com.google.dagger:hilt-android-compiler:2.49")
    ksp("com.google.dagger:hilt-compiler:2.49")
}