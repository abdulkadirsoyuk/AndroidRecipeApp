plugins {
    alias(libs.plugins.androidApplication)
  //  id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.recipeapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.recipeapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {



    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation ("com.firebaseui:firebase-ui-database:8.0.2")

    //  implementation("com.firebaseui:firebase-ui-database:4.2.1")

 //   implementation("com.android.support:cardview-v7:28.0.0")
  //  implementation("com.android.support:design:28.0.0")
    implementation("com.squareup.picasso:picasso:2.5.2")
    implementation ("androidx.appcompat:appcompat:1.0.0")
   // implementation("com.github.rengwuxian:MaterialEditText:2.1.4")
  //  implementation("com.amulyakhare:com.amulyakhare.textdrawable:1.0.1")
  //  implementation ('com.rengwuxian.materialedittext:library:2.1.4')
   // implementation(libs.rengwuxian)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}