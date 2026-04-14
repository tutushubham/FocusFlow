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

rootProject.name = "StudyPartner"
include(
    ":app",
    ":core:model",
    ":core:domain",
    ":core:data",
    ":core:ui",
    ":feature:onboarding",
    ":feature:auth",
    ":feature:profile",
    ":feature:home",
    ":feature:partner",
    ":feature:session",
    ":feature:chat",
)
