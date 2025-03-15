plugins {
    id("com.android.application")  // Only this line for the Android application plugin
    id("com.google.gms.google-services")  // Google services plugin for Firebase
}

android {
    namespace = "com.example.recipegenius"
    compileSdk = 35  // Change this to 35

    defaultConfig {
        applicationId = "com.example.recipegenius"
        minSdk = 24
        targetSdk = 35  // You can also update targetSdk if necessary
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
    implementation(platform("com.google.firebase:firebase-bom:33.9.0")) // Firebase BOM for consistent versions
    implementation("com.google.firebase:firebase-auth") // Firebase Authentication
    implementation("com.google.firebase:firebase-firestore") // Firebase Firestore
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("androidx.drawerlayout:drawerlayout:1.1.1")
    implementation("com.google.android.material:material:1.11.0") // For Material components like navigation
    
    // Retrofit for API calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    
    // Picasso for image loading
    implementation("com.squareup.picasso:picasso:2.8")
    
    // RecyclerView for displaying lists
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    
    // CardView for recipe cards
    implementation("androidx.cardview:cardview:1.0.0")
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}