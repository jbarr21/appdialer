plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
}

android {
  compileSdkVersion(deps.build.compileSdkVersion)
  buildToolsVersion(deps.build.buildToolsVersion)

  defaultConfig {
    applicationId = "io.github.jbarr21.appdialer"
    minSdkVersion(deps.build.minSdkVersion)
    targetSdkVersion(deps.build.targetSdkVersion)
    versionCode = deps.build.versionCode
    versionName = deps.build.versionName

    multiDexEnabled = false
    the<BasePluginConvention>().archivesBaseName = "appdialer"
    vectorDrawables.useSupportLibrary = true
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
}

dependencies {
  kapt("androidx.room:room-compiler:2.2.2")
  kapt("com.github.bumptech.glide:compiler:4.8.0")
  kapt("com.uber.motif:motif-compiler:0.0.18")

  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${deps.versions.kotlin}")
  implementation("androidx.appcompat:appcompat:1.1.0")
  implementation("androidx.constraintlayout:constraintlayout:1.1.3")
  implementation("androidx.core:core-ktx:1.1.0")
  implementation("androidx.lifecycle:lifecycle-extensions:2.1.0")
  implementation("androidx.lifecycle:lifecycle-runtime:2.1.0")
  implementation("androidx.palette:palette:1.0.0")
  implementation("androidx.recyclerview:recyclerview:1.1.0")
  implementation("androidx.room:room-ktx:2.2.2")
  implementation("androidx.room:room-runtime:2.2.2")
  implementation("androidx.room:room-rxjava2:2.2.2")
  implementation("androidx.transition:transition:1.2.0")
  implementation("com.github.bumptech.glide:glide:4.8.0")
  implementation("com.github.Commit451:ModalBottomSheetDialogFragment:1.1.0")
  implementation("com.github.andrefrsousa:SuperBottomSheet:1.3.0")
  implementation("com.google.android.material:material:1.1.0-beta02")
  implementation("com.google.guava:guava:27.0.1-android")
  implementation("com.uber.motif:motif:0.0.18")
  implementation("com.jakewharton.timber:timber:4.7.1")
  implementation("com.squareup.picasso:picasso:2.71828")
  implementation("com.uber.autodispose:autodispose:1.1.0")
  implementation("com.uber.autodispose:autodispose-android-archcomponents-ktx:1.1.0")
  implementation("com.uber.autodispose:autodispose-lifecycle-ktx:1.1.0")
  implementation("io.reactivex.rxjava2:rxjava:2.2.9")
  implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.0")

  debugImplementation("com.facebook.flipper:flipper:0.22.0")
  debugImplementation("com.facebook.soloader:soloader:0.6.0")
  debugImplementation("com.facebook.stetho:stetho:1.5.1")
  releaseImplementation("com.facebook.flipper:flipper-noop:0.22.0")

  testImplementation("junit:junit:4.12")
  testImplementation("com.google.truth:truth:0.44")

  androidTestImplementation("androidx.test:runner:1.2.0")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
