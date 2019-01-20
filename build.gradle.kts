buildscript {
  repositories {
    google()
    jcenter()
  }
  dependencies {
    classpath(deps.build.androidGradlePlugin)
    classpath(deps.build.kotlinGradlePlugin)
  }
}

allprojects {
  repositories {
    google()
    jcenter()
    maven("https://jitpack.io")
  }
}
