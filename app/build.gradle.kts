plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.currencyconverterapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.currencyconverterapp"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "BASE_URL", "\"https://openexchangerates.org/\"")
        buildConfigField("String", "APP_ID", "\"4d89332822f946bda5b6310faf50b787\"")
    }

    buildTypes {
        debug {
            isDebuggable
        }
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
        viewBinding = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //di-hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    //recyclerview
    implementation(libs.androidx.recyclerview)
    //coroutine
    implementation(libs.androidx.coroutine)
    implementation(libs.androidx.coroutine.core)
    //nav-graph
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.hilt.navigation.fragment)
    //okhttp
    implementation(libs.androidx.okhttp)
    //retrofit-moshi
    implementation(libs.converter.moshi)
    //retrofit
    implementation(libs.androidx.retrofit)
    implementation(libs.androidx.retrofit.coroutine)
    //coroutine-test
    testImplementation(libs.kotlinx.coroutines.test)
    //mockk-testing
    testImplementation(libs.mockk)
 //   androidTestImplementation(libs.mockk.android)
    //mockito-testing
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.mockito.android)
    //room-db
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    //test-runner-and-rules-and-core
    implementation(libs.androidx.test.runner)
    implementation(libs.androidx.test.rules)
    implementation(libs.androidx.core.testing)
    //turbine-for-test-flows
    testImplementation(libs.turbine)
    androidTestImplementation(libs.turbine)
    // robo-electric
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.core)
    androidTestImplementation(libs.androidx.junit)
    testImplementation(libs.robolectric)
    //mock webserver
    androidTestImplementation(libs.mockwebserver)
    //hilt-testing
    androidTestImplementation(libs.hilt.android.testing)
    //moshi
    implementation(libs.moshi.kotlin)

}