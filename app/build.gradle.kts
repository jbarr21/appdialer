plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  alias(libs.plugins.hilt)
}

android {
  compileSdk = 34
  buildToolsVersion = "34.0.0"
  namespace = "io.github.jbarr21.appdialer"
  defaultConfig {
    applicationId = "io.github.jbarr21.appdialer"
    minSdk = 23
    targetSdk = 33
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
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  packagingOptions {
    resources {
      excludes += "META-INF/kotlinx_coroutines_core.version"
    }
  }
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  sourceSets {
    map {
      it.java.srcDir("src/${it.name}/kotlin")
//      // For kapt (stubs)
//      it.java.srcDirs.add(file("build/generated/source/kapt/$it"))
//      // For kotlin code gen during kapt
//      it.java.srcDirs.add(file("build/generated/source/kaptKotlin/$it"))
    }
  }
}

dependencies {
  kapt(libs.androidx.room.apt)
  kapt(libs.androidx.lifecycle.apt)
  kapt(libs.dagger.hilt.apt.compiler)
  kapt(libs.showkase.processor)

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
  implementation(libs.androidx.hilt.navCompose)

  implementation(libs.androidx.lifecycle.extensions)
  implementation(libs.androidx.lifecycle.runtimeKtx)
  implementation(libs.androidx.lifecycle.viewmodelKtx)

  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.palette)

  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)

  implementation(libs.coil.compose)
  implementation(libs.dagger.hilt.android)
  implementation(libs.google.material)

  implementation(libs.kotlin.coroutines)
  implementation(libs.kotlin.coroutinesAndroid)
  implementation(libs.kotlin.stdlib)

  implementation(libs.misc.processPhoenix)
  implementation(libs.misc.timber)

  implementation(libs.okhttp.core)
  implementation(libs.showkase)

  debugImplementation(libs.misc.debug.flipper)
  debugImplementation(libs.misc.debug.flipperNetwork)
  debugImplementation(libs.misc.debug.soLoader)

  testImplementation(libs.test.junit)
  testImplementation(libs.test.truth)

  androidTestImplementation(libs.test.android.espresso.core)
  androidTestImplementation(libs.test.android.runner)
  androidTestImplementation(libs.androidx.compose.uiTest)
}