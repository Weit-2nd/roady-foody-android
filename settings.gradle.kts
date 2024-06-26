pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/")
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
        maven("https://jitpack.io") // compose-ratingbar
    }
}

rootProject.name = "RoadyFoody"
include(":app")
include(":data")
include(":domain")
include(":presentation")
