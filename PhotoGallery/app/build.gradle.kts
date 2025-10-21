import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.serialization)
    alias(libs.plugins.kotlin.ksp)
}

val props = Properties()
val localPropsFile = rootProject.file("local.properties")
if (localPropsFile.exists()) {
    props.load(FileInputStream(localPropsFile))
}
val clientId: String = System.getenv("CLIENT_ID") ?: props["client_id"] as? String ?: ""
val clientSecret: String = System.getenv("CLIENT_SECRET") ?: props["client_secret"] as? String ?: ""

android {
    namespace = "com.learning.photogallery"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.learning.photogallery"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "CLIENT_ID", "\"$clientId\"")
        buildConfigField("String", "CLIENT_SECRET", "\"$clientSecret\"")
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        jvmToolchain(21)
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    kotlinOptions {
        freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.recycler.view)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.fragment.testing)
    implementation(libs.androidx.activity)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.converter.moshi)
    implementation(libs.squareup.moshi)
    implementation(libs.squareup.moshi.kotlin)
    ksp(libs.squareup.moshi.codegen)
    implementation(libs.coil)
    //KotlinX Serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.squareup.okhttp3)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}