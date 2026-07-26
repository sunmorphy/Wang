import com.android.build.api.variant.impl.VariantOutputImpl
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "com.andikas.wang"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(FileInputStream(localPropertiesFile))
    }

    defaultConfig {
        applicationId = "com.andikas.wang"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "PASSPHRASE", "\"${localProperties.getProperty("passphraseKey")}\"")
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file(localProperties.getProperty("debugStoreFile") ?: "")
            storePassword = localProperties.getProperty("debugStorePassword") ?: ""
            keyAlias = localProperties.getProperty("debugKeyAlias") ?: ""
            keyPassword = localProperties.getProperty("debugKeyPassword") ?: ""
        }
        create("release") {
            storeFile = file(localProperties.getProperty("releaseStoreFile") ?: "")
            storePassword = localProperties.getProperty("releaseStorePassword") ?: ""
            keyAlias = localProperties.getProperty("releaseKeyAlias") ?: ""
            keyPassword = localProperties.getProperty("releaseKeyPassword") ?: ""
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
            applicationIdSuffix = ".debug"
            resValue("string", "app_name", "Debug - Wang")
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            resValue("string", "app_name", "Wang")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
        resValues = true
    }
}

androidComponents {
    onVariants { variant ->
        variant.outputs.forEach { output ->
            if (output is VariantOutputImpl) {
                val version = output.versionName.get()
                val name = "Wang-${variant.name}-v$version.apk"

                output.outputFileName.set(name)
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.compose.ui.text.google.fonts)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.navigation)

    implementation(libs.kotlinx.coroutines.android)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.ai)
    implementation(libs.firebase.ai.ondevice)

    implementation(libs.threetenbp)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.sqlcipher.android)
    implementation(libs.sqlite)

    implementation(libs.datastore.preferences)

    implementation(libs.androidx.security.crypto)
    implementation(libs.androidx.biometric)

    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m3)
    implementation(libs.vico.core)

    implementation(libs.lyricist)
    implementation(libs.lyricist.processor)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

ksp {
    arg("lyricist.internalVisibility", "true")
    arg("lyricist.generateStringsProperty", "true")
}