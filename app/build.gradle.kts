plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
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
  kapt("androidx.room:room-compiler:2.2.5")
  kapt("com.uber.motif:motif-compiler:0.0.18")

  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.72")
  implementation("androidx.appcompat:appcompat:1.1.0")
  implementation("androidx.constraintlayout:constraintlayout:1.1.3")
  implementation("androidx.core:core-ktx:1.2.0")
  implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
  implementation("androidx.lifecycle:lifecycle-runtime:2.2.0")
  implementation("androidx.palette:palette:1.0.0")
  implementation("androidx.preference:preference:1.1.1")
  implementation("androidx.recyclerview:recyclerview:1.1.0")
  implementation("androidx.room:room-ktx:2.2.5")
  implementation("androidx.room:room-runtime:2.2.5")
  implementation("androidx.room:room-rxjava2:2.2.5")
  implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.0.0")
  implementation("androidx.transition:transition:1.3.1")
  implementation("com.github.Commit451:ModalBottomSheetDialogFragment:1.1.0")
  implementation("com.github.andrefrsousa:SuperBottomSheet:1.3.0")
  implementation("com.google.android.material:material:1.1.0")
  implementation("com.google.guava:guava:27.0.1-android")
  implementation("com.uber.motif:motif:0.2.1")
  implementation("com.jakewharton:process-phoenix:2.0.0")
  implementation("com.jakewharton.rxrelay2:rxrelay:2.1.1")
  implementation("com.jakewharton.rxbinding3:rxbinding-swiperefreshlayout:3.1.0")
  implementation("com.jakewharton.timber:timber:4.7.1")
  implementation("com.squareup.coordinators:coordinators:0.4")
  implementation("com.uber.autodispose:autodispose:1.4.0")
  implementation("com.uber.autodispose:autodispose-android-archcomponents-ktx:1.1.0")
  implementation("com.uber.autodispose:autodispose-lifecycle-ktx:1.1.0")
  implementation("de.Maxr1998.android:modernpreferences:0.4.1")
  implementation("dev.chrisbanes:insetter-ktx:0.2.1")
  implementation("io.coil-kt:coil:0.10.0")
  implementation("io.reactivex.rxjava2:rxjava:2.2.9")
  implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.5")

  debugImplementation("com.facebook.flipper:flipper:0.39.0")
  debugImplementation("com.facebook.flipper:flipper-network-plugin:0.39.0")
  debugImplementation("com.facebook.soloader:soloader:0.9.0")
  releaseImplementation("com.facebook.flipper:flipper-noop:0.39.0")

  testImplementation("junit:junit:4.12")
  testImplementation("com.google.truth:truth:0.44")

  androidTestImplementation("androidx.test:runner:1.2.0")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
