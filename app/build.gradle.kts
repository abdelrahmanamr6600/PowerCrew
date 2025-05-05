import com.android.build.api.dsl.Packaging

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
    id ("kotlin-parcelize")

}

android {
    namespace = "com.example.powercrew"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.powercrew"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packagingOptions {
        resources {
            excludes.addAll(
                mutableSetOf(
                    "META-INF/INDEX.LIST",
                    "META-INF/DEPENDENCIES",
                    "META-INF/LICENSE",
                    "META-INF/LICENSE.txt",
                    "META-INF/NOTICE",
                    "META-INF/NOTICE.txt"
                )
            )
        }
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.lottie)
    implementation(libs.android.gif.drawable)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)
    implementation(libs.androidx.animation.core.android)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.auth)
    implementation(libs.core.ktx)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.snacking2)
    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.customtoastandroid)
    implementation(libs.datastore.preferences)
    implementation(libs.materialsearchbar)
    implementation(libs.popup.dialog)
    implementation(libs.androidx.multidex)
    implementation(libs.curved.bottom.navigation)
    implementation(libs.androidx.runtime.saved.instance.state)
    implementation(libs.roundedimageview)
    implementation(libs.google.firebase.firestore)
    implementation(libs.google.auth.library.oauth2.http)
    implementation(libs.grpc.okhttp)
    implementation(libs.okhttp)
    implementation(libs.google.http.client.gson)
    implementation(libs.retrofit)
    implementation(libs.androidx.lifecycle.process)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.kotlinx.coroutines.android.v139)
    implementation(libs.gson)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel)

    // Lifecycles only (without ViewModel or LiveData)
    implementation(libs.androidx.lifecycle.runtime)
    implementation (libs.swipeable.button)


}