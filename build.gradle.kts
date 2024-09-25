// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.hiltAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsKotlinJvm) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.googleServices) apply false
    alias(libs.plugins.appdistribution) apply false
}

subprojects {
    configurations {
        create("ktlint")
    }

    dependencies {
        "ktlint"("com.pinterest.ktlint:ktlint-cli:1.3.1") {
            attributes {
                attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
            }
        }
    }

    tasks.register<JavaExec>("ktlintCheck") {
        group = "verification"
        description = "Check Kotlin code style."
        classpath = configurations["ktlint"]
        mainClass.set("com.pinterest.ktlint.Main")
        // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
        args("src/**/*.kt", "**.kts", "!**/build/**")
    }

    tasks.register<JavaExec>("ktlintFormat") {
        group = "formatting"
        description = "Fix Kotlin code style deviations."
        classpath = configurations["ktlint"]
        mainClass.set("com.pinterest.ktlint.Main")
        jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
        // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
        args("-F", "src/**/*.kt", "**.kts", "!**/build/**")
    }
}
