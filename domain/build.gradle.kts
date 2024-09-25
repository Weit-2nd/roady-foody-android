plugins {
    id("kotlin")
    alias(libs.plugins.jetbrainsKotlinJvm)
}

dependencies {
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
