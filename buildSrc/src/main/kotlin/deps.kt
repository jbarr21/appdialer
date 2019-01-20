object deps {
  object versions {
    const val kotlin = "1.3.60"
  }

  object androidx {
    const val annotations = "androidx.annotation:annotation:1.0.1"
    const val legacyAnnotations = "com.android.support:support-annotations:28.0.0"
    const val appCompat = "androidx.appcompat:appcompat:1.1.0-alpha01"

    const val appcompat = "androidx.appcompat:appcompat:1.1.0-alpha01"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"
    const val coreKtx = "androidx.core:core-ktx:1.1.0-alpha03"
    const val recyclerView = "androidx.recyclerview:recyclerview:1.0.0"
  }

  object build {
    const val androidGradlePlugin = "com.android.tools.build:gradle:3.4.2"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${deps.versions.kotlin}"

    const val buildToolsVersion = "28.0.3"
    const val compileSdkVersion = 28
    const val minSdkVersion = 21
    const val targetSdkVersion = 28
    const val versionCode = 1
    const val versionName = "1.0.0"
  }

  object kotlin {
    const val stdLibJdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${versions.kotlin}"
  }

  object test {
    const val espresso = "androidx.test.espresso:espresso-core:3.1.1"
    const val junit = "junit:junit:4.12"
    const val testRunner = "androidx.test:runner:1.1.1"
  }
}
