plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id ("androidx.navigation.safeargs")
}

android {
    namespace = "com.example.weatherapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.weatherapp"
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    lint {
        // Turns off checks for the issue IDs you specify.
        disable += "ContentDescription"
        // Turns on checks for the issue IDs you specify. These checks are in
        // addition to the default lint checks.
        //   enable += "RtlHardcoded" + "RtlCompat" + "RtlEnabled"
        // To enable checks for only a subset of issue IDs and ignore all others,
        // list the issue IDs with the 'check' property instead. This property overrides
        // any issue IDs you enable or disable using the properties above.
        // checkOnly += "NewApi" + "InlinedApi"
        // If set to true, turns off analysis progress reporting by lint.
        //  quiet = true
        // If set to true (default), stops the build if errors are found.
        abortOnError = false
        // If set to true, lint only reports errors.
        //  ignoreWarnings = true
        // If set to true, lint also checks all dependencies as part of its analysis.
        // Recommended for projects consisting of an app with library dependencies.
        checkDependencies = true
        checkReleaseBuilds = true


    }
    flavorDimensions += listOf("mode")
    productFlavors {
        create("staging") {
            dimension = "mode"
            resValue(type = "string", name = "app_name", value = "Weather App Staging")
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = "\"https://weather.visualcrossing.com\""
            )
        }
        create("development") {
            dimension = "mode"
            resValue(type = "string", name = "app_name", value = "Weather App Development")
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = "\"https://weather.visualcrossing.com\""
            )
        }
        create("production") {
            dimension = "mode"
            resValue(type = "string", name = "app_name", value = "Weather App Center")
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = "\"https://weather.visualcrossing.com\""
            )
        }
    }
}
kapt {
    correctErrorTypes = true
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    //security
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    //Paging
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    //Gson
    implementation("com.google.code.gson:gson:2.10.1")
    //moshi
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    //Glide
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    kapt ("com.github.bumptech.glide:compiler:4.15.1")
    implementation ("com.caverock:androidsvg:1.4")
    // retrofit for networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // define a BOM and its version
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    // define any required OkHttp artifacts without version
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
    // logging
    implementation("com.jakewharton.timber:timber:5.0.1")
    //Hilt
    implementation ("com.google.dagger:hilt-android:2.50")
    kapt ("com.google.dagger:hilt-compiler:2.48")
    // refresh
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation ("androidx.drawerlayout:drawerlayout:1.1.1")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.0")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.0")


}