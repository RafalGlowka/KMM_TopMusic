import io.gitlab.arturbosch.detekt.Detekt

@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
  id("com.android.application") version libs.versions.androidLibraries.get() apply false
  id("com.android.library") version libs.versions.androidLibraries.get() apply false
  id("org.jetbrains.compose") version libs.versions.composePlugin.get() apply false
  kotlin("android") version libs.versions.kotlin.get() apply false
  kotlin("multiplatform") version libs.versions.kotlin.get() apply false
  kotlin("native.cocoapods") version libs.versions.kotlin.get() apply false
  kotlin("plugin.serialization") version libs.versions.kotlin.get() apply false
  id("org.jetbrains.kotlin.jvm") version libs.versions.kotlin.get() apply false
  id("io.github.skeptick.libres") version libs.versions.libres.get() apply false
  id("io.realm.kotlin") version "1.8.0" apply false
  id("io.gitlab.arturbosch.detekt") version "1.23.0"
  id("io.kotest.multiplatform") version libs.versions.kotest.get() apply false
}

subprojects {
  apply(plugin = "io.gitlab.arturbosch.detekt")

  detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom("${rootProject.projectDir}/detekt.yml")
  }

  tasks.withType<Detekt>().configureEach {
    reports {
      xml.required.set(true)
      html.required.set(false)
      txt.required.set(false)
      sarif.required.set(false)
    }
    dependencies {
      detektPlugins(libs.detekt.formatting)
      detektPlugins(libs.detekt)
    }
  }
}

allprojects {
  repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
      jvmTarget = JavaVersion.VERSION_17.toString()
    }
  }
  tasks.withType<Detekt>().configureEach {
    jvmTarget = JavaVersion.VERSION_17.toString()
  }

  // Some fix for Libres.
  tasks.withType<ProcessResources>().configureEach {
    mustRunAfter(":shared:libresGenerateImages")
  }
}

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}
