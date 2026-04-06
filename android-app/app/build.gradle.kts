plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val rawBaseUrl = (project.findProperty("ROADMAP_BASE_URL") as String?)
    ?: System.getenv("ROADMAP_BASE_URL")
    ?: "https://example.com/"
val normalizedBaseUrl = rawBaseUrl.trim().let {
    if (it.endsWith("/")) it else "$it/"
}

android {
    namespace = "com.roadmap.eehh"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.roadmap.eehh"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        buildConfigField("String", "BASE_URL", "\"$normalizedBaseUrl\"")
    }

    buildFeatures {
        buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("io.noties.markwon:core:4.6.2")
    implementation("io.noties.markwon:inline-parser:4.6.2")
    implementation("io.noties.markwon:ext-latex:4.6.2")
}
