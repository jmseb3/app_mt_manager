import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

apply(from = "../keystore/signing.gradle")

android {
    namespace = "com.wonddak.mtmanger"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.wonddak.mtmanger"
        minSdk = 24
        targetSdk = 35
        versionCode = 23
        versionName = "3.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "../composeApp/proguard-rules.pro",
            )
//            signingConfig = signingConfigs.getByName("mtManagerSigning")
        }
        debug {

        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(project(":composeApp"))
    implementation(libs.androidx.activity.compose)
    implementation("com.google.android.gms:play-services-ads:23.2.0")
    implementation(libs.bundles.koin.android)
    implementation(libs.datastore.preferences)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    androidTestImplementation(libs.androidx.junit4)
    debugImplementation(libs.androidx.testManifest)
}
