val accompanistVersion = "0.20.0"
val composeVersion = "1.0.4"
val coroutinesVersion = "1.3.8"
val flipperVersion = "0.55.0"
val hiltVersion = "2.36"
val kotlinVersion = "1.5.31"
val roomVersion = "2.3.0"

plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  id("dagger.hilt.android.plugin")
}

android {
  compileSdkVersion(31)
  buildToolsVersion = "30.0.3"
  defaultConfig {
    applicationId = "io.github.jbarr21.appdialer"
    minSdkVersion(23)
    targetSdkVersion(31)
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
  composeOptions {
    kotlinCompilerExtensionVersion = composeVersion
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
  kapt("androidx.room:room-compiler:2.3.0")
  kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
  kapt("androidx.lifecycle:lifecycle-compiler:2.4.0")

  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
  implementation("androidx.activity:activity-compose:1.4.0")
  implementation("androidx.appcompat:appcompat:1.3.1")
  implementation("androidx.compose.ui:ui:$composeVersion")
  implementation("androidx.compose.ui:ui-tooling:$composeVersion")
  implementation("androidx.compose.foundation:foundation:$composeVersion")
  implementation("androidx.compose.material:material:$composeVersion")
  implementation("androidx.compose.material:material-icons-core:$composeVersion")
  implementation("androidx.compose.material:material-icons-extended:$composeVersion")
  implementation("androidx.core:core-ktx:1.7.0")
  implementation("androidx.datastore:datastore-preferences:1.0.0")
  implementation("androidx.hilt:hilt-navigation-compose:1.0.0-alpha03")
  implementation("com.google.dagger:hilt-android:$hiltVersion")
  implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
  implementation("androidx.navigation:navigation-compose:2.4.0-alpha10")
  implementation("androidx.palette:palette:1.0.0")
  implementation("androidx.room:room-ktx:$roomVersion")
  implementation("androidx.room:room-runtime:$roomVersion")
  implementation("com.google.accompanist:accompanist-insets-ui:$accompanistVersion")
  implementation("com.google.accompanist:accompanist-navigation-animation:$accompanistVersion")
  implementation("com.google.accompanist:accompanist-swiperefresh:$accompanistVersion")
  implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")
  implementation("com.google.android.material:material:1.4.0")
  implementation("com.jakewharton:process-phoenix:2.0.0")
  implementation("com.jakewharton.timber:timber:4.7.1")
  implementation("com.squareup.okhttp3:okhttp:4.9.2")
  implementation("io.coil-kt:coil-compose:1.4.0")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

  debugImplementation("com.facebook.flipper:flipper:$flipperVersion")
  debugImplementation("com.facebook.flipper:flipper-network-plugin:$flipperVersion")
  debugImplementation("com.facebook.soloader:soloader:0.9.0")

  testImplementation("com.google.truth:truth:1.1")
  testImplementation("junit:junit:4.13.2")

  androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
  androidTestImplementation("androidx.test:runner:1.3.0")
  androidTestImplementation("androidx.compose.ui:ui-test:$composeVersion")
}