buildscript {
  val kotlinVersion = "1.4.10"
  val hiltVersion = "2.28-alpha"
  repositories {
    google()
    jcenter()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:4.2.0-alpha13")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    classpath("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")
  }
}

allprojects {
  repositories {
    google()
    jcenter()
    maven("https://jitpack.io")
  }
}