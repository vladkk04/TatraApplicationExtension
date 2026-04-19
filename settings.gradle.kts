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

rootProject.name = "JokeAndFunStuff"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":data")
include(":domain")

include(":core")
include(":navigation")

include(":feature")
include(":feature:home")
include(":feature:shared")
include(":feature:createGroup")
include(":feature:createSpending")
include(":feature:debtGroups")
include(":feature:savingGroups")
include(":feature:qrScanner")
include(":feature:qrCreator")
include(":feature:contribute")
include(":feature:transaction")
include(":feature:pending")