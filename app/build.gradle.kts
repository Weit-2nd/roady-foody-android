import java.io.FileInputStream
import java.util.Properties

val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file(".gradle/roadyfoody.properties")))

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.appdistribution)
    kotlin("kapt")
}

android {
    namespace = "com.weit2nd.roadyfoody"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.weit2nd.roadyfoody"
        minSdk = 29
        targetSdk = 34
        versionCode = 4
        versionName = "0.0.4"
    }

    signingConfigs {
        create("release") {
            storePassword = "${localProperties["RELEASE_STORE_PASSWORD"]}"
            keyAlias = "${localProperties["RELEASE_KEY_ALIAS"]}"
            keyPassword = "${localProperties["RELEASE_KEY_PASSWORD"]}"
            storeFile = File(project.rootDir, localProperties["RELEASE_STORE_FILE"].toString())
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs["release"]
        }
        debug {
            isMinifyEnabled = false
        }
    }

    setFlavorDimensions(listOf("RoadyFoody"))
    productFlavors {
        create("production") {
            dimension = "RoadyFoody"
            firebaseAppDistribution {
                artifactType = "AAB"
                serviceCredentialsFile = "${localProperties["APP_DISTRIBUTION_PATH"]}"
                releaseNotesFile = "${localProperties["RELEASE_NOTE_PATH"]}"
                groups = "${localProperties["TESTER_GROUPS"]}"
            }
        }
        create("dev") {
            dimension = "RoadyFoody"
            applicationIdSuffix = ".dev"
            firebaseAppDistribution {
                artifactType = "AAB"
                serviceCredentialsFile = "${localProperties["APP_DISTRIBUTION_PATH"]}"
                releaseNotesFile = "${localProperties["RELEASE_NOTE_PATH"]}"
                groups = "${localProperties["TESTER_GROUPS"]}"
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
}

dependencies {
    implementation(project(":presentation"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.hilt.android)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    kapt(libs.hilt.android.compiler)
}

tasks.register<JavaExec>("uploadProductionRelease") {
    dependsOn("bundleProductionRelease", "appDistributionUploadProductionRelease")
}

tasks.register<JavaExec>("uploadDevRelease") {
    dependsOn("bundleDevRelease", "appDistributionUploadDevRelease")
}
