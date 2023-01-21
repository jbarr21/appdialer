buildscript {
  repositories {
    google()
    mavenCentral()
  }
}

plugins {
  alias(libs.plugins.agp.application) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.hilt) apply false
}

allprojects {
  repositories {
    mavenCentral()
    google()
    jcenter()
    maven("https://jitpack.io")
  }
}