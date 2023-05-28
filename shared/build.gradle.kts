plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  kotlin("native.cocoapods")
  id("com.android.library")
  id("org.jetbrains.compose")
  id("io.github.skeptick.libres")
  id("io.realm.kotlin")
  id("io.kotest.multiplatform")
}

kotlin {
  android()

  ios()
  iosSimulatorArm64()

  cocoapods {
    summary = "Some description for the Shared Module"
    homepage = "Link to the Shared Module homepage"
    version = "1.0"
    ios.deploymentTarget = "15.2"
    podfile = project.file("../ios/Podfile")
    framework {
      baseName = "shared"
      isStatic = true
    }
    extraSpecAttributes["resources"] = "['src/commonMain/libres/fonts/**']"
  }

  sourceSets {
    val commonMain by getting {

      dependencies {
        implementation(libs.koin.core)
        implementation(libs.logger.kermit)
        implementation(compose.ui)
        implementation(compose.foundation)
        implementation(compose.material)
        implementation(compose.runtime)
        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
        implementation(compose.components.resources)
        implementation(libs.libres.compose)
        implementation("org.jetbrains.kotlinx:atomicfu") {
          version {
            strictly("[0.20.2,)")
          }
          because("https://youtrack.jetbrains.com/issue/KT-57235")
        }
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.negotiation)
        implementation(libs.ktor.serialization)
        implementation(libs.realm.base)
        implementation(libs.kotlinx.datetime)
        implementation(libs.kotlinx.immutable)
      }
    }
    val androidMain by getting {
      dependencies {
        implementation(libs.koin.android)
        implementation(libs.android.material)
        implementation(libs.androidx.fragment.ktx)
        implementation(libs.ktor.client.android)
      }
    }
    val iosMain by getting {
      dependsOn(commonMain)
      dependencies {
        implementation(libs.logger.kermit)
        implementation(libs.ktor.client.darwin)
      }
    }
    val iosSimulatorArm64Main by getting {
      dependsOn(iosMain)
    }

    val commonTest by getting {
      val mockk_version = "1.13.5"

      dependencies{
        dependsOn(commonMain)
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
        implementation(libs.kotest.assertions.core)
        implementation(libs.kotest.framework.engine)
        implementation(libs.kotest.framework.datatest)
        implementation(libs.kotest.property)
//        implementation(libs.mockk)
//        implementation("io.mockk:mockk:$mockk_version")
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.coroutines.test)
//        implementation("io.kotest:kotest-runner-junit5:$kotest_version")

        implementation(libs.turbine)
        implementation(libs.kotlinx.coroutines.core)
      }
    }
  }
}

libres {
  generatedClassName = "MainRes"
  generateNamedArguments = true
  baseLocaleLanguageCode = "en"
  camelCaseNamesForAppleFramework = true
}

android {
  namespace = "com.glowka.rafal.topmusic"
  compileSdk = 33
  defaultConfig {
    minSdk = 24
    targetSdk = 33
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

}

detekt {
  input = files(
    "src/androidMain/kotlin",
    "src/commonMain/kotlin",
    "src/iosMain/kotlin",
  )
}