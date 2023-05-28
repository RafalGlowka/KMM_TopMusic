plugins {
  id("com.android.application")
  kotlin("android")
}

android {
  namespace = "com.glowka.rafal.topmusic"
  compileSdk = 33
  defaultConfig {
    applicationId = "com.glowka.rafal.topmusic"
    minSdk = 24
    targetSdk = 33
    versionCode = 1
    versionName = "1.0"
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
  }
  packagingOptions {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
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
  implementation(project(":shared"))
  implementation(libs.androidx.compose.runtime)
  implementation("androidx.compose.material:material:${libs.versions.compose.get()}")
  implementation("com.google.android.material:material:${libs.versions.androidxMaterial.get()}")
  implementation(libs.lottie)
  implementation(libs.lottieCompose)
  implementation(libs.koin.core)
  implementation(libs.koin.android)
}

detekt {
  input = files(
    "src/main/java",
  )
}