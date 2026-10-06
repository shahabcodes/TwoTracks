pluginManagement {
    includeBuild("build-logic")
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
    }
}

rootProject.name = "TwoTracks"

// Pure-Kotlin modules (fast unit tests, no Android dependency)
include(":core:model")
include(":core:planner")
include(":core:seed")
include(":core:content")

// Android library modules
include(":core:common")
include(":core:ui")
include(":core:data")

// The application
include(":app")
