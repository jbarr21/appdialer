buildscript {
  val kotlinVersion = "1.7.10"
  val hiltVersion = "2.44"
  repositories {
    google()
    jcenter()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:7.3.1")
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