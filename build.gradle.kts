buildscript {
  val kotlinVersion = "1.6.21"
  val hiltVersion = "2.36"
  repositories {
    google()
    jcenter()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:7.0.4")
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