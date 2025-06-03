// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
        classpath("com.google.firebase:firebase-appdistribution-gradle:5.1.1")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.2")
    }
}

plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false

    id("com.google.dagger.hilt.android") version "2.48" apply false

    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
}