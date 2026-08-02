buildscript {
  repositories {
    google()
    mavenCentral()
  }
}

plugins {
  alias(libs.plugins.agp.application) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.metro) apply false
  alias(libs.plugins.ksp) apply false
}

allprojects {
  repositories {
    mavenCentral()
    google()
    maven("https://jitpack.io")
  }
}
