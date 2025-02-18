// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
}
buildscript {
    repositories {
        google()  // Ensure this repository is included
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.0")  // Update to a version that supports API 35
        classpath("com.google.gms:google-services:4.3.15")
    }
}