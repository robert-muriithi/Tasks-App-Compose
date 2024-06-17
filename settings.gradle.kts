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
    }
}

rootProject.name = "ComposeTODO"
include(":app")
include(":feature")
include(":feature:tasks")
include(":feature:settings")
include(":design-system")
include(":core:database")
include(":core:network")
include(":feature:auth")
include(":core:datastore")
include(":feature:onboarding")
