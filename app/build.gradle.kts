plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
//    id("com.google.gms.google-services")

}

android {
    namespace = "com.eltescode.modules"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = ProjectConfig.appId
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Compose.composeCompilerVersion
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(Compose.compiler)
    implementation(platform(Compose.composeBoom))
    implementation(Compose.ui)
    implementation(Compose.uiGraphics)
    implementation(Compose.uiToolingPreview)
    implementation(Compose.hiltNavigationCompose)
    implementation(Compose.material3)
    implementation(Compose.runtime)
    implementation(Compose.navigation)
    implementation(Compose.viewModelCompose)
    implementation(Compose.activityCompose)
    implementation(Compose.materialIconsExtended)

    implementation(DaggerHilt.hiltAndroid)
    kapt(DaggerHilt.hiltCompiler)

    implementation(AndroidX.coreKtx)
    implementation(AndroidX.lifecycleRuntime)
    implementation(AndroidX.appCompat)

    implementation(Coil.coilCompose)

    implementation(Google.material)
//
//    implementation(Retrofit.okHttp)
//    implementation(Retrofit.retrofit)
//    implementation(Retrofit.okHttpLoggingInterceptor)
//    implementation(Retrofit.moshiConverter)

    kapt(Room.roomCompiler)
    implementation(Room.roomKtx)
    implementation(Room.roomRuntime)
    implementation(WorkManager.workManager)
}
