val composeVersion = "1.0.0-alpha04"
val coroutinesVersion = "1.3.8"
val corbindVersion = "1.4.0"
val flipperVersion = "0.55.0"
val hiltVersion = "2.28-alpha"
val roomVersion = "2.2.5"

plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  id("dagger.hilt.android.plugin")
}

android {
  compileSdkVersion(29)
  buildToolsVersion = "29.0.2"
  defaultConfig {
    applicationId = "io.github.jbarr21.appdialer"
    minSdkVersion(23)
    targetSdkVersion(29)
    versionCode = 1
    versionName = "1.0"
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
  buildFeatures {
    compose = true
    viewBinding = true
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  composeOptions {
    kotlinCompilerVersion = "1.4.0"
    kotlinCompilerExtensionVersion = composeVersion
  }
  kotlinOptions {
    jvmTarget = "1.8"
    useIR = true
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
  kapt("androidx.room:room-compiler:2.2.5")
  kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
  kapt("androidx.lifecycle:lifecycle-compiler:2.2.0")

  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.0")
  implementation("androidx.activity:activity-ktx:1.1.0")
  implementation("androidx.appcompat:appcompat:1.2.0")
  implementation("androidx.core:core-ktx:1.3.1")
  implementation("androidx.datastore:datastore-preferences:1.0.0-alpha01")
  implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
  implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
  implementation("androidx.lifecycle:lifecycle-runtime:2.2.0")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")
  implementation("androidx.lifecycle:lifecycle-viewmodel:2.2.0")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
  implementation("androidx.palette:palette:1.0.0")
  implementation("androidx.recyclerview:recyclerview:1.1.0")
  implementation("androidx.room:room-ktx:$roomVersion")
  implementation("androidx.room:room-runtime:$roomVersion")
  implementation("androidx.room:room-rxjava2:$roomVersion")
  implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
  implementation("androidx.transition:transition:1.3.1")
  implementation("androidx.compose.ui:ui:$composeVersion")
  // Tooling support (Previews, etc.)
  implementation("androidx.ui:ui-tooling:$composeVersion")
  // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
  implementation("androidx.compose.foundation:foundation:$composeVersion")
  // Material Design
  implementation("androidx.compose.material:material:$composeVersion")
  // Material design icons
  implementation("androidx.compose.material:material-icons-core:$composeVersion")
  implementation("androidx.compose.material:material-icons-extended:$composeVersion")
  // Integration with observables
  implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
  implementation("androidx.compose.runtime:runtime-rxjava2:$composeVersion")
  implementation("com.google.android.material:material:1.2.1")
  implementation("com.google.dagger:hilt-android:$hiltVersion")
  implementation("com.google.guava:guava:27.0.1-android")
  implementation("com.jakewharton:process-phoenix:2.0.0")
  implementation("com.jakewharton.timber:timber:4.7.1")
  implementation("com.squareup.coordinators:coordinators:0.4")
  implementation("dev.chrisbanes.accompanist:accompanist-coil:0.3.0")
  implementation("dev.chrisbanes:insetter-ktx:0.2.1")
  implementation("io.coil-kt:coil:0.10.1")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx2:$coroutinesVersion")

  debugImplementation("com.facebook.flipper:flipper:$flipperVersion")
  debugImplementation("com.facebook.flipper:flipper-network-plugin:$flipperVersion")
  debugImplementation("com.facebook.soloader:soloader:0.9.0")
  releaseImplementation("com.facebook.flipper:flipper-noop:$flipperVersion")

  testImplementation("junit:junit:4.12")
  testImplementation("com.google.truth:truth:0.44")

  androidTestImplementation("androidx.test:runner:1.2.0")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

  // UI Tests
  androidTestImplementation("androidx.ui:ui-test:$composeVersion")
}