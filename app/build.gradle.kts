plugins {
  id("com.android.application")
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.ksp)
  alias(libs.plugins.metro)
}

android {
  compileSdk = 37
  namespace = "io.github.jbarr21.appdialer"
  defaultConfig {
    applicationId = "io.github.jbarr21.appdialer"
    minSdk = 23
    targetSdk = 37
    versionCode = 2
    versionName = "0.0.3"
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
  buildFeatures {
    compose = true
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("debug")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  packagingOptions {
    resources {
      excludes += "META-INF/kotlinx_coroutines_core.version"
    }
  }
  sourceSets {
    map {
      it.java.srcDir("src/${it.name}/kotlin")
    }
  }
}

dependencies {
  ksp(libs.androidx.room.apt)

  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.window)

  implementation(libs.androidx.compose.foundation)
  implementation(libs.androidx.compose.material)
  implementation(libs.androidx.compose.material3.core)
  implementation(libs.androidx.compose.material3.windowSizeClass)
  implementation(libs.androidx.compose.materialIcons)
  implementation(libs.androidx.compose.materialIconsExtended)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.uiTooling)

  implementation(libs.androidx.compose.accompantist.systemUi)

  implementation(libs.androidx.core)
  implementation(libs.androidx.datastore)

  implementation(libs.androidx.lifecycle.extensions)
  implementation(libs.androidx.lifecycle.runtimeKtx)
  implementation(libs.androidx.lifecycle.viewmodelKtx)

  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.palette)

  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)

  implementation(libs.coil.compose)
  implementation(libs.google.material)

  implementation(libs.kotlin.coroutines)
  implementation(libs.kotlin.coroutinesAndroid)
  implementation(libs.kotlin.stdlib)

  implementation(libs.misc.processPhoenix)
  implementation(libs.misc.timber)

  implementation(libs.okhttp.core)

  testImplementation(libs.test.junit)
  testImplementation(libs.test.truth)

  androidTestImplementation(libs.test.android.espresso.core)
  androidTestImplementation(libs.test.android.runner)
  androidTestImplementation(libs.androidx.compose.uiTest)
}
