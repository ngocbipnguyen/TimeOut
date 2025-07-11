plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs")
    id("com.google.firebase.appdistribution")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.bachnn.timeout"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.bachnn.timeout"
        minSdk = 24
        targetSdk = 35
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

            firebaseAppDistribution {
                appId = "1:28015991801:android:8e017259081ac041b7bb28"
                serviceCredentialsFile = file("/Users/bachnn/Downloads/messenger-key.json").toString()
                releaseNotes = "Version Release #1"
                testers="bachn3682@gmail.com, bachnn@vnext.vn"
            }
        }

        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            firebaseAppDistribution {
                appId = "1:28015991801:android:8e017259081ac041b7bb28"
                releaseNotes = "Version Debug #1"
                testers = "bachn3682@gmail.com, bachnn@vnext.vn"
                serviceCredentialsFile = file("/Users/bachnn/Downloads/messenger-key.json").toString()
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures{
        dataBinding = true
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    implementation("androidx.hilt:hilt-work:1.2.0")

    //hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    //retrofit
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation ("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")


    implementation ("com.google.code.gson:gson:2.10.1")

    //kapt
    kapt ("androidx.hilt:hilt-compiler:1.2.0")


    //navigation
    implementation ("androidx.navigation:navigation-fragment:2.8.0")
    implementation ("androidx.navigation:navigation-ui:2.8.0")

    // room
    val room_version = "2.5.2"

    implementation("androidx.room:room-runtime:$room_version")
    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")


}